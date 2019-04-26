import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Embedding {

	class Node {
		double x;
		double y;
		Node(double x, double y){
			this.x = x;
			this.y = y;
		}
	}

	class Edge {
		Node a;
		Node b;
		Edge(Node _a, Node _b) {
			a = _a;
			b = _b;
		}
	}

	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;

	Embedding() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	double[][] getAdjacency() {
		double[][] A = new double[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (i == j) A[i][j] = 0;
				else if (i < j) A[i][j] = (isEdge(nodes.get(i), nodes.get(j))) ? 1 : 0;
				else A[i][j] = A[j][i];
			}
		}
//		new Matrix(A).print(A.length, 1);
		return A;
//		double[][] x = {{2, -1, -1, 0}, {-1, 2, -1, 0}, {-1, -1, 3, -1}, {0, 0, -1, 1}};
//		double[][] x = {{0, 1, 1, 0}, {1, 0, 1, 0}, {1, 1, 0, 1}, {0, 0, 1, 0}};
//		return x;
	}

	void addNode(double x, double y) {
		nodes.add(new Node(x, y));
	}
	Node getNode(int i) {
		return nodes.get(i);
	}
	int getNodeCount() {
		return nodes.size();
	}
	void clearEmbedding() {
		nodes.clear();
		edges.clear();
	}

	void addEdge(Node a, Node b) {
		if (!isEdge(a, b))
			edges.add(new Edge(a, b));
	}
	void makeEdges() {
		for (int i = 0; i < nodes.size() - 1; i++) {
			if (!isEdge(nodes.get(i), nodes.get(i+1)))
				edges.add(new Edge(nodes.get(i), nodes.get(i+1)));
		}
	}
	void clearEdges() {
		edges.clear();
	}

	// Tests if the edge is already in the set of edges
	boolean isEdge(Node a, Node b) {
		for (Edge e : edges) {
			if (e.a == a && e.b == b) return true;
			else if (e.a == b && e.b == a) return true;
		}
		return false;
	}

	//For Qinqing
	//====================================================================================================
	void triangulateNodes() {

	}
	//====================================================================================================

	void draw(GraphicsContext g) {
		g.setFill(Color.WHITE);
		double pointRadius = 4.0;
		for (Node i : nodes) {
			g.fillOval(i.x - pointRadius/2.0, i.y - pointRadius/2.0, pointRadius, pointRadius);
		}
		
		g.setStroke(Color.WHITE);
		for (Edge i : edges) {
			g.strokeLine(i.a.x, i.a.y, i.b.x, i.b.y);
		}
	}
}
