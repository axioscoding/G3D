import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class Box extends DrawableObject {

    double offset = 0;

    Color color = Color.WHITE;
    public int[][] faces = {
            { 0, 1, 2, 3 }, { 5, 4, 7, 6 }, { 4, 0, 3, 7 }, { 1, 5, 6, 2 }, { 4, 5, 1, 0 }, { 3, 2, 6, 7 }
    };

    public Box(Vector pos, Vector dims) {
        this.pos = pos;
        vectors = new Vector[8];
        vectors[0] = new Vector(pos.x - dims.x / 2, pos.y - dims.y / 2, pos.z - dims.z / 2);
        vectors[1] = new Vector(pos.x + dims.x / 2, pos.y - dims.y / 2, pos.z - dims.z / 2);
        vectors[2] = new Vector(pos.x + dims.x / 2, pos.y + dims.y / 2, pos.z - dims.z / 2);
        vectors[3] = new Vector(pos.x - dims.x / 2, pos.y + dims.y / 2, pos.z - dims.z / 2);

        vectors[4] = new Vector(pos.x - dims.x / 2, pos.y - dims.y / 2, pos.z + dims.z / 2);
        vectors[5] = new Vector(pos.x + dims.x / 2, pos.y - dims.y / 2, pos.z + dims.z / 2);
        vectors[6] = new Vector(pos.x + dims.x / 2, pos.y + dims.y / 2, pos.z + dims.z / 2);
        vectors[7] = new Vector(pos.x - dims.x / 2, pos.y + dims.y / 2, pos.z + dims.z / 2);
    }

    public void setColor(Color c) {
        color = c;
    }

    public void addY(double amount) {
        pos.y += amount;
        for (int i = 0; i < 8; i++) {
            vectors[i].y += amount;

        }
    }

    @Override
    public void update(int deltaTime) {

    }

    @Override
    public void draw(G3D g3d, Vector[] v) {
        Graphics2D g = g3d.getGraphics();

        g.setColor(color);

        for (int i = 0; i < 4; i++) {
            if (v[i].z > 0 && v[i].z < 1 && v[(i + 1) % 4].z > 0 && v[(i + 1) % 4].z < 1) {
                g.drawLine((int) v[i].x, (int) v[i].y, (int) v[(i + 1) % 4].x, (int) v[(i + 1) % 4].y);
            }

        }

        for (int i = 0; i < 4; i++) {
            if (v[i + 4].z > 0 && v[i + 4].z < 1 && v[(i + 1) % 4 + 4].z > 0 && v[(i + 1) % 4 + 4].z < 1) {
                g.drawLine((int) v[i + 4].x, (int) v[i + 4].y, (int) v[(i + 1) % 4 + 4].x, (int) v[(i + 1) % 4 + 4].y);
            }

        }

        for (int i = 0; i < 4; i++) {
            if (v[i].z > 0 && v[i].z < 1 && v[i + 4].z > 0 && v[i + 4].z < 1) {
                g.drawLine((int) v[i].x, (int) v[i].y, (int) v[i + 4].x, (int) v[i + 4].y);
            }

        }

    }

    @Override
    public void fill(G3D g3d, Vector[] v) {

        for (int i = 0; i < faces.length; i++) {
            if (zCheck(v[faces[i][0]]) && zCheck(v[faces[i][1]]) && zCheck(v[faces[i][2]]) && zCheck(v[faces[i][3]])) {
                Vector[] faceVec = { v[faces[i][0]], v[faces[i][1]], v[faces[i][2]], v[faces[i][3]] };
                Vector normal = G3D.computeNormal(faceVec);

                if (normal.z < 0)
                    continue;
                drawFaces(i, g3d.getGraphics(), v);
            }
        }

    }

    private void drawFaces(int index, Graphics2D g, Vector[] v) {

        switch (index) {
            case 0:
                g.setColor(Color.RED);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
            case 2:
                g.setColor(Color.BLUE);
                break;
            case 3:
                g.setColor(Color.CYAN);
                break;
            case 4:
                g.setColor(Color.YELLOW);
                break;
            case 5:
                g.setColor(Color.MAGENTA);
                break;
            default:
                g.setColor(Color.WHITE);
        }
        GeneralPath gp = new GeneralPath();
        gp.moveTo(v[faces[index][0]].x, v[faces[index][0]].y);
        gp.lineTo(v[faces[index][1]].x, v[faces[index][1]].y);
        gp.lineTo(v[faces[index][2]].x, v[faces[index][2]].y);
        gp.lineTo(v[faces[index][3]].x, v[faces[index][3]].y);
        gp.closePath();

        g.fillPolygon(
                new int[] { (int) v[faces[index][0]].x, (int) v[faces[index][1]].x, (int) v[faces[index][2]].x,
                        (int) v[faces[index][3]].x },
                new int[] { (int) v[faces[index][0]].y, (int) v[faces[index][1]].y, (int) v[faces[index][2]].y,
                        (int) v[faces[index][3]].y },
                4);

        // g.fill(gp);
    }

}
