import com.example.pongretry.controllers.BallMovementHandler;
import com.example.pongretry.controllers.GameController;
import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;
import com.example.pongretry.model.Racket;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BallMovementHandlerTest {
    private Pane mockPane;
    private Ball mockBall;
    private Racket mockRacket1;
    private Racket mockRacket2;
    private Player mockPlayer1;
    private Player mockPlayer2;
    private  String name;
    private GameController mockGameController;

    private BallMovementHandler ballMovementHandler;

    @BeforeEach
    public void setUp() {
        mockPane = new Pane();
        mockBall = new Ball();
        mockRacket1 = new Racket(2);
        mockRacket2 = new Racket(2);
        name = "test";


        ballMovementHandler = new BallMovementHandler(mockPane, mockBall, mockRacket1, mockRacket2,
                mockPlayer1, mockPlayer2, 1.0, 1.0, mockGameController ,name); // Adjust the parameters as needed
    }

    @Test
    public void testHandleCollisionsRacket1() {
        // Assuming the ball intersects with Racket1
        mockRacket1.setX(15);
        mockRacket1.setY(10);
        mockRacket1.setWidth(20);
        mockRacket1.setHeight(20);
        mockBall.setCenterX(15);
        mockBall.setCenterY(15);

        double initialDx = ballMovementHandler.getDx();
        ballMovementHandler.handleCollisions();
        double newDx = ballMovementHandler.getDx();

        assertEquals((-initialDx), newDx);
    }
    @Test

    public void testHandleCollisionsWall() {
        mockBall.setCenterY(5);
        double initialDy = ballMovementHandler.getDy();
        ballMovementHandler.handleCollisions();
        double newDy = ballMovementHandler.getDy();
        assertEquals((-initialDy), newDy);


    }
    @Test

    public void testHandleCollisionsBottomWall() {
        double ballRadius = mockBall.getRadius();
        double bottomWallY = mockPane.getHeight() - ballRadius;
        mockBall.setCenterY(bottomWallY);
        double initialDy = ballMovementHandler.getDy();
        ballMovementHandler.handleCollisions();
        double newDy = ballMovementHandler.getDy();
        assertEquals((-initialDy), newDy);

    }

}
