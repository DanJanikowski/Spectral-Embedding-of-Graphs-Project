import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Spectral {

	private GraphicsContext gLeft, gRight;
	private double screenW, screenH;
	private Random rand;

	Embedding editable;
	Embedding spectralEmbedding;

	Spectral(GraphicsContext gl, GraphicsContext gr){
		rand = new Random();
		gLeft = gl;
		gRight = gr;
		screenW = gLeft.getCanvas().getWidth();
		screenH = gLeft.getCanvas().getHeight();
		
		gLeft.translate(screenW / 2.0, screenH / 2.0);
		gLeft.scale(1.0, -1.0);
		gRight.translate(screenW / 2.0, screenH / 2.0);
		gRight.scale(1.0, -1.0);

		resetEmbeddings();
	}

	void resetEmbeddings() {
		editable = new Embedding();
		spectralEmbedding = new Embedding();
	}

	void addPoint(double _x, double _y) {
		editable.addPoint(_x, _y);
	}

	void addNRandPoints(int N) {
		for (int i = 0; i < N; i++) {
			addPoint(rand.nextDouble() * screenW - screenW / 2.0, rand.nextDouble() * screenH - screenH / 2.0);
		}
	}

	void draw() {
		gLeft.setFill(Color.WHITE);
		gRight.setFill(Color.WHITE);
		// gLeft.fillOval(-200, -200, 4, 4);
		// gRight.fillOval(200, 200, 4, 4);
		editable.draw(gLeft);
		spectralEmbedding.draw(gRight);

		gRight.setStroke(Color.WHITE);
		gRight.strokeLine(-screenW / 2.0, screenH / 2.0, -screenW / 2.0, -screenH / 2.0);
	}
}
