import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
	int ccw(Node a, Node b, Node c){
		int val = (int) ((b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y)); 
        if (val == 0) return 0;  // colinear 
        // clock or counterclock wise 
        return (val > 0)? 1: 2;  
	}
	
	boolean inTriangle(Node a, Node b, Node c, Node t){
		return (ccw(a,b,t) == ccw(b,c,t)) &&  (ccw(a,b,t) == ccw(c,a,t));
	}
	
	void randomNodes(int num) {
		Random rand = new Random(); 
		for (int i=0; i<num; i++) {
			double x = rand.nextFloat() * 10 ;
			double y = rand.nextFloat() * 10 ;
			addNode(x, y);
		}
	}
	
	void triangulateNodes() {
		randomNodes(100);
		for (int i = 0; i < nodes.size(); i++) {
            for (int j = i+1; j < nodes.size(); j++) {
                for (int k = j+1; k < nodes.size(); k++) {
                    boolean isTriangle = true;
                    for (int a = 0; a < nodes.size(); a++) {
                        if (a == i || a == j || a == k) continue;
                        if (inTriangle(nodes.get(i), nodes.get(j), nodes.get(k), nodes.get(a))) {
                           isTriangle = false;
                           break;
                        }
                    }
                    if (isTriangle) {
                    	addEdge(nodes.get(i), nodes.get(j));
                    	addEdge(nodes.get(i), nodes.get(k));
                    	addEdge(nodes.get(j), nodes.get(k));
                    }
                }
            }
		}
	}
	

	
	void DelauTri() {
		try {
			Vector<Vector2D> pointSet = new Vector<Vector2D>();
			
			Random rand = new Random(); 
			for (int i=0; i<10; i++) {
				double x = rand.nextFloat() * 10 ;
				double y = rand.nextFloat() * 10 ;
				pointSet.add(new Vector2D(x,y));
				addNode(x,y);
			}
		    
		    DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
		    delaunayTriangulator.triangulate();
		    
		    List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();
		    for (int i = 1; i < triangleSoup.size(); i++) {
		    	Triangle2D triangle = triangleSoup.get(i);
		    	addEdge(new Node(triangle.a.x, triangle.a.y), new Node(triangle.b.x, triangle.b.y));
		    	addEdge(new Node(triangle.b.x, triangle.b.y), new Node(triangle.c.x, triangle.c.y));
		    	addEdge(new Node(triangle.a.x, triangle.a.y), new Node(triangle.c.x, triangle.c.y));
		    }
//		    System.out.println(triangleSoup);
		    
		} catch (NotEnoughPointsException e) {
		}
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
