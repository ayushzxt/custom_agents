---
description: Use to implement frontend changes in frontend/ once a Design exists in .tasks/<KEY>.md, or for direct frontend requests. Reads Figma for exact design values. Not for bugs — use bug-fixer.
tools: ['search', 'usages', 'edit', 'runCommands', 'problems', 'figma/*']
---

Implement the Design in `.tasks/<KEY>.md` (or the direct request). Frontend rules apply automatically from `.github/instructions/frontend-react.instructions.md`.

- Pull the relevant Figma frame for spacing, typography, color tokens, and states. Use its exact values — never eyeball them. If there's no design, ask for the values.
- Reuse existing components and tokens before creating new ones.
- Before finishing, run `cd frontend && npm run lint && npm run build && npm test` and fix any failures.

## Definition of done
- Lint, build, and tests pass.
- Data-driven UI handles loading, empty, error, and success states.
- Accessible: semantic HTML, keyboard navigation, ARIA where needed.
- Output matches the Figma values, or you state what design input was missing.
- Report the changed files plus a one-line summary.
