---
applyTo: "frontend/**"
---

# Frontend conventions (React / TypeScript)

Applies automatically to any file under `frontend/`, in addition to `copilot-instructions.md`.

- **Functional components + hooks only.** No class components.
- **TypeScript strict** — type all props, API responses (`src/types/`), and non-trivial returns.
  Avoid `any`; comment why if it's truly unavoidable.
- **Cover loading/empty/error/success states** for anything that fetches or mutates data — see
  `TaskList.tsx` for the reference pattern (`tasks === null` → loading, `tasks.length === 0` →
  empty, `error` → `role="alert"`, else → content).
- **Accessibility (WCAG)**: semantic HTML over generic `div`/`span` where one fits, keyboard
  operability for every interactive element, `aria-label`/`aria-live`/`role="alert"` where the
  visual state needs an accessible equivalent, form inputs always paired with a `<label>`.
- **Responsive layout** — no hardcoded fixed pixel widths that break on narrow viewports.
- **Reuse existing CSS custom properties** (e.g. `--border-color`, `--error-color`) instead of
  hardcoding new values when an equivalent token exists.
- **Reuse before creating** — check `src/components/` for something close before writing a new
  component.
- Before considering frontend work done: `cd frontend && npm run lint && npm run build && npm test`
  must pass.
