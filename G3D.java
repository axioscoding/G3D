import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class G3D {

    Graphics2D g = null;
    Color color = Color.WHITE;
    double stroke = 1;

    double noiseDelta = 0;
    double noiseScalar = 0.002; // 0.002
    double noiseMultiplier = 165;
    double noiseSpeed = 0.01; // 0.01

    double FOV = 0.015;
    double ZFAR = 1500;
    double ZNEAR = 10;

    float colorOffset = 0;
    float colorDelta = 0.1f;

    double maxY = 0;
    double minY = 0;

    Vector CAMERA_POSITION = new Vector(0, 0, 0);
    Vector facing = new Vector(0, 0, 0);
    double fov = (90 * Math.PI / 180);

    static double speed = 1;
    static double rotSpeed = 0.1;

    double[][] translationMatrix = Matrix.getDefaultMatrix();
    double[][] scalingMatrix = Matrix.getDefaultMatrix();
    double[][] rotationXMatrix = Matrix.getDefaultMatrix();
    double[][] rotationYMatrix = Matrix.getDefaultMatrix();
    double[][] rotationZMatrix = Matrix.getDefaultMatrix();
    double[][] rotationCenter = Matrix.getDefaultMatrix();
    double[][] rotateBack = Matrix.getDefaultMatrix();
    double[][] cameraTransformation = Matrix.getDefaultMatrix();
    double[][] projMat = Matrix.getDefaultMatrix();

    public G3D() {

    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setStroke(double i) {
        stroke = i;
    }

    public Graphics2D getGraphics() {
        return g;
    }

    /*
     * 
     * 
     * Shapes
     * 
     * 
     */

    public void draw(DrawableObject o) {
        g.setColor(color);
        o.draw(this, applyTransformations(o.getVectors()));
    }

    public void fill(DrawableObject o) {
        g.setColor(color);
        o.fill(this, applyTransformations(o.getVectors()));
    }

    public void fill(DrawableObject[] o) {
        DrawableObject[] o2 = o.clone();
        // Z buffering
        Arrays.sort(o2, new Comparator<DrawableObject>() {
            @Override
            public int compare(DrawableObject o1, DrawableObject o2) {
                Vector o1Pos = applyTransformations(o1.getPos());
                Vector o2Pos = applyTransformations(o2.getPos());
                if (o1Pos.z > o2Pos.z) {
                    return -1;
                } else if (o1Pos.z < o2Pos.z) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (DrawableObject drawableObject : o2) {
            g.setColor(color);
            drawableObject.fill(this, applyTransformations(drawableObject.getVectors()));
        }
    }

    /*
     * 
     * 
     * Transformations
     * 
     * 
     */

    public Vector[] applyTransformations(Vector[] v) {

        Vector[] v2 = new Vector[v.length];

        for (int i = 0; i < v.length; i++) {
            v2[i] = applyTransformations(v[i]);
        }
        return v2;
    }

    public Vector applyTransformations(Vector m) {
        m.w = 1;

        m = Matrix.matmulV4(rotationCenter, m);

        m = Matrix.matmulV4(rotationXMatrix, m);
        m = Matrix.matmulV4(rotationYMatrix, m);
        m = Matrix.matmulV4(rotationZMatrix, m);

        m = Matrix.matmulV4(rotateBack, m);

        m = Matrix.matmulV4(scalingMatrix, m);
        m = cameraTransform(m);
        m = applyPerspective(m);

        m.x += translationMatrix[0][3];
        m.y += translationMatrix[1][3];
        m.z += translationMatrix[2][3];
        return m;
    }

    public Vector cameraTransform(Vector m) {
        double[][] xrot = {
                { 1, 0, 0, 0 },
                { 0, Math.cos(facing.y), Math.sin(facing.y), 0 },
                { 0, -Math.sin(facing.y), Math.cos(facing.y), 0 },
                { 0, 0, 0, 1 }
        };

        double[][] yrot = {
                { Math.cos(facing.x), 0, -Math.sin(facing.x), 0 },
                { 0, 1, 0, 0 },
                { Math.sin(facing.x), 0, Math.cos(facing.x), 0 },
                { 0, 0, 0, 1 }
        };

        double[][] zrot = {
                { Math.cos(facing.z), -Math.sin(facing.z), 0, 0 },
                { Math.sin(facing.z), Math.cos(facing.z), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

        m = Vector.add(m, CAMERA_POSITION.mul(-1));
        m = Matrix.matmulV4(xrot, m);
        m = Matrix.matmulV4(yrot, m);
        m = Matrix.matmulV4(zrot, m);

        return m;
    }

    public Vector applyPerspective(Vector m) {

        double[][] vs = {
                { ((double) Window.HEIGHT / (double) Window.WIDTH) * (1.0 / Math.tan(fov / 2.0)), 0, 0, 0 },
                { 0, 1.0 / Math.tan(fov / 2.0), 0, 0 },
                { 0, 0, ZFAR / (ZFAR - ZNEAR), (-ZFAR * ZNEAR) / (ZFAR - ZNEAR) },
                { 0, 0, 1, 0 }
        };

        return project(Matrix.matmulV4(vs, m));
    }

    // 1.0067114094
    // -10.067114094

    public Vector project(Vector m) {

        if (m.w != 0) {
            double[][] div = {
                    { 1.0 / (m.w), 0, 0, 0 },
                    { 0, 1.0 / (m.w), 0, 0 },
                    { 0, 0, 1.0 / (m.w), 0 },
                    { 0, 0, 0, 1 }
            };

            m = Matrix.matmulV4(div, m);
            // m.vals[3][0] = 1;
            double[][] mul = {
                    { Window.WIDTH, 0, 0, 0 },
                    { 0, Window.HEIGHT, 0, 0 },
                    { 0, 0, 1, 0 },
                    { 0, 0, 0, 1 }
            };
            return Matrix.matmulV4(mul, m);
        } else {
            return m;
        }
    }

    public void translate(Vector v) {
        double[][] vals = {
                { 1, 0, 0, v.x },
                { 0, 1, 0, v.y },
                { 0, 0, 1, v.z },
                { 0, 0, 0, 1 }
        };
        translationMatrix = vals;
    }

    public void resetTranslation() {
        translationMatrix = Matrix.getDefaultMatrix();
    }

    public void scale(double scale) {
        double[][] vals = {
                { scale, 0, 0, 0 },
                { 0, scale, 0, 0 },
                { 0, 0, scale, 0 },
                { 0, 0, 0, 1 }
        };
        scalingMatrix = vals;
    }

    public void scale(double scalex, double scaley, double scalez) {
        double[][] vals = {
                { scalex, 0, 0, 0 },
                { 0, scaley, 0, 0 },
                { 0, 0, scalez, 0 },
                { 0, 0, 0, 1 }
        };
        scalingMatrix = vals;
    }

    public void resetScale() {
        scalingMatrix = Matrix.getDefaultMatrix();
    }

    public void rotateX(double angle) {
        double[][] vals = {
                { 1, 0, 0, 0 },
                { 0, Math.cos(angle), Math.sin(angle), 0 },
                { 0, -Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        };
        rotationXMatrix = vals;
    }

    public void rotateY(double angle) {
        double[][] vals = {
                { Math.cos(angle), 0, -Math.sin(angle), 0 },
                { 0, 1, 0, 0 },
                { Math.sin(angle), 0, Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        };
        rotationYMatrix = vals;
    }

    public void rotateZ(double angle) {
        double[][] vals = {
                { Math.cos(angle), -Math.sin(angle), 0, 0 },
                { Math.sin(angle), Math.cos(angle), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };
        rotationZMatrix = vals;
    }

    public void rotateX(double angle, Vector pos) {
        double[][] vals = {
                { 1, 0, 0, 0 },
                { 0, Math.cos(angle), Math.sin(angle), 0 },
                { 0, -Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        };

        double[][] vals2 = {
                { 1, 0, 0, pos.x },
                { 0, 1, 0, pos.y },
                { 0, 0, 1, pos.z },
                { 0, 0, 0, 1 }
        };

        double[][] vals3 = {
                { 1, 0, 0, -pos.x },
                { 0, 1, 0, -pos.y },
                { 0, 0, 1, -pos.z },
                { 0, 0, 0, 1 }
        };

        rotationCenter = vals2;
        rotateBack = vals3;

        rotationXMatrix = vals;
    }

    public void rotateY(double angle, Vector pos) {
        double[][] vals = {
                { Math.cos(angle), 0, -Math.sin(angle), 0 },
                { 0, 1, 0, 0 },
                { Math.sin(angle), 0, Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        };

        double[][] vals2 = {
                { 1, 0, 0, -pos.x },
                { 0, 1, 0, -pos.y },
                { 0, 0, 1, -pos.z },
                { 0, 0, 0, 1 }
        };

        double[][] vals3 = {
                { 1, 0, 0, pos.x },
                { 0, 1, 0, pos.y },
                { 0, 0, 1, pos.z },
                { 0, 0, 0, 1 }
        };

        rotationCenter = vals2;
        rotateBack = vals3;

        rotationYMatrix = vals;
    }

    public void rotateZ(double angle, Vector pos) {
        double[][] vals = {
                { Math.cos(angle), -Math.sin(angle), 0, 0 },
                { Math.sin(angle), Math.cos(angle), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

        double[][] vals2 = {
                { 1, 0, 0, pos.x },
                { 0, 1, 0, pos.y },
                { 0, 0, 1, pos.z },
                { 0, 0, 0, 1 }
        };

        double[][] vals3 = {
                { 1, 0, 0, -pos.x },
                { 0, 1, 0, -pos.y },
                { 0, 0, 1, -pos.z },
                { 0, 0, 0, 1 }
        };

        rotationCenter = vals2;
        rotateBack = vals3;

        rotationZMatrix = vals;
    }

    public static double map(double s, double e, double p, double s2, double e2) {
        return (p - s) * ((e2 - s2) / (e - s)) + s2;
    }

    public static Vector computeNormal(Vector[] v) {
        Vector normal = new Vector(0, 0, 0);

        for (int i = 0; i < v.length; i++) {
            Vector current = v[i];
            Vector next = v[(i + 1) % v.length];

            normal.x += ((current.y - next.y) * (current.z + next.z));
            normal.y += ((current.z - next.z) * (current.x + next.x));
            normal.z += ((current.x - next.x) * (current.y + next.y));
        }

        return normal.normalize();
    }

}
