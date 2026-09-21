---
mode: agent
description: 'Run the full agent pipeline for a single Jira issue, from brief to PR.'
---

Run the complete pipeline for issue `${input:key:Jira issue key (e.g. PROJ-123)}` end to end,
invoking each agent below in order and waiting for each to finish before starting the next. Stop
and report to the user if any step fails or raises a blocking concern (code-reviewer or
security-reviewer reporting a Blocker/Critical finding) instead of proceeding.

1. **jira-story-reader** — fetch issue `${input:key}` and write `.tasks/${input:key}.md`.
2. Read the resulting brief's **Type** field:
   - **Bug** → skip to step 4 (`bug-fixer`), do not invoke solution-architect.
   - **Story** → continue to step 3.
3. **solution-architect** — read the brief and append a `## Design` section.
4. Implementation:
   - **Bug** → **bug-fixer** reproduces, fixes, and adds a regression test.
   - **Story** → **java-backend-dev** and/or **react-frontend-dev**, whichever the Design's
     "Affected Modules" section calls for (both, if the change spans backend and frontend).
5. **test-writer** — write tests from the brief's Acceptance Criteria (skip only if bug-fixer's
   regression coverage is already sufficient and test-writer confirms no gaps).
6. **code-reviewer** — review the diff. If it reports any Blocker, stop and surface it to the
   user instead of continuing to security-reviewer.
7. **security-reviewer** — review the diff. If it reports any Critical/High finding, stop and
   surface it to the user instead of continuing to git-pr-agent.
8. **git-pr-agent** — create the branch, commit, and open the PR.

At the end, summarize: issue key, branch name, PR URL, and a one-line note on what code-reviewer
and security-reviewer found (or "no findings").
