public class Matrix {

    public static double[][] matmul4(double[][] a, double[][] b) {
        double[][] res = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    res[i][j] += (a[i][k] * b[k][j]);
                }
            }
        }
        return res;
    }

    public static Vector matmulV4(double[][] a, Vector v) {
        return new Vector(a[0][0] * v.x + a[0][1] * v.y + a[0][2] * v.z + a[0][3] * v.w,
                a[1][0] * v.x + a[1][1] * v.y + a[1][2] * v.z + a[1][3] * v.w,
                a[2][0] * v.x + a[2][1] * v.y + a[2][2] * v.z + a[2][3] * v.w,
                a[3][0] * v.x + a[3][1] * v.y + a[3][2] * v.z + a[3][3] * v.w);
    }

    public static double[][] getDefaultMatrix() {
        double[][] k = {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };
        return k;
    }

}
