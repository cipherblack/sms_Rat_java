# Java Project Setup and Usage Guide

## This guide provides instructions for setting up and running your Java project. Please follow the steps below carefully:

## Prerequisites
- Ensure that JDK 11 or newer is installed on your system.
- You should have an Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse installed.

Setup Instructions

1. **Download and Extract the Project**
   - Download the project ZIP file and extract it to a directory of your choice.

2. **Import the Project into Your IDE**
   - Open your preferred IDE (e.g., IntelliJ IDEA or Eclipse).
   - From the main menu, select File > Open and choose the extracted project folder.
   - Ensure that your project is recognized as either a Maven or Gradle project (you may need to import the pom.xml or build.gradle file if prompted).

3. **Configure Project Variables and Settings**
   - Open any configuration files (if available), such as application.properties or config.json.
   - Modify the required values to suit your environment, such as API keys, server addresses, or database credentials.


4. **Configuring the server section to receive messages**
   - First, put the index.php file in the php_server folder in its public_html folder and enter the file address, which is included with the server address and the folder name, in line 95 of the `app\src\main\java\com\example\myapplication\SmsService.java` file. you give
   - Make sure that the address of the file inside the host is entered correctly.

5. **Build the Project**
   - If you are using Maven, run the following command in your terminal or command prompt:
     ```bash
     mvn clean install
     ```
   - If you are using Gradle, run the following command:
   ```bash
     gradle build
   ```
6. **Running the Project**
   - Locate the main class of the project (usually the class containing the public static void main(String[] args) method).
   - Right-click on the main class and select the Run option to start the application.


- Dependency Issues: If you encounter errors related to missing dependencies, ensure that your system is connected to the internet so that Maven or Gradle can download the necessary dependencies.
- JDK Version Errors: If you face issues related to JDK version mismatches, double-check that the correct JDK version is configured in your IDE (e.g., JDK 11 or newer).
