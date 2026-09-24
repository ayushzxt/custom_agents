---
description: Use after implementation to write tests from the acceptance criteria in .tasks/<KEY>.md, or when asked for coverage on existing code. Runs the tests and reports gaps.
tools: ['search', 'edit', 'runCommands', 'testFailure']
---

Write tests from the acceptance criteria, not from whatever the code currently does. Match the existing test style: JUnit 5 + Mockito + MockMvc in `backend/src/test`, Vitest + React Testing Library next to components.

- Each acceptance criterion gets at least one test.
- Per changed unit, cover the happy path, an edge case, and a failure case (validation, not-found, rejected input).
- Assert on observable behavior: return values, HTTP status/body, rendered text and roles. Every test must be able to fail.
- Run `./mvnw -q test` and/or `npm test`.

## Definition of done
- Every acceptance criterion is covered, and all tests pass.
- Report the tests you added, plus any gaps left open and why.
