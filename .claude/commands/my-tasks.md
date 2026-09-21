---
description: List your open Jira stories/bugs and write an implementation brief for each.
---

Use the **jira-story-reader** agent with no issue key override, so it runs its default JQL:

```
assignee = currentUser() AND issuetype in (Story, Bug) AND statusCategory != Done ORDER BY priority DESC
```

After it finishes, present the results as a table: Key | Type | Priority | Summary | Brief path
(`.tasks/<KEY>.md`). If no issues were found, say so plainly instead of showing an empty table.

Do not proceed to solution-architect, bug-fixer, or any implementation agent — this command only
gathers and briefs. To act on a specific issue, the user runs `/start-task <KEY>` next.
