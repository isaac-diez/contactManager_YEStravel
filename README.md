# **YEStravel Contact Manager**

A robust, secure, and intuitive CRM (Customer Relationship Management) solution designed for YEStravel to manage international client contacts. This application handles multi-language data (English and Chinese), secure authentication via JWT, and bulk data operations.

## 🚀 Features

- Secure Authentication: JWT-based authentication with cookie storage and custom security filters to prevent unauthorized access and back-button exploits.

- Contact Management: Full CRUD (Create, Read, Update, Delete) operations for managing contact profiles.

- Internationalization Support: Native support for Chinese characters and English names, ensuring data integrity across regions.

- Bulk CSV Import: High-performance asynchronous import system using Jackson Dataformat CSV to upload large contact lists instantly.

- Responsive UI: Built with Bootstrap 5 for a seamless experience across desktop and mobile devices.

- Pagination & Search: Optimized data viewing with server-side pagination to handle thousands of records efficiently.

## 🛠️ Tech Stack

- Backend: Java 21, Spring Boot 3.5.x, Spring Data JPA, Spring Security.

- Database: MySQL 8.0.

- Frontend: Thymeleaf, Bootstrap 5, Vanilla JavaScript.

- Libraries: Jackson (JSON/CSV), JJWT (JWT handling), Lombok.

- Build Tool: Maven.