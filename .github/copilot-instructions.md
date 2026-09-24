# Project rules

Stack: Java 21 / Spring Boot 3.3 / Maven in `backend/`; React 18 / TypeScript strict / Vite in `frontend/`.
Area rules live in `.github/instructions/` and apply automatically by path.

## Pipeline

Story: jira-story-reader → solution-architect → java-backend-dev / react-frontend-dev → test-writer → code-reviewer → security-reviewer → git-pr-agent
Bug: jira-story-reader → bug-fixer → test-writer → code-reviewer → security-reviewer → git-pr-agent

Stages hand off through `.tasks/<KEY>.md` (gitignored, agent-generated — don't hand-edit).
Stop and report to the user if code-reviewer finds a Blocker or security-reviewer a Critical/High.

## Always

- Never invent requirements; unclear items go under Open Questions in the brief.
- Match existing file, package, and naming conventions.
- Design patterns only where they fit, with a one-line justification.
- Never commit to `main` or force-push. Branch `<KEY>-short-title`, Conventional Commits.
- Report what changed and what was actually run — never claim a check you didn't run.
