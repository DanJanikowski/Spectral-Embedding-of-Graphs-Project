import javafx.scene.canvas.GraphicsContext;

class Spectral {

	private GraphicsContext gfx;
	private double xmin = -1, xmax = 1, ymin = -1, ymax = 1;
	private double screenW, screenH;

	Spectral(GraphicsContext graphicsContext){
		gfx = graphicsContext;
		screenW = graphicsContext.getCanvas().getWidth();
		screenH = graphicsContext.getCanvas().getHeight();
	}

	//TODO methods for drawing graphs
}
