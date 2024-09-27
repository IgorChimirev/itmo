       
public class sed1{
    public static double first(double r, int t) {
        double k;
        if (t == 1) {
            k =  Math.asin(Math.cos(Math.pow(Math.E,2 * r)));
            return k;
        }
        if (t == 2) {
            k = Math.atan(0.1 * (1/(Math.pow(Math.E,Math.abs(r)))));;
            return k;
        }
        if (t == 3) {
            k = Math.pow((0.75*(((1/3) * Math.pow(Math.sin(Math.pow(((0.5)/(r-1)),2)),3) + 1))),Math.pow((Math.pow(Math.pow(((r + 1)/2)/3,3),Math.pow(r,0.25*r)))/2,3));
            return k;
        }
        return Double.NaN;
    }
    public static void printMatrix(double[][] y) {
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[i].length; j++) {
                System.out.printf("%8.3f ",y[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        long[] w = new long[9];
        int q = 4;
        for (int i = 0; i < w.length; i++){
            w[i] = q;
            q = q + 2;
        }

        
        for (long n : w) {
            System.out.print(n + " ");
        }
        System.out.println();
        float [] w2 = new float[13];
        for (int i = 0;i < w2.length;i++){

            w2[i] = (float)(-3.0  + (Math.random() * 5.0));

        }
        for (float n : w2) {
            System.out.print(n + " ");
        }
        double[][] w3 = new double[9][13];
        for (int i = 0; i < 9; i ++ ){
            for (int j = 0; j < 13; j ++){
                if (w[i] == 4){
                    w3[i][j] = first(w2[j], 1);
                    continue;
                }
                if (w[i] == 10 | w[i] == 12 | w[i] == 14 | w[i] == 20){
                    w3[i][j] = first(w2[j], 2);
                    continue;
                }
                w3[i][j] = first(w2[j], 3);
            }
        }
        printMatrix(w3);
    }
}
