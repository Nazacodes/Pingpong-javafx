import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;

public class StackTest {
    public static void main(String[] args) {
        // Test creation of Player objects
        testPlayerCreation();

        // Test creation of Ball objects
        testBallCreation();
    }

    private static void testPlayerCreation() {
        System.out.println("Stack Size Test for Player Objects:");

        long startTime = System.currentTimeMillis();
        try {
            createPlayers(0);
        } catch (StackOverflowError e) {
            long endTime = System.currentTimeMillis();
            long elapsedTimeSeconds = (endTime - startTime) / 1000;
            System.out.println("Failed to create Player objects due to StackOverflowError");
            System.out.println("Time taken to throw StackOverflowError for Player object creation (seconds): " + elapsedTimeSeconds);
        }
    }

    private static void testBallCreation() {
        System.out.println("Stack Size Test for Ball Objects:");

        long startTime = System.currentTimeMillis();
        try {
            createBalls(0);
        } catch (StackOverflowError e) {
            long endTime = System.currentTimeMillis();
            long elapsedTimeSeconds = (endTime - startTime) / 1000;
            System.out.println("Failed to create Ball objects due to StackOverflowError");
            System.out.println("Time taken to throw StackOverflowError for Ball object creation (seconds): " + elapsedTimeSeconds);
        }
    }

    private static void createPlayers(int depth) {
        int arraySize = 10000000; // Define the size of the array
        Player[] players = new Player[arraySize];

        // Fill the array with Player objects
        for (int i = 0; i < arraySize; i++) {
            players[i] = new Player();
        }

        // Recursive call to create more Player objects
        createPlayers(depth + 1);
    }

    private static void createBalls(int depth) {
        int arraySize = 10000000; // Define the size of the array
        Ball[] balls = new Ball[arraySize];

        // Fill the array with Ball objects
        for (int i = 0; i < arraySize; i++) {
            balls[i] = new Ball();
        }

        // Recursive call to create more Ball objects
        createBalls(depth + 1);
    }
}
