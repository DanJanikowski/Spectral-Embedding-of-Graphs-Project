import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
        Spectral spectral = new Spectral(bp);
		Embedding embedding = new Embedding();
		
		//Pane to hold buttons
		TilePane tp = new TilePane();
		bp.setBottom(tp);

        Button addNodes = new Button("Add Nodes");
        addNodes.setOnAction(spectral.addNodesEvent());
        tp.getChildren().add(addNodes);

		Button clearNodes = new Button("Clear Nodes + Edges");
        clearNodes.setOnAction(spectral.clearNodesEvent());
        tp.getChildren().add(clearNodes);

        Button clearTempEdges = new Button("Clear Edges");
        clearTempEdges.setOnAction(spectral.clearEdgesEvent());
        tp.getChildren().add(clearTempEdges);

        Button sortNodesByX = new Button("Sort By X");
        sortNodesByX.setOnAction(spectral.sortXEvent());
        tp.getChildren().add(sortNodesByX);
        Button sortNodesByY = new Button("Sort By Y");
        sortNodesByY.setOnAction(spectral.sortYEvent());
        tp.getChildren().add(sortNodesByY);

        Button makePathGraph = new Button("Make Path Graph");
        makePathGraph.setOnAction(spectral.pathGraphEvent());
        tp.getChildren().add(makePathGraph);

        Button makeTreeGraph = new Button("Make Tree Graph");
        makeTreeGraph.setOnAction(spectral.treeGraphEvent());
        tp.getChildren().add(makeTreeGraph);

        Button makeDelaunayTriang = new Button("Delaunay Triangulation");
        makeDelaunayTriang.setOnAction(spectral.delaunayTriangEvent());
        tp.getChildren().add(makeDelaunayTriang);

        Button makeSpectral = new Button("Spectral Embedding");
		makeSpectral.setOnAction(spectral.spectralEmbedEvent());
		tp.getChildren().add(makeSpectral);

        ToggleButton connectNodes = new ToggleButton("Toggle Connect");
        connectNodes.setSelected(spectral.connecting);
        connectNodes.setOnAction(spectral.toggleEvent());
        tp.getChildren().add(connectNodes);


        Scene scene = new Scene(bp, spectral.screenW, spectral.screenH, false, SceneAntialiasing.BALANCED);
        stage.setScene(scene);
		stage.setFullScreen(true);
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