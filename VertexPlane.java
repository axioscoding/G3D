import java.awt.geom.GeneralPath;
import java.awt.Color;
import java.awt.GradientPaint;

public class VertexPlane extends DrawableObject {

    private int rows, cols;
    private Color[] colors;

    public VertexPlane(Vector p1, Vector p2, Vector p3, Vector p4, int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        colors = new Color[cols * rows];
        vectors = new Vector[cols * rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                double x = G3D.map(0, rows, i, G3D.map(0, cols, j, p1.x, p2.x), G3D.map(0, cols, j, p4.x, p3.x));
                double y = G3D.map(0, rows, i, G3D.map(0, cols, j, p1.y, p2.y), G3D.map(0, cols, j, p4.y, p3.y));
                double z = G3D.map(0, rows, i, G3D.map(0, cols, j, p1.z, p2.z), G3D.map(0, cols, j, p4.z, p3.z));
                vectors[index] = new Vector(x, y, z);
                colors[index] = Color.WHITE;
            }
        }
    }

    @Override
    public void update(int deltaTime) {

    }

    public void setColor(int index, Color c) {
        colors[index] = c;
    }

    @Override
    public void draw(G3D g3d, Vector[] v) {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols - 1; j++) {
                int index = i * cols + j;
                if (v[index].z > 0 && v[index].z < 1
                        && v[index + 1].z > 0 && v[index + 1].z < 1
                        && v[index + cols].z > 0 && v[index + cols].z < 1
                        && v[index + cols + 1].z > 0 && v[index + cols + 1].z < 1
                        && (v[index].x != 0 || v[index].y != 0)) {
                    g3d.getGraphics().setColor(colors[index]);
                    GeneralPath gp = new GeneralPath();
                    gp.moveTo(v[index].x, v[index].y);
                    gp.lineTo(v[index + cols].x, v[index + cols].y);
                    gp.lineTo(v[index + 1].x, v[index + 1].y);
                    gp.closePath();
                    g3d.getGraphics().draw(gp);

                    gp.moveTo(v[index + 1].x, v[index + 1].y);
                    gp.lineTo(v[index + cols + 1].x, v[index + cols + 1].y);
                    gp.lineTo(v[index + cols].x, v[index + cols].y);
                    gp.closePath();
                    g3d.getGraphics().draw(gp);
                }

            }

        }

    }

    @Override
    public void fill(G3D g3d, Vector[] v) {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols - 1; j++) {
                if (true) {
                    int index = i * cols + j;
                    if (v[index].z > 0 && v[index].z < 1
                            && v[index + 1].z > 0 && v[index + 1].z < 1
                            && v[index + cols].z > 0 && v[index + cols].z < 1
                            && v[index + cols + 1].z > 0 && v[index + cols + 1].z < 1
                            && (v[index].x != 0 || v[index].y != 0)) {
                        // (v[index + cols + 1].x - ((v[index + cols].x + v[index + cols + 1].x) / 2))
                        // (v[index].x + ((v[index].x - v[index+1].x) / 2))

                        // g3d.getGraphics().setColor(colors[index]);

                        GeneralPath gp = new GeneralPath();
                        gp.moveTo(v[index].x, v[index].y);
                        gp.lineTo(v[index + cols].x, v[index + cols].y);
                        gp.lineTo(v[index + 1].x, v[index + 1].y);
                        gp.closePath();

                        GradientPaint gradp = new GradientPaint((float) v[index].x, (float) v[index].y,
                                colors[index],
                                (float) v[index + cols - 1].x, (float) v[index + cols - 1].y, colors[index + cols - 1]);

                        g3d.getGraphics().setPaint(gradp);

                        g3d.setColor(colors[index]);

                        g3d.getGraphics().fill(gp);

                        gp.moveTo(v[index + 1].x, v[index + 1].y);
                        gp.lineTo(v[index + cols + 1].x, v[index + cols + 1].y);
                        gp.lineTo(v[index + cols].x, v[index + cols].y);
                        gp.closePath();
                        g3d.getGraphics().fill(gp);
                    }

                }
            }

        }

    }

}
