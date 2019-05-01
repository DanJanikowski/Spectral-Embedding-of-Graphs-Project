import Jama.Matrix;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class Embedding {

	class NodeDistCmp implements Comparator<Vector2D> {
		private Vector2D toCmp;
		NodeDistCmp(Vector2D node) {toCmp = node;}
		public int compare(Vector2D a, Vector2D b) {
			if (toCmp.sub(a).squareMag() < toCmp.sub(b).squareMag()) return -1;
			if (toCmp.sub(a).squareMag() > toCmp.sub(b).squareMag()) return 1;
			return 0;
		}
	}
	class NodeXCmp implements Comparator<Vector2D> {
		public int compare(Vector2D a, Vector2D b) {
			if (a.x < b.x) return -1;
			if (a.x > b.x) return 1;
			return 0;
		}
	}
	class NodeYCmp implements Comparator<Vector2D> {
		public int compare(Vector2D a, Vector2D b) {
			if (a.y < b.y) return -1;
			if (a.y > b.y) return 1;
			return 0;
		}
	}

	class Edge {
		Vector2D a;
		Vector2D b;
		Edge(Vector2D _a, Vector2D _b) {
			a = _a;
			b = _b;
		}
	}

	private final double nodeRadius = 8.0;
	private ArrayList<Vector2D> nodes;
	private ArrayList<Edge> edges;

	private Vector2D selected1, selected2;

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
		return A;
	}

	void addNode(double x, double y) {
		nodes.add(new Vector2D(x, y));
	}
	// Gets the node at the given index in the arraylist
	Vector2D getNode(int i) {
		return nodes.get(i);
	}
	// Tries to find and return the node at the given coordinates
	Vector2D findNode(double x, double y) {
		for (Vector2D n : nodes)
			if (n.dist(x, y) < nodeRadius) return n;
		return null;
	}
	int getNodeCount() {
		return nodes.size();
	}
	void selectNode(double x, double y) {
		if (selected1 == null)
			selected1 = findNode(x, y);
		else if (selected2 == null)
			selected2 = findNode(x, y);

		if (selected1 != null && selected2 != null) {
			addEdge(selected1, selected2);
			selected1 = null;
			selected2 = null;
		}
	}

	void addEdge(Vector2D a, Vector2D b) {
		if (!isEdge(a, b))
			edges.add(new Edge(a, b));
	}
	// Tests if the edge is already in the set of edges
	boolean isEdge(Vector2D a, Vector2D b) {
		for (Edge e : edges) {
			if (e.a == a && e.b == b) return true;
			else if (e.a == b && e.b == a) return true;
		}
		return false;
	}
	void clearEdges() {
		edges.clear();
	}

	void clearEmbedding() {
		nodes.clear();
		edges.clear();
	}

	// ===================================================================================================
	void sortByX() {
		Collections.sort(nodes, new NodeXCmp());
	}
	void sortByY() {
		Collections.sort(nodes, new NodeYCmp());
	}
	// Edge creation functions
    void PathGraph() {
        for (int i = 0; i < nodes.size() - 1; i++) {
            addEdge(nodes.get(i), nodes.get(i+1));
        }
    }
    void TreeGraph() {
        for (int i = 2; i < nodes.size()+1; i++) {
            addEdge(nodes.get(i-1), nodes.get(i/2-1));
        }
    }

	//For Qinqing
	//====================================================================================================
//	int ccw(Node a, Node b, Node c){
//		int val = (int) ((b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y));
//        if (val == 0) return 0;  // colinear
//        // clock or counterclock wise
//        return (val > 0)? 1: 2;
//	}
//
//	boolean inTriangle(Node a, Node b, Node c, Node t){
//		return (ccw(a,b,t) == ccw(b,c,t)) &&  (ccw(a,b,t) == ccw(c,a,t));
//	}
//
//	void randomNodes(int num) {
//		Random rand = new Random();
//		for (int i=0; i<num; i++) {
//			double x = rand.nextFloat() * 10 ;
//			double y = rand.nextFloat() * 10 ;
//			addNode(x, y);
//		}
//	}
//
//	void triangulateNodes() {
//		randomNodes(100);
//		for (int i = 0; i < nodes.size(); i++) {
//            for (int j = i+1; j < nodes.size(); j++) {
//                for (int k = j+1; k < nodes.size(); k++) {
//                    boolean isTriangle = true;
//                    for (int a = 0; a < nodes.size(); a++) {
//                        if (a == i || a == j || a == k) continue;
//                        if (inTriangle(nodes.get(i), nodes.get(j), nodes.get(k), nodes.get(a))) {
//                           isTriangle = false;
//                           break;
//                        }
//                    }
//                    if (isTriangle) {
//                    	addEdge(nodes.get(i), nodes.get(j));
//                    	addEdge(nodes.get(i), nodes.get(k));
//                    	addEdge(nodes.get(j), nodes.get(k));
//                    }
//                }
//            }
//		}
//	}

	void DelauTri() {
		if (nodes.size() < 3) return;

	    DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(nodes);
	    delaunayTriangulator.triangulate();
	    List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();
	    for (int i = 0; i < triangleSoup.size(); i++) {
	        Triangle2D triangle = triangleSoup.get(i);
	        addEdge(triangle.a, triangle.b);
			addEdge(triangle.b, triangle.c);
			addEdge(triangle.c, triangle.a);
	    }
	}

	//====================================================================================================

	void draw(GraphicsContext g) {
		Color translucentWhite = Color.rgb(100, 200, 200, 0.7);
		g.setFill(translucentWhite);
		g.setStroke(Color.WHITE);
		for (Vector2D i : nodes) {
			if (i == selected1)
				g.strokeOval(i.x - nodeRadius/2.0, i.y - nodeRadius/2.0, nodeRadius, nodeRadius);
			else
				g.fillOval(i.x - nodeRadius/2.0, i.y - nodeRadius/2.0, nodeRadius, nodeRadius);
		}
		
		g.setStroke(translucentWhite);
		for (Edge i : edges) {
			g.strokeLine(i.a.x, i.a.y, i.b.x, i.b.y);
		}
	}
}
