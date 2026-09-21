---
description: Run the full agent workflow for a single Jira issue, from brief to PR.
argument-hint: <ISSUE-KEY>
---

Run the complete workflow for issue **$1** end to end, using the Task/subagent tool to delegate
each step to the matching agent and waiting for each to finish before starting the next. Stop and
report to the user if any step fails or raises a blocking concern (e.g. code-reviewer/
security-reviewer report a Blocker/Critical finding) instead of proceeding.

1. **jira-story-reader** — fetch issue `$1` and write `.tasks/$1.md`.
2. Read the resulting `.tasks/$1.md` to check the **Type** field:
   - **Bug** → skip straight to step 4 (`bug-fixer`), do not run solution-architect.
   - **Story** → continue to step 3.
3. **solution-architect** — read `.tasks/$1.md` and append a `## Design` section.
4. Implementation:
   - **Bug** → **bug-fixer** reproduces, fixes, and adds a regression test.
   - **Story** → run **java-backend-dev** and/or **react-frontend-dev**, whichever the Design's
     "Affected Modules" section calls for (both, if the change spans backend and frontend).
5. **test-writer** — write tests from `.tasks/$1.md` Acceptance Criteria (skip if bug-fixer already
   added sufficient regression coverage and test-writer confirms no gaps remain).
6. **code-reviewer** — review the diff. If it reports any Blocker, stop and surface it to the user
   instead of continuing to security-reviewer.
7. **security-reviewer** — review the diff. If it reports any Critical/High finding, stop and
   surface it to the user instead of continuing to git-pr-agent.
8. **git-pr-agent** — create the branch, commit, and open the PR.

At the end, summarize: issue key, branch name, PR URL, and a one-line note on what code-reviewer
and security-reviewer found (or "no findings").
