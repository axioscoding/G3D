public class Vector {

    public double x, y, z, w;
    public double alpha, beta;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.w = 0;
        this.alpha = Math.atan2(y, x);

    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 0;
        // this.angle = Math.atan2(y, x);

    }

    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        // this.angle = Math.atan2(y, x);

    }

    public Vector(double angle) {
        this.alpha = angle;
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
    }

    public void fromAngles(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
        x = Math.cos(alpha) * Math.cos(beta);
        y = Math.sin(alpha) * Math.cos(beta);
        z = Math.sin(beta);
    }

    public void add(Vector b) {
        this.x += b.x;
        this.y += b.y;
        this.z += b.z;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void mult(Vector b) {
        this.x *= b.x;
        this.y *= b.y;
        this.z *= b.z;
    }

    public void mult(double i) {
        this.x *= i;
        this.y *= i;
        this.z *= z;
    }

    public Vector mul(Vector b) {
        return new Vector(this.x * b.x, this.y * b.y, this.z * b.z);
    }

    public Vector mul(double i) {
        return new Vector(this.x * i, this.y * i, this.z * i);
    }

    public void limit(double mag) {
        double norm = mag();
        if (norm > 0) {
            double f = Math.min(mag, norm) / norm;
            if (f != 0) {
                this.x *= f;
                this.y *= f;
                this.z *= f;
            }
        }

    }

    public Vector normalize() {
        double norm = mag();
        if (norm == 0)
            return new Vector(0, 0, 0);
        return new Vector(x / norm, y / norm, z / norm);
    }

    public double distance(Vector b) {
        return Math.sqrt((x - b.x) * (x - b.x) + (y - b.y) * (y - b.y) + (z - b.z) * (z - b.z));
    }

    public Vector abs() {
        return new Vector(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public double[][] toMatrix4() {
        double[][] v = {
                { x },
                { y },
                { z },
                { 1 }
        };
        return v;
    }

    public static Vector add(Vector a, Vector b) {

        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z + " W: " + w;
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public Vector matmul(double[][] a) {

        return new Vector(a[0][0] * x + a[0][1] * y + a[0][2] * z + a[0][3] * w,
                a[1][0] * x + a[1][1] * y + a[1][2] * z + a[1][3] * w,
                a[2][0] * x + a[2][1] * y + a[2][2] * z + a[2][3] * w,
                a[3][0] * x + a[3][1] * y + a[3][2] * z + a[3][3] * w);

    }
}
