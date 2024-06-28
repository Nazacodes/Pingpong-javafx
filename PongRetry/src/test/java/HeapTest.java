import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;

public class HeapTest {
    public static void main(String[] args) {
        // Test creation of game scenario with two players and one ball
        testPlayerArrayCreation();
        testBallArrayCreation();
    }

    private static void testPlayerArrayCreation() {
        System.out.println("Heap Size Test for Player Array Creation:");

        long startTime = System.currentTimeMillis();
        int count = 0;

        try {
            while (true) {
                Player[] players = new Player[100000]; // Array to store players

                // Populate the array with new Player objects
                for (int i = 0; i < players.length; i++) {
                    players[i] = new Player();
                }

                count++;
                System.out.println("Iteration: " + count); // Increment the count for each iteration
            }
        } catch (OutOfMemoryError error) {
            long endTime = System.currentTimeMillis();
            long elapsedTimeSeconds = (endTime - startTime) / 1000;
            System.out.println("Failed to create player array due to OutOfMemoryError");
            System.out.println("Time taken to throw OutOfMemoryError for player array creation (seconds): " + elapsedTimeSeconds);
            System.out.println("Number of iterations before running out of memory: " + count);
        }
    }

    private static void testBallArrayCreation() {
        System.out.println("Heap Size Test for Ball Array Creation:");

        long startTime = System.currentTimeMillis();
        int count = 0;

        try {
            while (true) {
                Ball[] balls = new Ball[100000]; // Array to store balls

                // Populate the array with new Ball objects
                for (int i = 0; i < balls.length; i++) {
                    balls[i] = new Ball();
                }

                count++;
                System.out.println("Iteration: " + count); // Increment the count for each iteration
            }
        } catch (OutOfMemoryError error) {
            long endTime = System.currentTimeMillis();
            long elapsedTimeSeconds = (endTime - startTime) / 1000;
            System.out.println("Failed to create ball array due to OutOfMemoryError");
            System.out.println("Time taken to throw OutOfMemoryError for ball array creation (seconds): " + elapsedTimeSeconds);
            System.out.println("Number of iterations before running out of memory: " + count);
        }
    }
}
