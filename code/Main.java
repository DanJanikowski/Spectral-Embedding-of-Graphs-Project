import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    public static int w = 800, h = 800;

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(bp);
		//Canvas
		GraphicsContext gLeft, gRight;
        Canvas canvasLeft = new Canvas(w, h);
		Canvas canvasRight = new Canvas(w, h);
		gLeft = canvasLeft.getGraphicsContext2D();
		gRight = canvasRight.getGraphicsContext2D();
		bp.setLeft(canvasLeft);
		bp.setRight(canvasRight);
		//Drawing class
		Spectral spectral = new Spectral(gLeft, gRight);

		//Events
		EventHandler<ActionEvent> clearEmbedding = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				spectral.resetEmbeddings();
			}
		};
		EventHandler<ActionEvent> addPointsEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				spectral.addNRandPoints(100);
			}
		};
		
		//Pane to hold buttons
		TilePane tp = new TilePane();
		bp.setBottom(tp);
		Button clearPoints = new Button("Clear Points");
		Button addPoints = new Button("Add Points");
		clearPoints.setOnAction(clearEmbedding);
		addPoints.setOnAction(addPointsEvent);
		tp.getChildren().add(clearPoints);
		tp.getChildren().add(addPoints);


        scene.setRoot(bp);
        stage.setScene(scene);
		//stage.setFullScreen(true);
		stage.show();
		

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
					case X:
						System.exit(1);
						break;
                }
            }
		});
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
				double clickedX = event.getSceneX() - w / 2.0;
				double clickedY = -event.getSceneY() + h / 2.0;
				if (clickedX < w/2 && clickedX > -w/2 && clickedY < h/2 && clickedY > -h/2) {
					System.out.println(clickedX + " " + clickedY);
				}
            }
		});
		
		new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
				gLeft.clearRect(-w / 2.0, -h / 2.0, w, h);
				gRight.clearRect(-w / 2.0, -h / 2.0, w, h);
				spectral.draw();
            }
        }.start();
    }

    @Override
    public void stop() throws Exception{
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}