import java.util.Random;

import Jama.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

class Spectral {

	private double screenW, screenH;
	private double canvasW, canvasH;
	private double edgeBuffer = 20; // Closest distance points can be generated from the edge

	private GraphicsContext gLeft, gRight;
	private Random rand;

	Embedding editable;
	Embedding spectralEmbedding;

	private int EigVecIndex = 1;
	private int plottingMethod = 0; // 0 for x/y and 1 for time/y
	private Matrix eigenDecomp;

	Spectral(BorderPane bp){
		rand = new Random();

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		screenW = screenBounds.getMaxX();
		screenH = screenBounds.getMaxY();
//		canvasW = screenW / 2.0;
//		canvasH = canvasW;
		canvasW = 800;
		canvasH = 800;

		Canvas canvasLeft = new Canvas(canvasW, canvasH);
		Canvas canvasRight = new Canvas(canvasW, canvasH);
		bp.setLeft(canvasLeft);
		bp.setRight(canvasRight);
		gLeft = canvasLeft.getGraphicsContext2D();
		gRight = canvasRight.getGraphicsContext2D();
		// Translate and flip both canvas' such that origin is at middle of each
		gLeft.translate(canvasW / 2.0, canvasH / 2.0);
		gLeft.scale(1.0, -1.0);
		gRight.translate(canvasW / 2.0, canvasH / 2.0);
		gRight.scale(1.0, -1.0);


		canvasLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double clickedX = event.getSceneX() - canvasW / 2.0;
				double clickedY = -event.getSceneY() + canvasH / 2.0;
//				System.out.println(clickedX + " " + clickedY);
				editable.addNode(clickedX, clickedY);
				redraw();
			}
		});

		editable = new Embedding();
		spectralEmbedding = new Embedding();
		redraw();
	}

	// Change which eigenvectors are used to draw the spectral embedding
	void changeEigVecIndex(int i) {
		if (i == 0) {
			if (EigVecIndex > 0) EigVecIndex--;
		} else if (i == 1) {
			if (EigVecIndex < editable.getNodeCount() - 2) EigVecIndex++;
		}
		if (eigenDecomp != null)
			recreateSpectralEmbedding();
		redraw();
	}
	void changePlottingMethod() {
		plottingMethod = (plottingMethod + 1) % 2;
		if (eigenDecomp != null)
			recreateSpectralEmbedding();
		redraw();
	}

	void createEigenDecomp() {
		EigVecIndex = 1;

		double[][] Atemp = editable.getAdjacency();
		double[][] Dtemp = new double[Atemp.length][Atemp.length];

		for (int i = 0; i < Dtemp.length; i++) {
			double sum = 0;
			for (int j = 0; j < Atemp.length; j++)
				sum += Atemp[i][j];
			Dtemp[i][i] = sum;
		}
		Matrix A = new Matrix(Atemp);
		Matrix D = new Matrix(Dtemp);
		Matrix L = D.minus(A);
		eigenDecomp = L.eig().getV().transpose();
	}
	void recreateSpectralEmbedding() {
		spectralEmbedding.clearEmbedding();

		double[][] Atemp = editable.getAdjacency();
		int pointCount = Atemp.length;

		if (plottingMethod == 0) {
			double[] xcoords = eigenDecomp.getArray()[EigVecIndex];
			double[] ycoords = eigenDecomp.getArray()[EigVecIndex+1];
			for (int i = 0; i < xcoords.length; i++)
				spectralEmbedding.addNode(xcoords[i] * pointCount * 10, ycoords[i] * pointCount * 10);
		} else if (plottingMethod == 1) {
			double[] xcoords = new double[pointCount];
			double[] ycoords = eigenDecomp.getArray()[EigVecIndex];
			double time = -canvasW/2.0 + edgeBuffer;
			double dt = (canvasW - 2*edgeBuffer) / (double)pointCount;
			for (int i = 0; i < xcoords.length; i++) {
				xcoords[i] = time;
				time += dt;
			}
			for (int i = 0; i < xcoords.length; i++)
				spectralEmbedding.addNode(xcoords[i], ycoords[i] * pointCount * 10);
		}
		for (int i = 0; i < Atemp.length; i++) {
			for (int j = i + 1; j < Atemp[0].length; j++) {
				if (Atemp[i][j] == 1)
					spectralEmbedding.addEdge(spectralEmbedding.getNode(i), spectralEmbedding.getNode(j));
			}
		}
	}

	void resetEmbeddings() {
		editable.clearEmbedding();
		spectralEmbedding.clearEmbedding();
	}

	void redraw() {
		gLeft.clearRect(-canvasW / 2.0, -canvasH / 2.0, canvasW, canvasH);
		gRight.clearRect(-canvasW / 2.0, -canvasH / 2.0, canvasW, canvasH);
		gLeft.setFill(Color.WHITE);
		gRight.setFill(Color.WHITE);

		editable.draw(gLeft);
		spectralEmbedding.draw(gRight);

		gLeft.setStroke(Color.WHITE);
		gLeft.strokeLine(canvasW / 2.0, canvasH / 2.0, -canvasW / 2.0, canvasH / 2.0);
		gLeft.strokeLine(-canvasW / 2.0, canvasH / 2.0, -canvasW / 2.0, -canvasH / 2.0);
		gLeft.strokeLine(-canvasW / 2.0, -canvasH / 2.0, canvasW / 2.0, -canvasH / 2.0);
		gLeft.strokeLine(canvasW / 2.0, -canvasH / 2.0, canvasW / 2.0, canvasH / 2.0);
		gRight.setStroke(Color.WHITE);
		gRight.strokeLine(canvasW / 2.0, canvasH / 2.0, -canvasW / 2.0, canvasH / 2.0);
		gRight.strokeLine(-canvasW / 2.0, canvasH / 2.0, -canvasW / 2.0, -canvasH / 2.0);
		gRight.strokeLine(-canvasW / 2.0, -canvasH / 2.0, canvasW / 2.0, -canvasH / 2.0);
		gRight.strokeLine(canvasW / 2.0, -canvasH / 2.0, canvasW / 2.0, canvasH / 2.0);

		gLeft.scale(1.0, -1.0);
		gLeft.translate(-canvasW / 2.0, -canvasH / 2.0);
		gLeft.setStroke(Color.GREEN);
		if (plottingMethod == 0)
			gLeft.strokeText("Drawing from eigenvectors: " + EigVecIndex + " and " + (EigVecIndex + 1), 20, 20);
		else if (plottingMethod == 1)
			gLeft.strokeText("Drawing time versus eigenvector: " + EigVecIndex, 20, 20);
		gLeft.translate(canvasW / 2.0, canvasH / 2.0);
		gLeft.scale(1.0, -1.0);
	}

	// Events
	EventHandler<ActionEvent> createClearEvent() {
		return new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				resetEmbeddings();
				redraw();
			}
		};
	}
	EventHandler<ActionEvent> createAddPointsEvent() {
		return new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				for (int i = 0; i < 100; i++)
					editable.addNode(rand.nextDouble() * (canvasW - 2.0 * edgeBuffer) - canvasW / 2.0 + edgeBuffer,
										rand.nextDouble() * (canvasH - 2.0 * edgeBuffer) - canvasH / 2.0 + edgeBuffer);
				redraw();
			}
		};
	}
	EventHandler<ActionEvent> createSpecEmbedEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				createEigenDecomp();
				recreateSpectralEmbedding();
				redraw();
			}
		};
	}
	EventHandler<ActionEvent> makeEdgesEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				editable.makeEdges();
				redraw();
			}
		};
	}
	EventHandler<ActionEvent> clearEdgesEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				editable.clearEdges();
				spectralEmbedding.clearEmbedding();
				redraw();
			}
		};
	}
	EventHandler<ActionEvent> triangulatePointsEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				editable.clearEdges();
				editable.triangulateNodes();
				redraw();
			}
		};
	}
}
