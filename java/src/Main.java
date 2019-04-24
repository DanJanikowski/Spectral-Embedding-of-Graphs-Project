import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

public class Main extends Application {

	private final int screenW = 1920;
	private final int screenH = 1080;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Canvas canvas = new Canvas(screenW,screenH);
		Scene scene = new Scene(new StackPane(canvas),screenW,screenH,false,SceneAntialiasing.BALANCED);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);

		Spectral spectral = new Spectral(canvas.getGraphicsContext2D()); //Where everything will be run from

		scene.setOnMouseClicked(event -> new Thread(() -> {
			Platform.runLater(() -> {
				System.out.println("Mouse Clicked:" + event.getX() + ", " + event.getY());
			});
		}).start());

		scene.setOnKeyPressed(event -> new Thread(() -> {
			Platform.runLater(() -> {
				System.out.println("Key Pressed: " + event.getCode());
				switch (event.getCode()) {
					case ESCAPE:
						System.exit(0);
						break;
				}
			});
		}).start());

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
