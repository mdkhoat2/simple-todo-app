# Todo App (Spring Boot + Maven)

A tiny but functional Spring Boot REST API designed for Software Testing demos: fast unit tests, code coverage with JaCoCo, and CI/CD via GitHub Actions. Small enough that builds finish quickly, but real enough to make meaningful pull requests.

## Stack

- Java 24, Spring Boot 3
- Maven
- JUnit 5, Spring Test (MockMvc)
- JaCoCo for code coverage
- GitHub Actions for CI (and optional CD to GHCR)

## Run locally

- Start the database:

```powershell
docker run --name todo-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:16
```

- Start the app:

```powershell
mvn "-Dspring-boot.run.jvmArguments=-Duser.timezone=Asia/Ho_Chi_Minh" spring-boot:run
```

- API endpoints (port 8080):
  - GET `/api/todos`
  - POST `/api/todos` with JSON `{ "title": "Task" }`
  - PUT `/api/todos/{id}/toggle`
  - DELETE `/api/todos/{id}`

Quick test with PowerShell:

```powershell
# Create
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/todos -ContentType 'application/json' -Body '{"title":"Write tests"}'
# List
Invoke-RestMethod http://localhost:8080/api/todos
```

## Build, test, and coverage

Run full build with tests and coverage:

```powershell
mvn -q verify
```

Open the HTML coverage report at `target/site/jacoco/index.html`.

The build enforces a minimal line coverage threshold (70%). You can tweak this in `pom.xml` under the JaCoCo plugin.

## CI (Continuous Integration)

A workflow at `.github/workflows/ci.yml` runs on every push and pull request:
- Builds the project (Java 24)
- Runs tests and JaCoCo
- Uploads the HTML coverage report and test reports as artifacts

This keeps PRs meaningful: add or change behavior, adjust tests, and see coverage right in CI.

## CD (Continuous Deployment)

Included artifacts:
- `Dockerfile` (multi-stage) builds a minimal runtime image
- `.github/workflows/cd.yml` builds and pushes an image to GitHub Container Registry (GHCR) on pushes to `main`

To enable CD:
1. Ensure your repo is on GitHub (public or private).
2. Use the default `GITHUB_TOKEN` (workflow already requests `packages: write`).
3. Optionally set your repo visibility for GHCR packages in Settings > Packages.
4. After merge to `main`, the action builds, then pushes `ghcr.io/<owner>/<repo>:latest` and `:sha`.

Run the container locally (optional):

```powershell
# After a local build
docker build -t todo-app .
docker run -p 8080:8080 todo-app
```

## Project structure

- `src/main/java/com/example/todo` — application and API
- `src/test/java/...` — unit and web layer tests
- `pom.xml` — dependencies and JaCoCo gate
- `.github/workflows/` — CI and optional CD

## Meaningful PR ideas

- Add an endpoint to rename a Todo (PATCH `/api/todos/{id}`)
- Add validation for max title length and corresponding tests
- Add an integration test that goes through controller -> service -> repo
- Add sorting options (`?sort=createdAt,desc`) and tests
- Implement persistence behind a profile (e.g., H2) while keeping in-memory default
- Improve error responses with a standardized error body

Each of these adds real value and is small enough for a focused PR.

## Notes

- This repo avoids heavy dependencies to keep builds fast.
- If Maven isn’t installed locally, install Maven 3.9+ and JDK 24+, or use the GitHub Actions CI results.
