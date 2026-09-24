---
mode: agent
description: 'Run the full agent pipeline for one Jira issue, from brief to PR.'
---

Run the pipeline in `copilot-instructions.md` for `${input:key:Jira issue key}`, one agent at a time.

- Route on the brief's **Type**: Story → solution-architect, then the dev agent(s) its Design names. Bug → bug-fixer.
- Stop and report on any failure, a code-reviewer Blocker, or a security-reviewer Critical/High.

Finish with the issue key, branch, PR URL, and one line on the review findings.
