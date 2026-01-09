# Covid Dashboard

A comprehensive dashboard for tracking and visualizing Covid-19 statistics.

## Tech Stack

**Frontend:**
- React
- Vite
- Tailwind CSS

**Backend:**
- Java
- Spring Boot
- MySQL
- OAuth2 (Google)

## Features
- Global and Country-wise statistics
- Interactive Charts
- User Authentication (Google OAuth2 & Email)
- Data Visualization

## Setup

### Prerequisites
- Node.js
- Java JDK 17+
- MySQL
- Maven

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/sujalpandey1605/covid-project.git
    cd covid-project
    ```

2.  **Frontend Setup**
    ```bash
    cd frontend
    npm install
    npm run dev
    ```

3.  **Backend Setup**
    - Configure your database in `backend/src/main/resources/application.properties`
    - Create `backend/src/main/resources/application-secret.properties` with your credentials:
      ```properties
      DB_PASSWORD=your_db_password
      MAIL_PASSWORD=your_mail_password
      GOOGLE_CLIENT_ID=your_client_id
      GOOGLE_CLIENT_SECRET=your_client_secret
      ```
    - Run the Spring Boot application.
