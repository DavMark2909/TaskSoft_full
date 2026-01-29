TaskSoft is a full-stack, enterprise-grade task management solution designed to optimize workforce efficiency through a microservices-based architecture. It replaces outdated coordination methods with a secure, scalable platform for task lifecycle management and role-based collaboration.

🚀 Key Features

- Role-Based Access Control (RBAC): Dynamic UI rendering that distinguishes between "Standard" and "Manager" views based on JWT claims. 
- Centralized Security: Implements a custom OAuth2 Authorization Server to manage user identities and security protocols. 
- Dual-Association Task Logic: Sophisticated data modeling that separates task creators (assigners) from executors (assignees). 
- Collaborative Group Workflows: Support for many-to-many user-group memberships, allowing tasks to be assigned to functional teams (e.g., DevOps, Frontend).


🛠 Technical Architecture
The system is built as a distributed ecosystem comprising four distinct services to ensure independent scalability and maintainability.

| Service | Technology Stack | Primary Responsibility |
| :--- | :--- | :--- |
| **Gateway Service** | Java, Spring Cloud Gateway | [cite_start]Acts as the single entry point (Edge Service), handling routing, CORS, and the Token Proxy handshake[cite: 99, 100, 101, 104]. |
| **Authorization Server** | Java, Spring Security, MySQL | [cite_start]Functions as the Identity Provider, issuing signed JWTs and handling secure user registration[cite: 91, 92, 95, 137]. |
| **Task Management** | Java, Spring Data JPA, PostgreSQL | [cite_start]Encapsulates core business logic for task creation, status updates, and database persistence[cite: 107, 108, 110]. |
| **Frontend Client** | React.js | [cite_start]A Single Page Application (SPA) utilizing the Context API for state and hooks for non-blocking async logic[cite: 113, 114, 181, 183]. |

🔐 Security Implementation
- The project utilizes the Token Proxy Pattern (Gateway-mediated security). 
- Token Relay: The Gateway intercepts incoming session identifiers, retrieves the corresponding JWT, and propagates it to downstream services. 
- Defense-in-Depth: While the Gateway blocks unauthenticated traffic, the Task Management service acts as an OAuth2 Resource Server, validating every JWT signature locally.
