---
mode: agent
description: 'Run the full chat-mode workflow for a single Jira issue, from brief to PR.'
---

Run the complete workflow for issue `${input:key:Jira issue key (e.g. PROJ-123)}` end to end,
switching chat mode for each step and waiting for each to finish before moving on. Stop and report
to the user if any step fails or raises a blocking concern (code-reviewer/security-reviewer
reporting a Blocker/Critical finding) instead of proceeding.

1. **jira-story-reader** mode — fetch the issue and write `.tasks/<key>.md`.
2. Read `.tasks/<key>.md`'s **Type** field:
   - **Bug** → skip to step 4 (`bug-fixer`), do not use solution-architect.
   - **Story** → continue to step 3.
3. **solution-architect** mode — append a `## Design` section to `.tasks/<key>.md`.
4. Implementation:
   - **Bug** → **bug-fixer** mode reproduces, fixes, adds a regression test.
   - **Story** → **java-backend-dev** and/or **react-frontend-dev** mode, whichever the Design's
     Affected Modules calls for.
5. **test-writer** mode — tests from Acceptance Criteria (skip only if bug-fixer's regression
   coverage is already sufficient and test-writer confirms no gaps).
6. **code-reviewer** mode — if it reports a Blocker, stop and surface it instead of continuing.
7. **security-reviewer** mode — if it reports Critical/High, stop and surface it instead of
   continuing.
8. **git-pr-agent** mode — branch, commit, open the PR.

Summarize at the end: issue key, branch name, PR URL, and a one-line note on code-reviewer /
security-reviewer findings (or "no findings").
