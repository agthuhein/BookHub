# BookHub API - Online Book-Selling Application

This project is a backend-only application designed for an online book-selling platform. The system allows users to browse books, post books' reviews and place orders. The application is developed using **Spring Boot**, **MySQL**, and **MongoDB**. The backend exposes a **RESTful API** to interact with the system.

## Features

- **User Management**: Users can sign up, log in, and manage their profiles.
- **Book Management**: Users can view a list of books, including authors, publishers, categories and reviews.
- **Orders**: Users can place orders, and will receive email notification for order status.
- **Book Reviews**: Users can leave reviews for books they have purchased.
- **Database Design**:
    - **MySQL** is used for handling **Users**, **Books**, **Authors**, **Publishers**, **Categories**, and **Order**.
    - **MongoDB** is used to store **Book Reviews**.

## Technologies Used

- **Backend**: Java, Spring Boot, RESTful API
- **Database**:
    - **MySQL**: For relational data (users, books, orders, etc.)
    - **MongoDB**: For storing book reviews
- **Testing**: Postman for API testing
- **Build Tool**: Maven

## Database Schema

### MySQL:
- **Users**: Information about registered users.
- **Books**: Book details such as isbn, title, price, and stock.
- **Authors**: Authors that books belong to (e.g., J. K. Rowling)
- **Publishers**: Publishers that books belong to (e.g., The Penguin Publishing Group).
- **Categories**: Categories that books belong to (e.g., fiction, non-fiction).
- **Orders**: Information about customer orders.

### MongoDB:
- **Book Reviews**: User reviews for each book (stored separately in MongoDB for faster retrieval).

## Setup and Installation

### Prerequisites

- **JDK 21 or higher**
- **Spring Boot** framework
- **MySQL 9.2.0** and **MongoDB v8.0.5** installed and running

### Steps to Run the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/agthuhein/BookHub.git
   cd BookHub

2. **Set up the MySQL Database**:
- **Create the database and name it 'BookHub' database**
- **Go to Database Backup folder. Then get the MySQL Database backup file**
- **Open command line/terminal. Change directory to where the backup file exits**
- **Backup the BookHub_BK.sql using the command below**
```bash
  mysql -u root -p BookHub < BookHub_BK.sql
```
- **Configure the connection in application.yml file**

3. **Set up the MongoDB Database**:
- **Ensure MongoDB is running**
- **Create a new database and name it 'BookHub'**
- **Create 'Reviews' collection**
```bash
  use("BookHub");
  db.createCollection("reviews");
```
- **Configure the connection in application.yml file**

4. **Run the BookHub Application**

### Steps to Test the Application
**Install Postman API Client**:
- **Import 'BookHub API.postman_collection.json' into postman collection**
- **Extract the folders and then test the GET, POST, PUT, and DELETE requests**

