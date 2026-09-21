---
mode: agent
description: 'List your open Jira stories/bugs and write an implementation brief for each.'
---

Invoke the **jira-story-reader** agent with no issue key override, so it runs its default query:
issues assigned to the current user, type Story or Bug, not done, ordered by priority.

After it finishes, present the results as a table: Key | Type | Priority | Summary | Brief path
(`.tasks/<KEY>.md`). If nothing was found, say so plainly instead of showing an empty table.

Do not invoke solution-architect, bug-fixer, or any implementation agent — this prompt only
gathers and briefs. To act on a specific issue, run `/start-task <KEY>` next.
