import java.awt.geom.GeneralPath;

public class Plane extends DrawableObject {

    public Plane(Vector p1, Vector p2, Vector p3, Vector p4) {
        vectors = new Vector[4];
        vectors[0] = p1;
        vectors[1] = p2;
        vectors[2] = p3;
        vectors[3] = p4;
    }

    @Override
    public void update(int deltaTime) {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(G3D g3d, Vector[] v) {

        if (zCheck(v[0]) || zCheck(v[1]) || zCheck(v[2]) || zCheck(v[3])) {
            Vector normal = G3D.computeNormal(v);
            if (normal.z >= 0) {
                GeneralPath gp = new GeneralPath();
                gp.moveTo(v[0].x, v[0].y);
                gp.lineTo(v[1].x, v[1].y);
                gp.lineTo(v[2].x, v[2].y);
                gp.lineTo(v[3].x, v[3].y);
                gp.closePath();

                g3d.getGraphics().draw(gp);
            }

        }

    }

    @Override
    public void fill(G3D g3d, Vector[] v) {
        if (zCheck(v[0]) || zCheck(v[1]) || zCheck(v[2]) || zCheck(v[3])) {
            Vector normal = G3D.computeNormal(v);
            if (normal.z >= 0) {

                GeneralPath gp = new GeneralPath();
                gp.moveTo(v[0].x, v[0].y);
                gp.lineTo(v[1].x, v[1].y);
                gp.lineTo(v[2].x, v[2].y);
                gp.lineTo(v[3].x, v[3].y);
                gp.closePath();

                g3d.getGraphics().fill(gp);
            }
        }

    }

}
