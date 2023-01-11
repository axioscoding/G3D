import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;

public class Scenes {

    VertexPlane v1, v2;

    double noiseDelta = 0;
    double noiseScalar = 0.001; // 0.002
    double noiseMultiplier = 160;
    double noiseSpeed = 0.06; // 0.01

    double minNoise = 0, maxNoise = 0;

    double val = 0;

    int density = 60;

    double width = 4000, depth = 4000, height = 400;

    double rotX = 0;
    double delta = 0.1;

    int floorDim = 50;
    double boxSize = 20;
    Box[] boxes = new Box[floorDim * floorDim + 1];

    Box b = new Box(new Vector(-60, -60, 400), new Vector(50, 50, 50));

    Sphere s = new Sphere(new Vector(0, 0, 500), 100, 30);

    Box b1 = new Box(new Vector(-300, -60, 0), new Vector(200, 200, 200));

    Box b2 = new Box(new Vector(-300, -60, 250), new Vector(200, 200, 200));

    DrawableObject[] objs = { s, b1, b2 };

    enum S {
        VERTEX_PLANES,
        BOXES,
        BOX_AND_SPHERE
    }

    private S state;

    public Scenes(S s) {
        this.state = s;
        if (s == S.VERTEX_PLANES) {
            G3D.speed = 1.5;
            G3D.rotSpeed = 0.05;
            Vector p1 = new Vector(-width, -50, depth);
            Vector p2 = new Vector(width, -50, depth);
            Vector p3 = new Vector(width, -50, -depth);
            Vector p4 = new Vector(-width, -50, -depth);

            v1 = new VertexPlane(p1, p2, p3, p4, density, density);

            p1 = new Vector(-width, -50, depth);
            p2 = new Vector(width, -50, depth);
            p3 = new Vector(width, -50, -depth);
            p4 = new Vector(-width, -50, -depth);

            v2 = new VertexPlane(p1, p2, p3, p4, density, density);
        } else if (s == S.BOXES) {
            G3D.speed = 1;
            G3D.rotSpeed = 0.05;
            for (int i = 0; i < floorDim; i++) {
                for (int j = 0; j < floorDim; j++) {

                    int index = i * floorDim + j;
                    Vector pos = new Vector(boxSize * i - (floorDim * boxSize / 2) + boxSize * i, 100,
                            boxSize * j - (floorDim * boxSize / 2) + boxSize * j);
                    Vector dims = new Vector(boxSize, boxSize, boxSize);
                    boxes[index] = new Box(pos, dims);
                }
            }
            boxes[boxes.length - 1] = b;
        } else if (s == S.BOX_AND_SPHERE) {
            G3D.speed = 0.1;
            G3D.rotSpeed = 0.01;
        }
    }

    public void render(G3D g) {
        if (state == S.VERTEX_PLANES) {
            g.translate(new Vector(Window.WIDTH / 2, Window.HEIGHT / 2));
            g.scale(.1);
            g.fill(v1);
            g.fill(v2);
        } else if (state == S.BOXES) {
            g.setColor(Color.WHITE);

            Vector center = new Vector(Window.WIDTH / 2, Window.HEIGHT / 2);
            // delta *= Math.random() > 0.01 ? 1 : -1;
            g.translate(center);
            g.scale(.1);
            // g.rotateX(-Math.PI / 6);

            // g.rotateZ(rotX);

            for (int i = 0; i < floorDim; i++) {
                for (int j = 0; j < floorDim; j++) {
                    int index = i * floorDim + j;
                    // g.rotateY(rotX, boxes[index].pos);

                }
            }

            g.fill(boxes);

            // g.rotateY(rotX, b.pos);
            // g.fill(b);

            // g.box(new Vector(0, 0, 500), new Vector(50, 50, 50));
            rotX += delta;
        } else if (state == S.BOX_AND_SPHERE) {
            g.translate(new Vector(Window.WIDTH / 2, Window.HEIGHT / 2));
            g.scale(.1);

            g.setColor(Color.WHITE);
            g.fill(objs);
        }

    }

    public void update(int deltaTime) {
        if (state == S.VERTEX_PLANES) {
            for (int i = 0; i < density; i++) {
                for (int j = 0; j < density; j++) {
                    int index = i * density + j;

                    Vector pos = v1.getPos(index);
                    double noise = Noise.noise(pos.x * noiseScalar, noiseScalar, pos.z * noiseScalar + noiseDelta + 10)
                            * noiseMultiplier;

                    float col = (float) G3D.map(-noiseMultiplier, noiseMultiplier, noise, 0, 1);
                    float pCol = (float) G3D.map(depth, 0, pos.z, 0, 1);
                    float pCol2 = (float) G3D.map(-depth, 0, pos.z, 0, 1);
                    float pCol3 = (float) G3D.map(width, 0, pos.x, 0, 1);
                    float pCol4 = (float) G3D.map(-width, 0, pos.x, 0, 1);
                    float pColMin = Math.min(pCol, pCol2);
                    float pColMin2 = Math.min(pCol3, pCol4);
                    float pColMinFull = Math.min(pColMin, pColMin2);

                    v1.setColor(index,
                            Color.getHSBColor((float) G3D.map(-noiseMultiplier, noiseMultiplier, noise, 1, 0),
                                    0.7f, Math.min(pColMinFull, col)));

                    v1.setPos(index, new Vector(pos.x, height - noise, pos.z));

                    pos = v2.getPos(index);
                    noise = Noise.noise(pos.x * noiseScalar, noiseScalar, pos.z * noiseScalar + noiseDelta + 10)
                            * noiseMultiplier;

                    col = (float) G3D.map(-noiseMultiplier, noiseMultiplier, noise, 1, 0);
                    pCol = (float) G3D.map(depth, 0, pos.z, 0, 1);
                    pCol2 = (float) G3D.map(-depth, 0, pos.z, 0, 1);
                    pCol3 = (float) G3D.map(width, 0, pos.x, 0, 1);
                    pCol4 = (float) G3D.map(-width, 0, pos.x, 0, 1);
                    pColMin = Math.min(pCol, pCol2);
                    pColMin2 = Math.min(pCol3, pCol4);
                    pColMinFull = Math.min(pColMin, pColMin2);

                    v2.setColor(index,
                            Color.getHSBColor((float) G3D.map(-noiseMultiplier, noiseMultiplier, noise, 0, 1),
                                    0.7f, Math.min(pColMinFull, col)));

                    v2.setPos(index, new Vector(pos.x, -height - noise, pos.z));
                }
            }
            // System.out.println(maxNoise + " " + minNoise);

            noiseDelta += noiseSpeed;

        } else if (state == S.BOXES) {
            for (int i = 0; i < boxes.length - 1; i++) {
                boxes[i].addY(Math.sin(rotX + boxes[i].pos.x)
                        + Math.cos(rotX + boxes[i].pos.z) * 5);
            }
        }
    }

}
