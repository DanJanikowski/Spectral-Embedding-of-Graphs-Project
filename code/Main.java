import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(bp);
		Spectral spectral = new Spectral(bp);
		Embedding embedding = new Embedding();
		
		//Pane to hold buttons
		TilePane tp = new TilePane();
		bp.setBottom(tp);
        Button addPoints = new Button("Add Points");
		Button clearPoints = new Button("Clear Points");
        Button makeTempEdges = new Button("Make edges");
        Button clearTempEdges = new Button("Clear Edges");
        Button createTriangulation = new Button("Triangulate Points");
        Button makeSpectral = new Button("Spectral Embedding");
        addPoints.setOnAction(spectral.createAddPointsEvent());
		clearPoints.setOnAction(spectral.createClearEvent());
        makeTempEdges.setOnAction(spectral.makeEdgesEvent());
        clearTempEdges.setOnAction(spectral.clearEdgesEvent());
        createTriangulation.setOnAction(spectral.triangulatePointsEvent());
		makeSpectral.setOnAction(spectral.createSpecEmbedEvent());
        tp.getChildren().add(addPoints);
		tp.getChildren().add(clearPoints);
        tp.getChildren().add(makeTempEdges);
        tp.getChildren().add(clearTempEdges);
        tp.getChildren().add(createTriangulation);
		tp.getChildren().add(makeSpectral);


        scene.setRoot(bp);
        stage.setScene(scene);
//		stage.setFullScreen(true);
		stage.show();
		

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
                    case L:
                        spectral.changeEigVecIndex(1);
                        break;
                    case K:
                        spectral.changeEigVecIndex(0);
                        break;
                    case O:
                        spectral.changePlottingMethod();
                        break;
                    case E:
                    	System.out.println("test case E");
                    	embedding.DelauTri();
                    	final Canvas canvas = new Canvas(250,250);
                    	GraphicsContext gc = canvas.getGraphicsContext2D();
                    	embedding.draw(gc);
                    	tp.getChildren().add(canvas);
//                    	stage.setScene(s);
                    	stage.show();
                    	System.out.println("test case E end");
					case X:
						System.exit(1);
						break;
                }
            }
		});
    }

    @Override
    public void stop() throws Exception{
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}