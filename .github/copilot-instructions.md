# custom_agents — Copilot instructions

This file is loaded automatically into every Copilot Chat request in this repo (repo-wide custom
instructions). Role-specific chat modes in `.github/chatmodes/` add to this, they don't replace it
— keep stack/convention rules here so they aren't duplicated in every mode.

> **How this differs from the Claude Code setup on `claude/agent-team`:** that branch uses Claude
> Code subagents, which Claude auto-delegates to based on each agent's description. GitHub
> Copilot's custom chat modes (`.github/chatmodes/*.chatmode.md`) are the closest equivalent, but
> in VS Code they are **selected manually** from the chat mode dropdown (or via `@workspace` +
> mode switch) — there is no automatic "pick the right agent for this request" dispatch. The
> `.github/prompts/*.prompt.md` files are the equivalent of Claude's `/start-task` and `/my-tasks`
> slash commands and *are* invokable as `/start-task` / `/my-tasks` in Copilot Chat.

## Stack

- **Backend**: Java 21, Spring Boot 3.3.4, Maven (`backend/pom.xml`).
  Layering: `controller -> service (interface + impl) -> repository (Spring Data JPA)`. DTOs are
  records (`dto/`), entities are JPA classes with behavior methods (`entity/`), errors are
  centralized in `exception/GlobalExceptionHandler.java` via `@RestControllerAdvice`. Tests:
  JUnit 5, Mockito, MockMvc (`@WebMvcTest`), Testcontainers for integration tests.
- **Frontend**: React 18, TypeScript (strict), Vite (`frontend/`). Functional components + hooks
  only. Typed API client in `src/api/`, shared types in `src/types/`. Tests: Vitest + React
  Testing Library.

Detailed, automatically-applied conventions per area live in `.github/instructions/`:
`backend-java.instructions.md` (applies to `backend/**`) and `frontend-react.instructions.md`
(applies to `frontend/**`).

## Workflow

**Story**: `jira-story-reader` → `solution-architect` → `java-backend-dev` / `react-frontend-dev`
→ `test-writer` → `code-reviewer` → `security-reviewer` → `git-pr-agent`

**Bug**: `jira-story-reader` → `bug-fixer` → `test-writer` → `code-reviewer` →
`security-reviewer` → `git-pr-agent`

Each name above is a chat mode in `.github/chatmodes/`. Switch modes manually as you move through
the workflow, or run `/start-task <KEY>` for a guided walkthrough of the whole sequence.

## Task briefs

`.tasks/<KEY>.md` holds the per-issue brief (written by `jira-story-reader`, design appended by
`solution-architect`). It's gitignored — local scratch, not shared via git. Don't hand-edit it.

## MCP servers

- **Jira (Atlassian)**: connect an Atlassian MCP server (e.g. via `.vscode/mcp.json` or your
  Copilot MCP settings) so `jira-story-reader` mode can call it. Grant it **read-only** Jira
  scopes/tools only — issue search/get/comments/links, never edit/comment/transition — and list
  its exact tool names in `jira-story-reader.chatmode.md`'s `tools:` array once connected (they
  aren't filled in yet since no Atlassian MCP server was connected to Copilot when this branch was
  created).
- **Figma**: not connected. `react-frontend-dev` mode currently works from design details you
  paste in or describe. Connect the Figma Dev Mode MCP server the same way, then add its tools
  (e.g. `get_code`, `get_screenshot`, `get_variable_defs`) to that mode's `tools:` array.
- **GitHub**: `git-pr-agent` mode uses the `gh` CLI via the `runCommands` tool rather than an MCP
  server — ensure `gh auth status` succeeds in your environment.

## Non-negotiables (all modes)

- Constructor injection only, never field injection (backend).
- DTO/entity separation is strict — entities never cross the controller boundary.
- No N+1 queries.
- Functional React components + hooks only; cover loading/empty/error/success states for any
  data-fetching UI; accessibility (semantic HTML, keyboard access, ARIA) is not optional.
- Design patterns applied only where they fit, with a one-line justification when used.
- Match existing file/package conventions exactly.
- Never commit to `main` directly; branch as `<KEY>-short-title`, Conventional Commits, no
  force-pushes.
