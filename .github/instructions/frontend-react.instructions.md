---
applyTo: "frontend/**"
---

- Functional components and hooks only. Strict TypeScript, no `any`.
- API calls go through the typed client in `src/api/`; shared types live in `src/types/`.
- Data-driven UI covers loading, empty, error (`role="alert"`), and success — see `TaskList.tsx`.
- Accessibility: semantic elements, keyboard operable, labelled inputs, ARIA where visual state needs it.
- Responsive: no fixed pixel widths.
- Reuse CSS custom properties (`--border-color`, `--error-color`) and existing components before adding new ones.
