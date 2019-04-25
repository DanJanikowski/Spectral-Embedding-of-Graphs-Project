import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Embedding {

	class Point {
		double x;
		double y;
		Point(double x, double y){
			this.x = x;
			this.y = y;
		}
	}

	class Edge {
		Point a;
		Point b;
		Edge(Point _a, Point _b) {
			a = _a;
			b = _b;
		}
	}

	ArrayList<Point> nodes;
	ArrayList<Edge> edges;

	Embedding() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	void addPoint(double x, double y) {
		nodes.add(new Point(x, y));
	}

	void draw(GraphicsContext g) {
		g.setFill(Color.WHITE);
		double pointRadius = 2.0;
		for (Point i : nodes) {
			g.fillOval(i.x - pointRadius/2.0, i.y - pointRadius/2.0, pointRadius, pointRadius);
		}
		
		g.setStroke(Color.WHITE);
		for (Edge i : edges) {
			g.strokeLine(i.a.x, i.a.y, i.b.x, i.b.x);
		}
	}
}
