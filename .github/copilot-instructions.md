# custom_agents — Copilot instructions

This file is loaded automatically into every Copilot Chat / Copilot coding agent request in this
repo. Individual agents in `.github/agents/*.agent.md` add role-specific process on top of this —
keep stack/convention rules here so they aren't duplicated nine times.

## Stack

- **Backend**: Java 21, Spring Boot 3.3.4, Maven (`backend/pom.xml`). Layering:
  `controller -> service (interface + impl) -> repository (Spring Data JPA)`. DTOs are records
  (`dto/`), entities are JPA classes with behavior methods (`entity/`), errors are centralized in
  `exception/GlobalExceptionHandler.java` via `@RestControllerAdvice`. Tests: JUnit 5, Mockito,
  MockMvc (`@WebMvcTest`), Testcontainers for integration tests.
- **Frontend**: React 18.3, TypeScript 5.5 (strict), Vite 5.4 (`frontend/`). Functional
  components + hooks only. Typed API client in `src/api/`, shared types in `src/types/`. Tests:
  Vitest 2 + React Testing Library.

Detailed, automatically-applied conventions per area live in `.github/instructions/`:
`backend-java.instructions.md` (applies to `backend/**`) and `frontend-react.instructions.md`
(applies to `frontend/**`).

## Agent pipeline

```
jira-story-reader -> solution-architect -> [java-backend-dev | react-frontend-dev] -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

Bugs skip the architect and dev-implementation steps in favor of one focused agent:

```
jira-story-reader -> bug-fixer -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

Each name above is defined in `.github/agents/<name>.agent.md`. Run `/start-task <KEY>` for a
guided walkthrough of the whole pipeline, or `/my-tasks` to just gather and brief your open
issues (`.github/prompts/`).

| Agent | Role | Tools | Edits code? |
|---|---|---|---|
| `jira-story-reader` | Fetch Jira issues, write `.tasks/<KEY>.md` | `atlassian/*` (read ops only, by instruction) | Only `.tasks/` |
| `solution-architect` | Design the implementation, append to the brief | `codebase`, `search`, `usages` | Only `.tasks/` |
| `java-backend-dev` | Implement backend changes | `codebase`, `search`, `usages`, `runCommands`, `runTests` | Yes |
| `react-frontend-dev` | Implement frontend changes | `codebase`, `search`, `usages`, `runCommands`, `runTests`, `figma/*` | Yes |
| `test-writer` | Write tests from acceptance criteria | `codebase`, `search`, `runCommands`, `runTests` | Yes (tests only) |
| `bug-fixer` | Reproduce, root-cause, fix, regression test | `codebase`, `search`, `usages`, `runCommands`, `runTests` | Yes |
| `code-reviewer` | Review the diff (SOLID, bugs, perf, naming, tests) | `codebase`, `search`, `changes`, `runCommands` | No |
| `security-reviewer` | OWASP/security review of the diff | `codebase`, `search`, `changes`, `runCommands` | No |
| `git-pr-agent` | Branch, commit, open PR | `runCommands`, `changes`, `githubRepo` | No (git/PR only) |

Read-only agents (`code-reviewer`, `security-reviewer`) have no `edit` tool at all.
`jira-story-reader` and `solution-architect` get `edit` for one reason only — writing/appending to
`.tasks/<KEY>.md` — never for application source, as stated in each agent's own file.

## Task briefs

`.tasks/<KEY>.md` holds the per-issue brief (written by `jira-story-reader`, design appended by
`solution-architect`). It's gitignored — local scratch, not shared via git. Don't hand-edit it.

## MCP servers

Configured in `.vscode/mcp.json` (a starter template is committed — replace the placeholder URLs
with your real Atlassian/Figma MCP endpoints; that file wasn't present in this repo before this
setup, so verify the server names below still match what you actually register there).

- **`atlassian`** (Jira): agents reference its tools as `atlassian/*`. Only `jira-story-reader`
  declares it, and its prompt restricts usage to read/search/get operations — never
  comment/edit/transition/create, even though the wildcard grants the full toolset. If you scope
  credentials, read-only Jira API scopes are sufficient for this whole setup.
- **`figma`**: agents reference its tools as `figma/*`. Only `react-frontend-dev` declares it, to
  read frames/tokens/spacing/typography/colors — never to write back to Figma.
- **GitHub**: `git-pr-agent` uses the `gh` CLI via `runCommands`, not an MCP server — ensure
  `gh auth status` succeeds in your environment.

If your actual MCP server names in `.vscode/mcp.json` differ from `atlassian` / `figma`, update
the `tools:` line in every `.github/agents/*.agent.md` file that references them to match.

## Non-negotiables (all agents)

- Constructor injection only, never field injection (backend).
- DTO/entity separation is strict — entities never cross the controller boundary.
- No N+1 queries.
- Functional React components + hooks only; cover loading/empty/error/success states for any
  data-fetching UI; accessibility (semantic HTML, keyboard access, ARIA) is not optional.
- Design patterns applied only where they fit, with a one-line justification when used.
- Match existing file/package conventions exactly.
- Never commit to `main` directly; branch as `<KEY>-short-title`, Conventional Commits, no
  force-pushes.

## Note on `.github/chatmodes/`

This repo also has VS Code custom chat modes (`.github/chatmodes/*.chatmode.md`) covering the same
nine roles, from an earlier pass before `.github/agents/` (the current, primary mechanism) was set
up. They're kept for manual mode-switching in VS Code Copilot Chat if you prefer that over
`@agent-name` invocation. The two are not auto-synced — if you change conventions here or in an
`.agent.md` file, update the matching `.chatmode.md` too if you want them to stay consistent, or
remove `.github/chatmodes/` if you'd rather standardize on one mechanism.
