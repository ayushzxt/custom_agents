# custom_agents

A full-stack task app with an AI-assisted development workflow built on Claude Code subagents.

## Stack

- **Backend**: Java 21, Spring Boot 3.3.4, Maven (`backend/pom.xml`).
  Layering: `controller -> service (interface + impl) -> repository (Spring Data JPA)`.
  DTOs are records (`dto/`), entities are JPA classes with behavior methods (`entity/`), errors
  are centralized in `exception/GlobalExceptionHandler.java` via `@RestControllerAdvice`.
  Tests: JUnit 5, Mockito, MockMvc (`@WebMvcTest`), Testcontainers for integration tests.
- **Frontend**: React 18, TypeScript (strict), Vite (`frontend/`).
  Functional components + hooks only. API calls go through a typed client in `src/api/`, shared
  types in `src/types/`. Tests: Vitest + React Testing Library.

Run locally: `cd backend && ./mvnw spring-boot:run` and `cd frontend && npm install && npm run dev`.

## Code conventions (all agents must follow these)

- Constructor injection only, never field injection.
- DTO/entity separation is strict — entities never cross the controller boundary.
- No N+1 queries — check repository access patterns before considering a change done.
- Design patterns (Strategy, Factory, Builder, Adapter, Template Method, Observer, Decorator) are
  applied only where the problem's shape actually calls for them, with a one-line justification
  when used.
- Match existing file/package conventions exactly rather than introducing new structure.

## MCP servers used by these agents

- **Atlassian Rovo (Jira)** — connected. `jira-story-reader` uses it read-only
  (`mcp__Atlassian_Rovo__*` tools) to fetch issues; it never comments, edits, or transitions Jira
  issues.
- **Figma** — not connected yet. `react-frontend-dev` currently works from design details you
  paste inline or answer questions about. To enable direct Figma access: connect the Figma Dev
  Mode MCP server under claude.ai → Settings → Connectors (or ask an org admin to), enable it for
  this project, then add its tools (e.g. `get_code`, `get_screenshot`, `get_variable_defs`) to
  `.claude/agents/react-frontend-dev.md`'s `tools:` line.
- **GitHub** — `git-pr-agent` uses the `gh` CLI via `Bash`, not an MCP server. Make sure `gh` is
  installed and authenticated (`gh auth status`) in whatever environment runs this agent.

## Agent team and workflow

Nine subagents live in `.claude/agents/`. Claude Code delegates to them automatically based on
their `description`, or you can invoke the workflow explicitly with `/start-task <KEY>` or
`/my-tasks`.

**Story workflow:**

```
jira-story-reader -> solution-architect -> java-backend-dev / react-frontend-dev -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

**Bug workflow** (skips the architect and dev-implementation steps in favor of one focused agent):

```
jira-story-reader -> bug-fixer -> test-writer -> code-reviewer -> security-reviewer -> git-pr-agent
```

| Agent | Role | Tools | Model |
|---|---|---|---|
| `jira-story-reader` | Fetch Jira issues, write `.tasks/<KEY>.md` briefs | Read, Write, Grep, Glob, Jira MCP (read-only) | sonnet |
| `solution-architect` | Design the implementation, append to the brief | Read, Grep, Glob, Edit | opus |
| `java-backend-dev` | Implement backend changes | Read, Write, Edit, Bash, Grep, Glob | sonnet |
| `react-frontend-dev` | Implement frontend changes | Read, Write, Edit, Bash, Grep, Glob | sonnet |
| `test-writer` | Write tests from acceptance criteria | Read, Write, Edit, Bash, Grep, Glob | sonnet |
| `bug-fixer` | Reproduce, root-cause, fix, regression test | Read, Write, Edit, Bash, Grep, Glob | sonnet |
| `code-reviewer` | Review the diff (SOLID, bugs, perf, naming, tests) | Read, Grep, Glob, Bash | sonnet |
| `security-reviewer` | OWASP/security review of the diff | Read, Grep, Glob, Bash | opus |
| `git-pr-agent` | Branch, commit, open PR | Read, Grep, Glob, Bash | sonnet |

Read-only agents (`jira-story-reader` re: Jira, `solution-architect`, `code-reviewer`,
`security-reviewer`) deliberately don't have broad write access — `solution-architect` gets `Edit`
only to append its design section to the brief file.

## Task briefs

`jira-story-reader` and `solution-architect` write to `.tasks/<KEY>.md` — one file per Jira issue,
gitignored (local scratch space, not shared via git). Don't hand-edit these; they're regenerated
by the agents. Downstream agents (implementation, test-writer, git-pr-agent) read them for context
but only `jira-story-reader` and `solution-architect` write to them.

## Git

- Never commit to `main` directly; every change goes through a branch named `<KEY>-short-title`.
- Conventional Commits (`feat:`, `fix:`, `test:`, `refactor:`, `chore:`, `docs:`).
- No force-pushes, no history rewriting on branches with an open PR.
