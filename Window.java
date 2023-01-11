import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window implements Runnable, KeyListener {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1000;

    public static final long DESIRED_FPS = 1000;

    // >=0 : limit frames
    public static final int FRAMES = -1;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private boolean running = true;

    private int count = 0;

    private int steps = 0;

    public static int fps = 0;
    private int low_fps = 1000000;
    private long fps_last = System.nanoTime();

    private long desiredDeltaLoop = (1000 * 1000 * 1000) / DESIRED_FPS;

    public static final Font arial = new Font("Arial", Font.PLAIN, 20);

    private boolean[] keyDowns = new boolean[8];

    private Scene s;
    private G3D g3d;

    public Window() {
        frame = new JFrame("Template");
        frame.setFocusable(true);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);
        canvas = new Canvas();
        canvas.addKeyListener(this);
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        panel.add(canvas);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        s = new Scene();
        g3d = new G3D();
    }

    public void run() {
        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        while (running) {
            beginLoopTime = System.nanoTime();

            render();

            lastUpdateTime = currentUpdateTime;
            currentUpdateTime = System.nanoTime();

            update((int) ((currentUpdateTime - lastUpdateTime) / (1000 * 1000)));

            endLoopTime = System.nanoTime();
            deltaLoop = endLoopTime - beginLoopTime;

            if (deltaLoop <= desiredDeltaLoop) {
                try {
                    Thread.sleep((desiredDeltaLoop - deltaLoop) / (1000 * 1000));
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
            if (System.nanoTime() - fps_last > 50000000) {
                fps = (1000 * 1000 * 1000) / (int) (System.nanoTime() - beginLoopTime);
                fps_last = System.nanoTime();
                if (fps < low_fps && steps > 2)
                    low_fps = fps;
            }

            if (FRAMES == count)
                running = false;
            if (FRAMES >= 0)
                count++;

            steps++;
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g3d.setGraphics(g);

        // background every frame
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        render(g3d);

        g.setFont(arial);
        g.setColor(Color.CYAN);
        g.drawString("FPS: " + fps + "", 10, 80);
        g.setColor(Color.RED);
        g.drawString("LowesFps: " + low_fps + "", 10, 110);
        g.setColor(Color.WHITE);
        g.drawString(
                "X: " + Math.round(g3d.CAMERA_POSITION.x) + "  Y: " + Math.round(g3d.CAMERA_POSITION.y) + "  Z: "
                        + Math.round(g3d.CAMERA_POSITION.z),
                10, 140);
        g.drawString(
                "x" + Math.round((g3d.facing.x * 180 / Math.PI) % 360) + " y"
                        + Math.round((g3d.facing.y * 180 / Math.PI) % 360) + " z"
                        + Math.round((g3d.facing.z * 180 / Math.PI) % 360),
                10, 170);

        g.dispose();
        bufferStrategy.show();
    }

    protected void update(int deltaTime) {

        if (keyDowns[0]) {
            g3d.CAMERA_POSITION.y -= G3D.speed;
        }

        if (keyDowns[1]) {
            g3d.CAMERA_POSITION.y += G3D.speed;
        }

        if (keyDowns[2]) {
            // g3d.CAMERA_POSITION.z += 1;
            g3d.CAMERA_POSITION.z += Math.cos(g3d.facing.x) * G3D.speed;
            g3d.CAMERA_POSITION.x += Math.sin(g3d.facing.x) * G3D.speed;

        }

        if (keyDowns[3]) {
            g3d.CAMERA_POSITION.z += Math.cos(g3d.facing.x - Math.PI / 2) * G3D.speed;
            g3d.CAMERA_POSITION.x += Math.sin(g3d.facing.x - Math.PI / 2) * G3D.speed;
        }

        if (keyDowns[4]) {
            g3d.CAMERA_POSITION.z -= Math.cos(g3d.facing.x) * G3D.speed;
            g3d.CAMERA_POSITION.x -= Math.sin(g3d.facing.x) * G3D.speed;
        }

        if (keyDowns[5]) {
            g3d.CAMERA_POSITION.z -= Math.cos(g3d.facing.x - Math.PI / 2) * G3D.speed;
            g3d.CAMERA_POSITION.x -= Math.sin(g3d.facing.x - Math.PI / 2) * G3D.speed;
        }

        if (keyDowns[6]) {
            g3d.facing.x -= G3D.rotSpeed;
        }

        if (keyDowns[7]) {
            g3d.facing.x += G3D.rotSpeed;
        }

        // update/tick
        s.update(deltaTime);
    }

    protected void render(G3D g) {
        // render g
        s.render(g);
    }

    public static void main(String[] args) {
        Window ex = new Window();
        new Thread(ex).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE) {
            keyDowns[0] = true;
        }

        if (code == KeyEvent.VK_SHIFT) {
            keyDowns[1] = true;
        }

        if (code == KeyEvent.VK_W) {
            keyDowns[2] = true;

        }

        if (code == KeyEvent.VK_A) {
            keyDowns[3] = true;
        }

        if (code == KeyEvent.VK_S) {
            keyDowns[4] = true;
        }

        if (code == KeyEvent.VK_D) {
            keyDowns[5] = true;
        }

        if (code == KeyEvent.VK_Q) {
            keyDowns[6] = true;
        }

        if (code == KeyEvent.VK_E) {
            keyDowns[7] = true;
        }

        if (code == KeyEvent.VK_R) {
            g3d.facing.y -= 0.1;
        }

        if (code == KeyEvent.VK_F) {
            g3d.facing.y += 0.1;
        }

        if (code == KeyEvent.VK_C) {
            g3d.facing.z -= 0.1;
        }

        if (code == KeyEvent.VK_V) {
            g3d.facing.z += 0.1;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE) {
            keyDowns[0] = false;
        }

        if (code == KeyEvent.VK_SHIFT) {
            keyDowns[1] = false;
        }

        if (code == KeyEvent.VK_W) {
            keyDowns[2] = false;

        }

        if (code == KeyEvent.VK_A) {
            keyDowns[3] = false;
        }

        if (code == KeyEvent.VK_S) {
            keyDowns[4] = false;
        }

        if (code == KeyEvent.VK_D) {
            keyDowns[5] = false;
        }

        if (code == KeyEvent.VK_Q) {
            keyDowns[6] = false;
        }

        if (code == KeyEvent.VK_E) {
            keyDowns[7] = false;
        }

    }

}