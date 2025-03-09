# Breakout Game

This is a Java-based **Breakout** game that you can play in your terminal using **JavaFX**. The game features a paddle, a bouncing ball, and blocks to destroy. The game ends when the ball hits the bottom of the screen.

## How to Run the Game

To run the game, you need **Java 11 or later** and **JavaFX** set up on your machine.

### Requirements
- **Java 11 or later**
- **JavaFX SDK** (for versions of Java 11 and above)

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/Breakout.git
   cd Breakout

2. **Set up JavaFX (for Java 11 or later)**:
- Download the JavaFX SDK from OpenJFX
- Follow the instructions below to add the JavaFX library in your IDE (IntelliJ IDEA)

3. **Open the project in IntelliJ IDEA**:
- Open IntelliJ IDEA and select Open Project
- Navigate to the project folder and select it

4. **Run the game**:
- If you're using Java 11 or later, make sure to configure the VM options in IntelliJ to include the path to the JavaFX SDK.
- To do so, go to Run > Edit Configurations > Add New Configuration > Application > Type "Game" for Main Class > Modify options > Add VM options, and in Program arguments,
  ```bash
  --module-path /path/to/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml
  ```
  Replace /path/to/javafx-sdk-XX/lib with the correct path

5. **Play the game**:
- Once the game window opens, use the left and right arrow keys to move the paddle
- Press Enter to restart or quit the game
