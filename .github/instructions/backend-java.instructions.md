---
applyTo: "backend/**"
---

- Layering: controller → service interface (`service/`) + impl (`service/impl/`) → Spring Data repository. Controllers never touch repositories. No logic in repositories.
- DTOs are records (`dto/`). Entities never leave the service layer.
- Constructor injection only.
- Entities expose behavior methods (e.g. `Task.changeStatus`), not setters called from outside.
- Validation: `jakarta.validation` on request DTOs + `@Valid`. Let `GlobalExceptionHandler` map errors.
- Throw domain exceptions (e.g. `ResourceNotFoundException`) from services; register new ones in `GlobalExceptionHandler`.
- `@Transactional(readOnly = true)` on the service class; `@Transactional` on methods that write.
- No N+1 queries: use fetch joins or dedicated queries.
- Log with SLF4J where the outcome is known, not at every layer.
