public class Scene {

    Scenes s;

    public Scene() {

        // PRESETS:
        // BOXES - VERTEX_PLANES - BOX_AND_SPHERE
        s = new Scenes(Scenes.S.VERTEX_PLANES);

    }

    public void update(int deltaTime) {
        s.update(deltaTime);
    }

    public void render(G3D g) {

        s.render(g);

    }

}
