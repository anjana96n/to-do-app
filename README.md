# To-Do App

This is a To-Do application created with Spring Boot for the backend and Angular for the frontend, using PostgreSQL as the database.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [License](#license)

## Features
- Add, view, and delete tasks
- Mark tasks as completed
- Filter tasks by priority
- Responsive design

## Technologies Used
- **Backend**: Spring Boot
- **Frontend**: Angular
- **Database**: PostgreSQL
- **Containerization**: Docker and Docker Compose

## Prerequisites
Before you begin, ensure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Node.js](https://nodejs.org/) (for local development of the Angular app)
- [Maven](https://maven.apache.org/install.html) (for building the Spring Boot app)

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <https://github.com/anjana96n/to-do-app.git>
   cd <to-do-app>
   run docker-compose.yml
   ```

2. **Navigate to the Backend Directory**
   ```bash
   cd backend
   ```

3. **Build the Spring Boot Application**
   ```bash
   mvn clean package
   ```

4. **Navigate to the Frontend Directory**
   ```bash
   cd ../frontend
   ```

5. **Install Angular Dependencies**
   ```bash
   npm install
   ```

## Running the Application

1. **Navigate to the Project Root Directory**
   ```bash
   cd <project-root-directory>
   ```

2. **Start the Application Using Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the Application**
   - **Angular Frontend**: [http://localhost:4200](http://localhost:4200)
   - **Spring Boot Backend**: [http://localhost:8080](http://localhost:8080)
   - **PostgreSQL Database**: Accessible internally via Docker.

## API Endpoints

- **GET /api/tasks**: Retrieve all tasks
- **POST /api/tasks**: Create a new task
- **PUT /api/tasks/{id}/done**: Mark a task as completed

## Testing

To run the unit tests for the Spring Boot application, navigate to the backend directory and run:

```bash
mvn test
```

To run the Angular tests, navigate to the frontend directory and run:

```bash
ng test
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

