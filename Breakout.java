// Aiden Suh
// 2023-10-18
// This Program Lets The User Play The Classic Arcade Video Game: Breakout
  
// Import Necessary JavaFX & IO Libraries
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.Random; // Random Class From Java Standard Library; Used To Create Random Object

// The Breakout Class Extends Application Class; The Main Entry Point For JavaFX Applications
public class Breakout extends Application
{
    // Declaration Of Instance Variables For Game Properties
    private Stage primaryStage; // Primary Window Of JavaFX Application
    private static final int BLOCK_SIZE = 30; // Block Size
    private static final int BLOCKS_PER_ROW = 20; // # Of Blocks In A Row
    private static final int ROWS = 5; // 5 Rows In Total
    private static final int PADDLE_WIDTH = 80; // Paddle Size: Width
    private static final int PADDLE_HEIGHT = 10; // Paddle Size: Height
    private static final int BALL_SIZE = 10; // Ball Size
    private static final int SCREEN_WIDTH = 600; // Window Size: 600 x 600
    private static final int SCREEN_HEIGHT = 600;
    private Rectangle paddle; // Game Element: Paddle
    private Rectangle ball; // Game Element: Ball
    private double dx = 2, dy = 2; // Movement Speed Variables
    private int score = 0; // Score Counter
    private int highestScore = 0; // Highest Score Counter
    private Text scoreText; // Score Displayer
    private Text highestScoreText; // Highest Score Displayer
    private Text endText; // Ending Text Displayer
    private Timeline animation; // Timeline Object For Animation
    private static final String HIGHEST_SCORE_FILE = "highestScore.txt"; // Text File Storing Highest Recorded Score

    // The "Start" Method Gets Called When The Application Is Launched
    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage; // Initialize Game Window
        primaryStage.setTitle("BREAKOUT"); // Window Title
        Pane pane = new Pane(); // Pane Object That Holds Game Elements
        setupGame(pane, primaryStage); // Sets Up The Game By Calling setupGame Method
        
        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT); 
        scene.setOnKeyPressed(e -> { // Event Listener For Keyboard Input

            // Move The Paddle Left When Left Arrow Key Is Pressed
            if (e.getCode() == KeyCode.LEFT && paddle.getX() > 0) 
            {
                paddle.setX(paddle.getX() - 10);
            }

            // Move The Paddle Right When Right Arrow Key Is Pressed
            else if (e.getCode() == KeyCode.RIGHT && paddle.getX() < SCREEN_WIDTH - paddle.getWidth()) {
                paddle.setX(paddle.getX() + 10);
            }

            // Handles Game Over Screen & Button Clicks(Play Again Or Quit)
            else if (e.getCode() == KeyCode.ENTER && endText != null) 
            {
                Button playAgainButton = new Button("Play Again"); // Creates Play Again Button 
                Button quitButton = new Button("Quit"); // Creates Quit Button

                // Button Placements
                playAgainButton.setLayoutX(SCREEN_WIDTH / 2 - 100);
                playAgainButton.setLayoutY(SCREEN_HEIGHT / 2);
                quitButton.setLayoutX(SCREEN_WIDTH / 2 + 20);
                quitButton.setLayoutY(SCREEN_HEIGHT / 2);

                // When Play Again Button Is Pressed, Close Current Window, And Start New Window
                playAgainButton.setOnAction(event -> {
                    primaryStage.close();
                    start(new Stage());
                });

                quitButton.setOnAction(event -> primaryStage.close()); // When Quit Button Is Pressed, Close Window
                pane.getChildren().addAll(playAgainButton, quitButton);
            }
        });

        // Displays Window
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // setUpGame Method: Sets Up The Initial Game State
    private void setupGame(Pane pane, Stage primaryStage) 
    {
        pane.getChildren().clear(); // Resets Game State When Play Again Button Is Pressed, Before Starting A New Game

        // Create Score Block Elements
        Color[] colors = {Color.RED, Color.ORANGE, Color.DARKGREY, Color.GREEN, Color.BLUE}; // Array To Hold Block Color Values
        for (int j = 0; j < ROWS; j++) // Loop Until 5 Rows Of Block Lines Have Been Created
        {
            for (int i = 0; i < BLOCKS_PER_ROW; i++) // Loop Until 20 Blocks Have Been Created For A Row
            {
                Rectangle block = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                block.setFill(colors[j % colors.length]); // Different Block Colors For Different Rows
                block.setStroke(Color.BLACK); // Black Outlines For Each Block
                block.setX(i * BLOCK_SIZE);
                block.setY(j * BLOCK_SIZE);
                pane.getChildren().add(block);
            }
        }

        // Create A Paddle Element
        paddle = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFill(Color.BLACK);
        paddle.setY(SCREEN_HEIGHT - PADDLE_HEIGHT * 2);
        paddle.setX((SCREEN_WIDTH - PADDLE_WIDTH) / 2);
        pane.getChildren().add(paddle);

        // Create A Ball Element
        ball = new Rectangle(BALL_SIZE, BALL_SIZE);
        ball.setFill(Color.BLACK);
        ball.setY(SCREEN_HEIGHT / 2);
        ball.setX(new Random().nextInt(SCREEN_WIDTH - BALL_SIZE)); // Starting Location Of Ball Is Random In x-axis
        pane.getChildren().add(ball);

        score = 0; // Initial Score: 0
        highestScore = getHighestScore(); // Call getHighestScore Method To Retrieve Highest Recorded Score

        // Default Movement Speed
        dx = 2;
        dy = 2;

        // Create A "Start" Button For The Start Of The Game
        Button startButton = new Button("Start"); // Button Object
        startButton.setLayoutX((SCREEN_WIDTH / 2) - 24); // Button Placement
        startButton.setLayoutY(SCREEN_HEIGHT / 2);
        startButton.setOnAction(event -> { 
            pane.getChildren().remove(startButton); // Remove Start Button When It Gets Pressed
            
            // Sets Up Ball Animation Using moveBall Method
            animation = new Timeline(new KeyFrame(Duration.millis(10), e -> moveBall(pane))); 
            animation.setCycleCount(Timeline.INDEFINITE); // Animation Repeats Until It's Explicitly Stopped
            animation.play(); // Starts Ball Animation

            // Displays Current Score When Game Is Being Played
            scoreText = new Text("Score: " + score);
            scoreText.setFont(new Font(20));
            scoreText.setX(SCREEN_WIDTH - scoreText.getLayoutBounds().getWidth() - 520);
            scoreText.setY(SCREEN_HEIGHT - 10);
            pane.getChildren().add(scoreText);
        });
        pane.getChildren().add(startButton);
    }

    // moveBall Method: Move The Ball & Handle Collisions
    private void moveBall(Pane pane) 
    {
        if (animation != null && animation.getStatus() == Timeline.Status.RUNNING) // Ball Only Moves When Animation Is Currently Running
        {
            // Updates The Position Of Ball 
            ball.setX(ball.getX() + dx);
            ball.setY(ball.getY() + dy);

            // Ball Bounces Off When It Hits Left & Right Wall
            if (ball.getX() < 0 || ball.getX() > SCREEN_WIDTH - BALL_SIZE) 
            {
                dx *= -1;
            }

            // Ball Bounces Off When It Hits Top Wall
            if (ball.getY() < 0) 
            {
                dy *= -1;
            }

            // Game Ends When Ball Hits The Bottom Wall
            if (ball.getY() > SCREEN_HEIGHT - BALL_SIZE) 
            {
                endGame(pane, "GAME OVER!");
            }

            // Ball Bounces Off When It Hits The Paddle
            if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) 
            {
                dy *= -1;
            }

            // Ball Collides With Blocks
            for (var node : pane.getChildren()) // Iterates Through Each Item In The Pane(Container)
            {
                if (node instanceof Rectangle && node != paddle && node != ball) // The Item Detected Is A Block And Not Paddle Or Ball
                {
                    if (ball.getBoundsInParent().intersects(node.getBoundsInParent())) // A Ball Collided With The Block
                    {
                        // When Ball Hits The Block, Block Gets Destroyed, And Ball Changes To Its Color
                        Color blockColor = (Color) ((Rectangle) node).getFill();
                        ball.setFill(blockColor);
                        pane.getChildren().remove(node);

                        dy *= -1; // Ball Bounce Off When Collision Happens

                        // Speed Of Ball Increase By 3% For Each Destroyed Block
                        dx *= 1.03; 
                        dy *= 1.03;

                        // Score Increase For Each Destroyed Block
                        score++; 
                        scoreText.setText("Score: " + score);

                        // Increase Paddle Size For Every 5 Points
                        if (score % 5 == 0 && paddle.getWidth() < SCREEN_WIDTH / 3) 
                        {
                            paddle.setWidth(paddle.getWidth() + 10);
                        }

                        // Game Ends When All Blocks Have Been Destroyed
                        if (pane.getChildren().stream().noneMatch(node2 -> node2 instanceof Rectangle && node2 != paddle && node2 != ball)) 
                        {
                            endGame(pane, "CONGRATULATIONS!");
                        }

                        break; // Breaks Out Of The Loop Once Collision Is Detected & Handled
                    }
                }
            }
        }
    }

    // endGame Method: Handles The End Of The Game When Player Lose & Win The Game
    private void endGame(Pane pane, String message) 
    {
        animation.stop(); // When Method Gets Called, Stops Ball Animation
        Button playAgainButton = new Button("Play Again"); // Creates "Play Again" Button
        Button quitButton = new Button("Quit"); // Creates "Quit" Button

        // Display The Buttons
        playAgainButton.setLayoutX(SCREEN_WIDTH / 2 - 37);
        playAgainButton.setLayoutY(SCREEN_HEIGHT / 2);
        quitButton.setLayoutX(SCREEN_WIDTH / 2 - 20);
        quitButton.setLayoutY(SCREEN_HEIGHT / 1.8);

        // When Play Again Button Is Pressed, Close Current Window And Start New One
        playAgainButton.setOnAction(event -> {
            primaryStage.close();
            start(new Stage());
        });

        quitButton.setOnAction(e -> primaryStage.close()); // When Quit Button Is Pressed, Close The Game Window
        pane.getChildren().addAll(playAgainButton, quitButton);

        // End Text Displayer: Changes Based On How Game Ends; If Ball Hits The Ground, Player Loses, And If All Blocks Are Destroyed, Player Wins
        endText = new Text(message);
        endText.setFont(new Font(40));
        endText.setX((SCREEN_WIDTH - endText.getLayoutBounds().getWidth()) / 2); 
        endText.setY(SCREEN_HEIGHT / 2 - 45);
        pane.getChildren().add(endText);

        // Update Highest Score If Possible, And Displays It
        if (score > highestScore) 
        {
            highestScore = score;
            saveHighestScore();
        }
        highestScoreText = new Text("Highest Score: " + highestScore);
        highestScoreText.setFont(new Font(20));
        highestScoreText.setX(SCREEN_WIDTH - highestScoreText.getLayoutBounds().getWidth() - 223);
        highestScoreText.setY(endText.getY() + 30);
        pane.getChildren().add(highestScoreText);
    }

    // getHighestScore Method: Retrieve Highest Recorded Score From A Text File
    private int getHighestScore() 
    {
        try {
            File file = new File(HIGHEST_SCORE_FILE); // File Object For Text File

            if (!file.exists()) // If The Text File Doesn't Exist, No Highest Score Is Stored Yet, So Return 0
            {
                return 0;
            }

            // Buffered Reader Object That Reads The File Contents
            BufferedReader reader = new BufferedReader(new FileReader(file)); 
            String line = reader.readLine(); // String Value Of Highest Score In The File
            reader.close();

            return Integer.parseInt(line); // Converts String To An Integer, And Returns Highest Score

        } catch (IOException e) { // If IOException Occurs, Print The Exception Stack Trace To Console To Help With Debugging
            e.printStackTrace();
        }
        return 0; // If Any Exception Occurs, Default Value Of 0 Gets Returned
    }

    // saveHighestScore Method: Save The Highest Score To A Text File If Achieved
    private void saveHighestScore() 
    {
        try {
            FileWriter writer = new FileWriter(HIGHEST_SCORE_FILE); // File Writer Object To Update The File Content
            writer.write(Integer.toString(highestScore)); // Convert Integer Value Of Highest Score To String, And Writes On The File
            writer.close();

        } catch (IOException e) { // If IOException Occurs, Print The Exception Stack Trace To Console To Help With Debugging
            e.printStackTrace();
        }
    }

    // Main Method: Entry point For The Java aApplication; Launches The JavaFX Application By Calling Launch Method
    public static void main(String[] args) 
    {
        launch(args);
    }
}