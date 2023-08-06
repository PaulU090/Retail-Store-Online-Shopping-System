# Retail-Store-Online-Shopping-System
Integrating store management, product tracking, and customer interactions using PostgreSQL, SQL, and Java. Offers a seamless interface for managers and customers with real-time inventory tracking, order history retrieval, and comprehensive admin tools for product management and report generation.

## Overview

This online shopping system was developed as part of the CS166 Database Management Systems course at the University of California, Riverside. The system is designed to track information about different stores of a retail shop, product availability, stock requests from managers, customer order history, delivery, and billing information. Built using PostgreSQL, SQL, and Java, the project aims to model and implement an Online Shopping System to cater to the needs of a retail business by providing functionalities related to store management, product tracking, and customer interactions.

## Installation and Setup

### 1. **Prerequisites**
- Ensure you have PostgreSQL, Java, and the JDBC (Java Database Connectivity) driver for PostgreSQL installed on your machine.

### 2. **Clone the Repository**
- Clone the repository to your local machine.

### 3. **JDBC Configuration**
- Set up the JDBC driver in your Java project to enable database connectivity.

### 4. **SQL Scripts Execution**
- Navigate to the `project/sql/src/` directory.
- Before executing scripts, ensure you update the file paths in `load_data.sql` to absolute paths to match the location of your data files.
- Execute the shell script `project/sql/scripts/create_db.sh` to set up the database. This script will automatically run the following SQL scripts in order:
  - `create_tables.sql`
  - `create_indexes.sql`
  - `load_data.sql`

### 5. **Java Interface**
- Navigate to the `project/java/scripts/` directory.
- Execute the script `compile.sh` to compile and run the Java interface which utilizes JDBC to connect to the database.
- Once the interface is running, you can start interacting with the system.

## Project Phases

### Phase 1: Requirement Analysis

In this phase, we analyzed the requirements using the Entity-Relationship (ER) model. The ER diagram was generated based on the project requirements. For more details, refer to the [project description document](project_instructions/CS_166__Project_Description.pdf).

### Phase 2: Relational Schema Design

This phase involved converting the ER diagram into an ANSI compliant relational model. The relational model was developed with required constraints like NOT NULL, UNIQUE, PRIMARY KEY, FOREIGN KEY REFERENCES, etc. For more details, refer to the [phase 2 document](project_instructions/CS_166__Project_Phase_2.pdf).

### Phase 3: Implementation

In the final phase, we implemented the system using SQL and Java. We were provided with a template Java interface to connect to the PostgreSQL database and execute SQL queries via JDBC. For more details, refer to the [phase 3 document](project_instructions/CS166_Project_Phase_3.pdf).

## Usage

### 1. **Configuration & Initialization**
- Ensure your JDBC driver is correctly configured and the database server is running.
- Launch the Java application via the terminal. Upon start-up, the program will establish a connection to the PostgreSQL database via JDBC.

### 2. Account Creation
- If you're a first-time user, the terminal will prompt you with an option to create an account.
- Enter the designated command for "Create Account".
- Follow terminal prompts to provide the required personal details, such as name, email, and a secure password.
- Once your account is set up, you'll receive a confirmation in the terminal, and you can proceed to log in with your credentials.

### 3. **Main Menu**
- Once connected and logged in, you will be presented with a text-based menu of options within the terminal. Each option corresponds to a specific operation or query.
- Navigate by typing the number or key associated with your desired operation and pressing `Enter`.

### 4. **Common Operations**:
   - **View Products**: To list all products available, type the designated number or command. The terminal will display product details like name, price, and stock.
   - **Search Products**: Type the corresponding number or command and then input keywords or product IDs to find specific products.
   - **Check Store Inventory**: Choose the option to view a store's inventory. You'll be prompted to select a store, after which the terminal will display products available at that location.
   - **Customer Order History**: Type the associated command and then input a customer ID or name to fetch their past orders.
   - **Stock Requests**: Store managers can request stock replenishments. After selecting this option, input the required product IDs and quantities.
   - **Billing & Delivery Information**: Access or modify billing and delivery info for orders by selecting this operation and following the prompts.

### 5. **Advanced Operations** (primarily for managers or admins):
   - **Add New Products**: After choosing this option, input the necessary details to add a new product to the database.
   - **Update Product Details**: Select this option and follow the prompts to modify existing products.
   - **View Stock Requests**: Higher-level managers can review all stock requests and process them by selecting this operation.
   - **Generate Reports**: Generate specific reports by selecting this option and specifying the desired time frame.

### 6. **Exiting the Application**:
   - To safely close the connection to the database and exit the application, type the designated "Exit" or "Quit" command.

### 7. **Error Handling**:
   - If you provide invalid inputs or encounter database issues, the system will display error messages within the terminal. Follow the provided guidance and ensure that data inputs match the expected format and range.
