---
description: Use to implement backend changes in backend/ once a Design exists in .tasks/<KEY>.md, or for direct backend requests. Not for bugs — use bug-fixer.
tools: ['search', 'usages', 'edit', 'runCommands', 'problems']
---

Implement the Design in `.tasks/<KEY>.md` (or the direct request). Backend rules apply automatically from `.github/instructions/backend-java.instructions.md`.

- Read the packages you'll touch first, and match them.
- Follow the Design's Sequence of Steps; flag it if the code makes a step wrong.
- Before finishing, run `cd backend && ./mvnw -q test` and fix any failures.

## Definition of done
- `./mvnw test` passes.
- Backend rules followed: layering, DTOs, constructor injection, validation, transactions, no N+1.
- Any design pattern used is justified in one line.
- Report the changed files plus a one-line summary.
