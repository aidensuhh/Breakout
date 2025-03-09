# Breakout

A simple JavaFX-based implementation of the classic Breakout game.

## How to Run the Game

To run the game, you need **Java 11 or later** and **JavaFX** set up on your machine.

### Requirements
- **Java 11 or later**
- **JavaFX SDK** (compatible with your Java version)

### Steps to Set Up and Run the Game

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/Breakout.git
   cd Breakout

2. **Set up JavaFX (for Java 11 or later)**:
   1. Download the JavaFX SDK from OpenJFX
   2. Extract the SDK and note the path to the lib folder (e.g., /path/to/javafx-sdk-XX/lib)
   3. Add the JavaFX library to your IDE:
      - In IntelliJ IDEA:
         - Go to File > Project Structure > Libraries
         - Click + and select "Java" to add the lib folder from your JavaFX SDK

3. **Open the project in IntelliJ IDEA**:
   1. Launch IntelliJ IDEA
   2. Select File > Open
   3. Navigate to the project folder (Breakout) and open it

4. **Configure VM Options for Running the Game**:
   1. Go to Run > Edit Configurations
   2. Click + and select "Application"
   3. Set the following:
      - **Main Class:** Game
      - **VM Options:**
        ```bash
        --module-path /path/to/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml
        ```
      - Replace /path/to/javafx-sdk-XX/lib with the actual path to your JavaFX SDK's lib folder
        
5. **Run the Game**:
   - Click the "Run" button in IntelliJ IDEA to start the game.

6. **Play the Game !**
   - Use the left and right arrow keys to move the paddle
   - Press Enter to restart or quit the game
   - Enjoy!

