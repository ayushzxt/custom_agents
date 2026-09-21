---
applyTo: "backend/**"
---

# Backend conventions (Java / Spring Boot)

Applies automatically to any file under `backend/`, in addition to `copilot-instructions.md`.

- **Layering**: controllers depend on service interfaces, never repositories directly. Services
  are an interface (`service/`) + implementation (`service/impl/`). Repositories are Spring Data
  JPA interfaces only — no business logic in them. See `TaskController` / `TaskService` /
  `TaskServiceImpl` / `TaskRepository` for the reference shape.
- **DTOs are records** (`CreateTaskRequest`, `TaskResponse`) — entities (`@Entity`) never leave
  the service layer.
- **Constructor injection only** — no `@Autowired` field/setter injection.
- **Entities expose behavior methods** (e.g. `Task.changeStatus(...)`) rather than being purely
  anemic with public setters used from outside.
- **Validation**: `jakarta.validation` annotations on request DTOs + `@Valid` on controller
  parameters; let `GlobalExceptionHandler` turn `MethodArgumentNotValidException` into a 400.
- **Exceptions**: throw domain exceptions from the service layer (see `ResourceNotFoundException`);
  add new handlers to `GlobalExceptionHandler` for new exception types instead of try/catching in
  controllers.
- **Transactions**: `@Transactional(readOnly = true)` at the service class level for read paths,
  `@Transactional` on individual mutating methods.
- **No N+1 queries** — use fetch joins or dedicated query methods instead of triggering lazy loads
  in a loop.
- **Logging**: `LoggerFactory.getLogger(ThisClass.class)`, logged where the outcome is known
  (service/exception handler), not redundantly at every layer.
- **Design patterns** (Strategy, Factory, Builder, Adapter, Template Method, Observer, Decorator)
  only where the problem's shape calls for them — justify the choice in a comment when used.
- Before considering backend work done: `cd backend && ./mvnw -q compile && ./mvnw -q test` must
  pass.
