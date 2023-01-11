import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Sphere extends DrawableObject {

    private int resolution;

    private Color[] c;

    public Sphere(Vector pos, double radius, int resolution) {
        this.resolution = resolution;
        this.pos = pos;

        this.vectors = new Vector[(resolution + 1) * (resolution + 1)];
        this.c = new Color[(resolution + 1) * (resolution + 1)];

        for (int N = 0; N < resolution + 1; N++) {
            double lat = G3D.map(0, resolution, N, -Math.PI, Math.PI);
            for (int M = 0; M < resolution + 1; M++) {

                double lon = G3D.map(0, resolution, M, -Math.PI / 2, Math.PI / 2);

                int index = N * (resolution + 1) + M;

                double x = radius * Math.sin(lat) * Math.cos(lon) + pos.x;
                double y = radius * Math.sin(lat) * Math.sin(lon) + pos.y;
                double z = radius * Math.cos(lat) + pos.z;
                vectors[index] = new Vector(x, y, z);
                float rand = (float) Math.random();
                c[index] = new Color(rand, rand, rand, 1.0f);
            }
        }

    }

    @Override
    public void update(int deltaTime) {

    }

    @Override
    public void draw(G3D g3d, Vector[] v) {

        for (int i = 0; i < resolution; i++) {

            for (int j = 0; j < resolution + 1; j++) {
                int index = i * (resolution + 1) + j;
                if (zCheck(v[index]) && zCheck(v[index + resolution]) && zCheck(v[index + 1])
                        && zCheck(v[index + resolution + 1])) {
                    GeneralPath gp = new GeneralPath();
                    gp.moveTo(v[index].x, v[index].y);
                    gp.lineTo(v[index + resolution].x, v[index + resolution].y);
                    gp.lineTo(v[index + resolution + 1].x, v[index + resolution + 1].y);
                    gp.lineTo(v[index + 1].x, v[index + 1].y);
                    gp.closePath();

                    g3d.getGraphics().draw(gp);

                }

            }

        }

    }

    @Override
    public void fill(G3D g3d, Vector[] v) {

        ArrayList<Gpsort> zsa = new ArrayList<>();
        for (int i = 0; i < resolution; i++) {

            for (int j = 0; j < resolution + 1; j++) {
                int index = i * (resolution + 1) + j;
                if (zCheck(v[index]) && zCheck(v[index + resolution]) && zCheck(v[index + 1])
                        && zCheck(v[index + resolution + 1])) {
                    GeneralPath gp = new GeneralPath();
                    gp.moveTo(v[index].x, v[index].y);
                    gp.lineTo(v[index + resolution].x, v[index + resolution].y);
                    gp.lineTo(v[index + resolution + 1].x, v[index + resolution + 1].y);
                    gp.lineTo(v[index + 1].x, v[index + 1].y);
                    gp.closePath();

                    zsa.add(new Gpsort(gp,
                            ((v[index].z + v[index + 1].z + v[index + resolution].z + v[index + resolution + 1].z)
                                    / 4),
                            c[index]));

                }

            }

        }

        zsa.sort(new Comparator<Gpsort>() {
            @Override
            public int compare(Sphere.Gpsort o1, Sphere.Gpsort o2) {

                if (o1.z > o2.z) {
                    return -1;
                } else if (o1.z < o2.z) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });

        for (Gpsort gpsort : zsa) {
            g3d.getGraphics().setColor(gpsort.c);
            g3d.getGraphics().fill(gpsort.gp);
        }

        zsa.clear();
    }

    private class Gpsort {
        GeneralPath gp;
        double z;
        Color c;

        Gpsort(GeneralPath gp, double z, Color c) {
            this.gp = gp;
            this.z = z;
            this.c = c;
        }
    }

}
