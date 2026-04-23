# **YEStravel Contact Manager**

A robust, secure, and intuitive CRM (Customer Relationship Management) solution designed for YEStravel to manage international client contacts. This application handles multi-language data (English and Chinese), secure authentication via JWT, and bulk data operations.

## Features
- Secure Authentication: JWT-based authentication with cookie storage and custom security filters to prevent unauthorized access and back-button exploits.
- Contact Management: Full CRUD operations for detailed profiles, now including professional fields (Title, Company, and Position).
- Advanced Sorting & Search: Interactive table headers for dynamic multi-column sorting (ASC/DESC) combined with a real-time server-side search engine.
- Internationalization Support: Native support for Chinese characters and English names, ensuring data integrity across regions.
- Automated Reminders: Background scheduled jobs that send automated email notifications for upcoming birthdays with retry logic and asynchronous processing.
- Bulk CSV Import: High-performance asynchronous import system using Jackson Dataformat CSV to upload large contact lists instantly.
- Responsive UI: Optimized mobile experience with prioritized column visibility and unified spacing using Bootstrap 5.

## Tech Stack (Updated)
- Backend: Java 21, Spring Boot 3.5.x, Spring Data JPA, Spring Security (Stateless).
- Database: MySQL 8.0 / MariaDB (Optimized with utf8mb4_unicode_ci for cross-environment compatibility).
- Frontend: Thymeleaf, Bootstrap 5 (Floating labels & Dynamic Icons), Vanilla JavaScript.
- Libraries: Jackson (JSON/CSV), JJWT, Lombok, Spring Mail.
- Build Tool: Maven.

## Key Technical Enhancements
- Dynamic Pagination: Integration of Pageable and Sort objects across Controller, Service, and Repository layers to minimize boilerplate code.
- Resilient Emailing: Implementation of @Retryable and @Recover patterns to handle SMTP fluctuations during reminder dispatches.
- Security Fine-tuning: Stateless security architecture with specialized filters for JWT validation and public endpoint protection.
- Database Portability: Refactored SQL schemas and native queries to ensure seamless migration between local development and cloud production environments.