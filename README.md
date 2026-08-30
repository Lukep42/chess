# Chess - JavaFX Application

A fully interactive chess application built in Java with JavaFX, implementing piece movement, captures, special moves, check/checkmate detection, draw conditions, and a GUI.

The project was designed with an object-oriented architecture that separates piece behaviour, board state, game logic and graphical presentation.

### Tech Stack
* Java
* JavaFX
* Gradle
* JUnit

### Project Structure
The application is divided into several components:
* Piece classes: Define the movement and collision behaviour of each piece.
* ChessBoard: Maintains the board state and handles board-level operations such as movement, captures and attacks.
* ChessGame: Manages game state, turns, move validation, check/checkmate/stalemate, en passant and draw conditions.
* ChessBoardGUI: Handles the JavaFX GUI.

### Requirements
1. Java Development Kit (JDK) 21 or newer

### How to Run 
1. Clone repository
2. Running the Application   
   <details>
     <summary><b> Windows</b></summary>
     2.1 Open command prompt. <br>
     2.2 Navigate to project location. <br>
     2.3 Type
      
         gradlew.bat run
   </details>
   
   <details>
     <summary><b> Mac/Linux</b></summary>
     2.1 Open terminal <br>
     2.2 Navigate to project location <br>
     2.3 Give execution permission by typing <br>
      
            chmod +x gradlew
     2.4 Type
   
            ./gradlew run
   </details>
3. Running Tests
   <details>
     <summary><b> Windows</b></summary>
     3.1 Open command prompt. <br>
     3.2 Navigate to project location. <br>
     3.3 Type:
      
            gradlew.bat test
   </details>
   
   <details>
     <summary><b> Mac/Linux</b></summary>
     3.1 Open terminal <br>
     3.2 Navigate to project location <br>
     3.3 Give execution permission by typing: 
             
            chmod +x gradlew
      <i>This step can be skipped if it was done prior.</i> <br>
     3.4 Type:
   
            ./gradlew test
   </details>

   
