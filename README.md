# Work Tracker Backend
A Spring Boot REST API for tracking work sessions, managing organizations, and user memberships.
## Tech Stack
- **Java 21** + **Spring Boot 3.3.5**
- **PostgreSQL** (via Docker Compose)
- **Spring Security** with JWT authentication
- **Flyway** for database migrations
- **Lombok** for boilerplate reduction
## Architecture
Controllers → UseCases → Services → Repositories
- **Controllers**: HTTP handling only, no business logic
- **UseCases**: Business rules & validations (1 action = 1 use case)
- **Services**: CRUD operations & simple calculations
- **Repositories**: Data access only
## Main Features
### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `POST /auth/select-organization` - Switch organization context
### Organizations
- `POST /organizations` - Create organization
- `GET /organizations/{id}` - Get organization with settings
- `POST /organizations/settings` - Create settings (first time)
- `PUT /organizations/{id}/settings` - Update settings
- `POST /organizations/invite-code` - Generate invitation code
### Memberships
- `GET /membership/my-organizations` - List user's organizations
- `POST /membership/join-organization` - Join via invitation code
### Places
- `POST /places` - Create work location
- `GET /places` - List organization places
### Work Sessions
- `POST /work-sessions/start` - Start session
- `POST /work-sessions/end` - End active session
- `POST /work-sessions/manual` - Create manual entry
- `GET /work-sessions` - List sessions with filters
## Quick Start
### 1. Configure Environment
```bash
cp .env.example .env
# Edit .env with your database credentials
2. Start Database
docker-compose up -d
3. Run Application
./mvnw spring-boot:run
API Response Format
All endpoints return a standardized response:
{
  "status": 200,
  "message": "Success",
  "data": { ... }
}
Project Structure
src/main/java/com/drmj/work_tracker/
├── config/          # Security & JPA config
├── controller/      # REST endpoints
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── repository/      # Data access
├── security/        # JWT & auth utilities
├── service/         # Business services
├── usecase/         # Business use cases
└── utils/           # Error messages & utilities
Entities
- User - System users
- Organization - Companies/teams
- OrganizationSettings - Config per organization
- UserOrganization - Membership with roles (ADMIN, EMPLOYER, EMPLOYEE)
- Place - Work locations
- WorkSession - Time tracking entries
- HourlyRate - Rates per place
- InvitationCode - Organization invitations
Soft Delete
All entities support soft delete via Hibernate annotations:
@SQLDelete(sql = "UPDATE table SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
Development Guidelines
See AGENTS.md (AGENTS.md) for detailed coding standards and architecture guidelines.