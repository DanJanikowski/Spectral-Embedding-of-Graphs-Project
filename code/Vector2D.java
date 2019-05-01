public class Vector2D {
    double x;
    double y;
    Vector2D(double _x, double _y) {
        x = _x;
        y = _y;
    }
    Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }
    void addEquals(Vector2D v) {
        x += v.x;
        y += v.y;
    }
    Vector2D sub(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }
    void subEquals(Vector2D v) {
        x -= v.x;
        y -= v.y;
    }
    Vector2D mult(double s) {
        return new Vector2D(x * s, y * s);
    }
    double squareMag() {
        return x * x + y * y;
    }
    double mag() {
        return Math.sqrt(squareMag());
    }
    double dist(double x2, double y2) {
        return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
    }

    double dot(Vector2D v) {
        return x * v.x + y * v.y;
    }

    double cross(Vector2D v) {
        return x * v.y - y * v.x;
    }
}