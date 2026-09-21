# custom_agents

A small full-stack task app used as the base for an AI-assisted development workflow.

## Stack

- **Backend**: Java 21, Spring Boot 3.3 (Maven), layered `controller -> service -> repository`,
  DTO/entity separation, `@RestControllerAdvice` error handling, JUnit 5 + Mockito + MockMvc,
  Testcontainers for integration tests.
- **Frontend**: React 18 + TypeScript (Vite), functional components/hooks, Vitest + React
  Testing Library.

## Structure

```
backend/    Spring Boot API (Maven project)
frontend/   React + TypeScript app (Vite project)
```

## Running locally

```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend
cd frontend && npm install && npm run dev
```

## AI-assisted workflow

See `CLAUDE.md` (on the `claude/agent-team` branch) for the Claude Code subagent team, and
`.github/copilot-instructions.md` (on the `copilot/agent-team` branch) for the GitHub Copilot
equivalent.
