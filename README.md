# Recipe Manager

A full-stack recipe management system built without fluff. Small, but charming!

## Tech Stack

- Backend Engine: Spring Boot v4.0.6 (Java 17)
- Data Persistence: PostgreSQL + Spring Data JPA/Hibernate
- Migration Control: Flyway
- Documentation: OpenAPI 3 / Swagger UI

## Key Design Choices

- **Recipe** entity as an aggregate root, where the ingredients and instruction steps are bound as child entities of the master recipe, in a one-to-many relationship.
- The call architecture is orchestrated all through the **RecipeRepository**
- Decided to implement interface reflections with the search results, only fetching the ID, Name, and Description for each recipe item queried by any of the filtering mechanisms.
- Utilized DTOs such as _RecipeRequest_ for contracts, decoupling, and explicit privacy.
- Dynamic query using Specifications under the JpaSpecificationExecutor to enable stacking multipe filters without complicated query chaining.

## Get Started! (Local Dev)

### Pre-requisites

- Java 17 SDK
- PostgreSQL 14
- DBeaver (if you want to view the DB directly)