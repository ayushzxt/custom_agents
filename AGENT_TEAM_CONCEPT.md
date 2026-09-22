# The Agent Team Concept

How this repo turns a Jira ticket into a reviewed pull request using a team of specialized
Claude Code subagents instead of one general-purpose assistant.

---

## The problem

A single AI assistant handed "implement PROJ-123" tends to do all of it at once: half-read the
ticket, guess at missing requirements, write code and tests in one pass, and declare victory
without a real review. The failure modes are predictable:

- **Invented requirements.** The ticket is ambiguous, so the assistant fills the gap with a
  plausible-sounding assumption and never flags it.
- **No design step.** Code gets written before anyone decides what the API or data model should
  look like, so the shape is wrong and the rework is expensive.
- **Self-review.** The same context that wrote the code reviews it, and it finds nothing —
  an author is the worst reviewer of their own work.
- **Unbounded blast radius.** An assistant with write access to everything can "fix" a review
  finding by editing the thing it was supposed to be reviewing.

## The idea

Split the work into stages, give each stage a dedicated agent, and give each agent **only the
tools its job requires**. That produces three properties the single-assistant approach can't have:

1. **Focused context.** Each agent starts with a narrow job and a narrow prompt. The reviewer
   isn't distracted by the implementation reasoning that produced the diff.
2. **Enforced separation of duties.** The code reviewer and security reviewer have no edit tool at
   all — not as a policy they're asked to follow, but as a capability they don't have. They
   physically cannot rewrite the code they're judging.
3. **An auditable handoff.** Every stage reads and writes a shared artifact (the task brief), so
   the chain from "what the ticket said" to "what shipped" is inspectable at any point.

## The pipeline

**Stories** go through the full design-first flow:

```
jira-story-reader -> solution-architect -> java-backend-dev / react-frontend-dev
                  -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

**Bugs** skip the architect and the dev agents — designing a bug fix up front is waste, and the
fix should be minimal:

```
jira-story-reader -> bug-fixer -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

The branch point is the `Type` field in the brief, so the routing decision is made from ticket
data, not from a guess.

## The handoff artifact: `.tasks/<KEY>.md`

The pipeline's connective tissue is one Markdown file per Jira issue. It's the single place where
requirements, design, and open questions live, and it's why the agents don't need to share a
conversation.

| Stage | What it does to the brief |
|---|---|
| `jira-story-reader` | Creates it: summary, acceptance criteria checklist, bug repro steps, likely affected code, draft plan, testing notes, open questions |
| `solution-architect` | Appends a `## Design` section: affected modules, API contract, data model changes, sequence, risks |
| Dev / test agents | Read it — acceptance criteria drive both the implementation and the tests |
| `git-pr-agent` | Reads it for the Jira link and summary that go into the PR description |

Two rules make this work:

- **Ambiguity is never silently resolved.** Anything the ticket doesn't say goes under
  **Open Questions**, where a human (or the architect's Risks section) has to deal with it. An
  agent inventing a requirement is treated as a bug, not initiative.
- **The brief is generated, not hand-edited.** It's gitignored — local scratch, regenerated from
  Jira whenever the ticket changes.

## The nine agents

| Agent | Owns | Can edit code? |
|---|---|---|
| `jira-story-reader` | Requirements intake from Jira | Only `.tasks/` |
| `solution-architect` | Design before implementation | Only `.tasks/` |
| `java-backend-dev` | Spring Boot implementation | Yes |
| `react-frontend-dev` | React/TypeScript implementation | Yes |
| `test-writer` | Tests derived from acceptance criteria | Tests only |
| `bug-fixer` | Reproduce → root cause → minimal fix → regression test | Yes |
| `code-reviewer` | SOLID, bugs, performance, naming, missing tests | **No** |
| `security-reviewer` | OWASP Top 10, secrets, dependencies | **No** |
| `git-pr-agent` | Branch, conventional commits, PR | No (git only) |

Each agent's file ends with a **Definition of done** — an explicit checklist it must satisfy
before handing off. That's what keeps "done" from meaning "the model stopped typing."

## How it's wired in Claude Code

```
.claude/
  agents/            # one .md per agent: YAML frontmatter (name, description, tools, model) + system prompt
    jira-story-reader.md
    solution-architect.md
    ...
  commands/          # slash commands
    start-task.md    # /start-task <KEY> — runs the whole pipeline for one issue
    my-tasks.md      # /my-tasks — lists your open issues and briefs them
CLAUDE.md            # project conventions, loaded automatically into every session
```

Three mechanisms do the work:

- **`description` drives automatic delegation.** Claude Code reads each agent's description and
  dispatches to it on its own — mention a Jira key and `jira-story-reader` picks it up without
  being named. The descriptions are written as "use when…" statements for exactly this reason.
- **`tools` enforces least privilege.** The frontmatter list is the agent's entire capability set.
  `code-reviewer` and `security-reviewer` have no `Edit`/`Write` at all; `solution-architect` gets
  `Edit` solely to append its design to the brief.
- **`model` matches cost to difficulty.** `solution-architect` and `security-reviewer` run on
  Opus, where deeper reasoning actually pays for itself; the rest run on Sonnet.

`CLAUDE.md` holds the conventions every agent shares (stack, layering, DTO rules, git policy), so
they're stated once rather than duplicated nine times.

## Running it

```
/my-tasks              # what's assigned to me? writes a brief per issue
/start-task PROJ-123   # full pipeline for one issue, brief through PR
```

Or just describe the work — the agents' descriptions are written so Claude routes to the right one
automatically.

## Extending it

- **New stack, new rules**: edit `CLAUDE.md` first (shared conventions), then only the agents whose
  job actually changes.
- **New agent**: add a `.claude/agents/<name>.md` with a "use when…" description, the minimum
  tools, and a Definition of done. Add it to the pipeline in `CLAUDE.md` and
  `.claude/commands/start-task.md`.
- **Tighter permissions**: the `tools` line is the lever. If an agent shouldn't be able to do
  something, remove the tool rather than adding a rule asking it not to.

## Prerequisites

- **Jira** via the Atlassian MCP connector — `jira-story-reader` uses read-only operations only.
- **Figma** via MCP — optional; `react-frontend-dev` falls back to design details you provide
  inline if it isn't connected.
- **`gh` CLI**, authenticated — `git-pr-agent` uses it to open pull requests.

---

The same pipeline exists for GitHub Copilot on the `copilot/agent-team` branch, using Copilot's
custom agents (`.github/agents/*.agent.md`) — same nine roles, same permission model, different
host.
