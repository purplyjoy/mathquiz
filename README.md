MathQuiz is a Java-based quiz application that allows users to test and improve their mathematical knowledge through interactive quizzes. This project was developed as a programming project to practice Java programming and application development.

Features
- Interactive mathematics quizzes
- User-friendly interface
- Instant feedback on quiz answers
- Score summary after the quiz
- History of quizzes with the total score and level of difficulty

Built With
Java - main programming language
NetBeans IDE - original development environment
Visual Studio Code - development environment used after transferring the project from NetBeans
Java Development Kit (JDK) - for compiling and running java programs

Note: This project was originally developed in NetBeans as part of the course requirements and was later transferred to Visual Studio Code.

Requirements
Before running MathQuiz, make sure you have:
- A computer running Windows
- Java installed on your computer (JDK) recommended
- The MathQuiz project files

How to Run

Option 1: Run the Application Using the JAR File
1. Download or clone this repository
2. Make sure Java is installed in your computer
3. Open Command Prompt or terminal in the project folder
4. Run the following command, replacing MathQuiz.jar with the actual name of you JAR file:
    java -jar MathQuiz.jar

Option 2: Run Using the Batch File 
1. Download or clone this repository
2. Make sure Java is installed in your computer
3. Locate the .bat file in the project folder
4. Doluble-click the batch file to launch the application
If the application does not start, check that Java is installed and available from the command line.

Option 3: Run the Source Code
1. Install the JDK
2. Download or clone this repository
3. Open the project folder in Visual Studio Code
4. Install the Java extensions if prompted
5. Open the main java source file
6. Compile and run the program using the Java tools in VS Code, or use the terminal

Installation

Option 1: Clone the Repository Using Git
To clone the repository using Git, run:
git clone https://github.com/purplyjoy/mathquiz.git
Then enter the project directory:
cd mathquiz
Open the project folder in VS Code to view and run the source code.

Option 2: Download the Repository as a ZIP File
Visit the MathQuiz GitHub repository.
Click Code → Download ZIP.
Extract the downloaded ZIP file.
Open the extracted project folder in Visual Studio Code

Project Structure
mathquiz/
├── src/              # Java source files (questions (easy, medium, hard) and history file)
|   ├── easy          # Easy-level questions
|   ├── medium        # Medium-level questions
|   ├── hard          # Hard-level questions
|   ├── historyFile   # Quiz history data
├── .gitignore        # Files excluded from Git
├── NewJFrame.java    # Java source file
├── MathQuiz.bat      # Windows launcher
└── README.md         # Project documentation