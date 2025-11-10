# 🧩 Meu Currículo

This is a project for management of resumes.

---

## 🚀 Technologies

- **Java 21**
- **Spring Boot 4.0.0**
- **PostgreSQL 18**
- **Docker & Docker Compose**
- **Maven** for dependency management

---

## ⚙️ Prerequisites

- [Java 21](https://www.oracle.com/br/java/technologies/downloads/#java21)

---

## 🧰 How to Use

Follow these steps to set up and run the backend locally:

1️⃣ Clone the repository

git clone https://github.com/your-username/meucurriculo.git

2️⃣ Create the `.env` file

In the root of your project, create a file named `.env` with the following content:

POSTGRES_USER=username-postgres
POSTGRES_PASSWORD=password-postgres
POSTGRES_DB=database-postgres

💡 This file stores your PostgreSQL credentials safely and keeps them out of version control (remember to add `.env` to your `.gitignore`).


3️⃣ Start the database container

docker-compose -p meucurriculo -f path-to-your-projects\meucurriculo\docker-compose.yml up -d  

💡 This command will start a PostgreSQL 18 container configured with the credentials defined in your `.env` file.

4️⃣ Set the environment variables for the application

Before running the project, configure the following variables in your IDE or system environment:

DATABASE_HOST=localhost  
DATABASE_PORT=5432  
DATABASE_NAME=database-postgres  
DATABASE_USER=username-postgres
DATABASE_PASSWORD=password-postgres 
APP_PROFILE=development  
PORT=8080  

⚙️ These values will ensure your Spring Boot app connects to the PostgreSQL container correctly.

5️⃣ Run the project in your IDE

- Open the project in **IntelliJ IDEA** or **VS Code**.  
- Wait for Maven to download all dependencies.  
- Run the main class (usually named `MeucurriculoApplication.java`).

---

## 🧾 Base Configuration (application.yml)

> The `application-development.yml` file currently remains empty for future custom profile configurations.

---

## 🧪 Testing the API

Once the application is running, access it at:

http://localhost:8080/actuator/health

If everything is ok, you will see a property named `status` with the value `up` in the response JSON.

---

## 🧱 Version

**v0.0.1** – First version of the backend.
