# Recipe Manager

A full-stack recipe management system built without fluff. Small, but charming!

## Tech Stack

- Backend Engine: Spring Boot v4.0.6 (Java 17)
- Frontend: React
- Data Persistence: PostgreSQL + Spring Data JPA/Hibernate
- Migration Control: Flyway
- Documentation: OpenAPI 3 / Swagger UI

## Key Design Choices

- **Recipe** entity as an aggregate root, where the ingredients and instruction steps are bound as child entities of the master recipe, in a one-to-many relationship.
- The call architecture is orchestrated all through the **RecipeRepository**
- Decided to implement interface reflections with the search results, only fetching the ID, Name, and Description for each recipe item queried by any of the filtering mechanisms.
- Utilized DTOs such as _RecipeRequest_ for contracts, decoupling, and explicit privacy.
- Dynamic query using Specifications under the JpaSpecificationExecutor to enable stacking multipe filters without complicated query chaining.
- Used React as the Frontend to demonstrate RESTful API instead of using cURL or an external tool like Postman for ease of use and better demonstration of Frontend/Backend connection.

## Get Started! (Local Dev)

### Pre-requisites

- Java 17 SDK
- PostgreSQL 14
- DBeaver (if you want to view the DB directly)

## Assumptions & Boundaries

- Unit testing was not completed for the sake of time and not present on the requirement list. If implemented, I would have used **JUnit** and **Mockito**, targeting the service layer, and covering the methods inside RecipeService.java.
- Frontend is not fully fleshed out since it is not the priority of the demonstration, but may be further developed in future versions.
- Chose to implement a dynamic tagging system to detect if recipes fall under criteria such as vegetarian, dairy, etc. for a smoother experience. Some tags may be inaccurate due to similar usages (e.g. 'bake' can be used for deserts and oven baked poultry).
- 