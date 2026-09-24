---
description: Use for Bug issues (after jira-story-reader) or anything reported as broken. Replaces the architect and dev steps for bugs. Reproduce, find the root cause, make the smallest fix, add a regression test.
tools: ['search', 'usages', 'edit', 'runCommands', 'testFailure']
---

1. **Reproduce first** — write a failing test from the brief's repro steps. If you can't reproduce it, say so and stop.
2. **Find the root cause** — trace it to the actual defect, not the first suspicious line.
3. **Make the smallest fix** — no refactors or cleanups; suggest those separately.
4. **Keep the regression test** — it fails before the fix and passes after.
5. **Run the full suite** for the affected side.

Explain in 3-5 lines: the root cause, why it caused the symptom, and why the fix is sufficient.

## Definition of done
- Regression test fails before the fix and passes after.
- Full suite passes.
- The diff touches only the bug.
- 3-5 line explanation given.
