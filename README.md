## 🏥 Patient Management System – Microservices Architecture
The Patient Management System is a production-ready healthcare application built with a microservices architecture using Java Spring Boot and AWS. It manages patient records, billing, analytics, and authentication while demonstrating modern software engineering and DevOps practices for scalable, cloud-native applications.

## 🔍 Key Features
  * Microservices-based design for modularity, scalability, and maintainability.
  * Patient, Billing, Analytics, and Auth Services communicating via gRPC and Apache Kafka for event-driven data flow.
  * Secure authentication using Spring Security and JWT.
  * API Gateway integration for centralized routing, access control, and documentation via OpenAPI.
  * Containerized deployment with Docker and automated provisioning on AWS ECS using CloudFormation (IaC).
  * Local AWS environment testing using LocalStack for smooth development and deployment validation.

## 🧩 Microservices Repositories
This project follows a microservices architecture, where each service is maintained in its own repository:
| Service                  | Description                                                                  | Repository Link                                                         |
| ------------------------ | ---------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| 🏥 **Patient Service**   | Manages patient data, CRUD operations, and interactions with other services. | [Patient Service](https://github.com/Kavindu-Manimendra/patient-service-api)     |
| 💳 **Billing Service**   | Handles patient billing, payments, and cost tracking via gRPC communication. | [Billing Service](https://github.com/Kavindu-Manimendra/pm-billing-service)     |
| 📊 **Analytics Service** | Consumes Kafka events to generate insights and reports.                      | [Analytics Service](https://github.com/Kavindu-Manimendra/pm-analytics-service) |
| 🔐 **Auth Service**      | Manages user authentication, JWT generation, and token validation.           | [Auth Service](https://github.com/Kavindu-Manimendra/pm-auth-service)           |
| 🌐 **API Gateway**       | Routes and secures external API requests to individual microservices.        | [API Gateway](https://github.com/Kavindu-Manimendra/pm-api-gateway)             |


## ⚙️ Tech Stack
  * Backend: Java, Spring Boot, Spring Security, gRPC, Apache Kafka
  * Containerization & Deployment: Docker, AWS ECS, CloudFormation, LocalStack
  * Database: PostgreSQL (RDS)
  * API Gateway & Communication: Spring Cloud Gateway, REST, gRPC
  * Testing: JUnit, Integration Tests
