
public abstract class DrawableObject {

    protected Vector[] vectors;

    protected Vector pos;

    public Vector getPos() {
        return pos;
    }

    public Vector[] getVectors() {
        return vectors;
    }

    public Vector getPos(int i) {
        return vectors[i];
    }

    public void setPos(int i, Vector v) {
        vectors[i] = v;
    }

    public abstract void update(int deltaTime);

    public abstract void draw(G3D g3d, Vector[] v);

    public abstract void fill(G3D g3d, Vector[] v);

    public boolean zCheck(Vector v) {
        return v.z > 0 && v.z < 1;
    }

}
