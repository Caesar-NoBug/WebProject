import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.util.MathArrays;

import javax.swing.*;
import java.awt.*;

public class LagrangeInterpolation extends JPanel{

    private static double x[] = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20
    };

    private static double y[] = {
            9.01, 8.96, 7.96, 7.96, 8.02, 9.05, 10.13, 11.18, 12.26, 13.28,
            13.32, 12.61, 11.29, 10.22, 9.15, 7.95, 7.95, 8.86, 9.81, 10.80, 10.93
    };

    private static int n = 20;

    private static double h = 1;

    private static double coef[][] = new double[n >> 1][3];

    //二次分段插值
    public static void main(String[] args) {

        //求光缆长度
        double length = 0;
        for (int i = 0; i < n; i++) {
            double dy = y[i + 1] - y[i];
            length += Math.sqrt(1 + dy * dy);
        }

        System.out.println(length);

        for (int i = 0; i < n >> 1; i ++) {
            int j = i << 1;
            int x0 = j;
            int x1 = j + 1;
            int x2 = j + 2;
            coef[i][0] = 0.5 * y[x0] * x[x1] * x[x2] - y[x1] * x[j] * x[x2] + 0.5 * y[x2] * x[j] * x[x1];
            coef[i][1] = -1 * (0.5 * y[x0] * (x[x1] + x[x2]) - y[x1] * (x[x0] + x[x2]) + 0.5 * y[x2] * (x[x0] + x[x1]));
            coef[i][2] = 0.5 * y[x0] - y[x1] + 0.5 * y[x2];
            /*System.out.println(y[x0] + " " + y[x1] + " " + y[x2]);
            System.out.println((coef[i][0] + coef[i][1] * x0 + coef[i][2] * x0 * x0) + " " +
                    (coef[i][0] + coef[i][1] * x1 + coef[i][2] * x1 * x1) + " " +
                    (coef[i][0] + coef[i][1] * x2 + coef[i][2] * x2 * x2));*/
        }

        //绘制图片
        JFrame frame = new JFrame("");
        frame.add(new LagrangeInterpolation());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //计算近似值
    private double interpolate(double x){
        int i = (int) x / 2;
        return coef[i][0] + coef[i][1] * x + coef[i][2] * x * x;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 25;
        int w = getWidth();
        int h = getHeight();
        int xMin = 0;
        int xMax = 20;
        int yMin = 0;
        int yMax = 15;
        int xInc = 1;
        int yInc = 1;

        g.setColor(Color.BLACK);
        g.drawLine(padding, h-padding, padding, padding);
        g.drawLine(padding, h-padding, w-padding, h-padding);
        g.setColor(Color.GRAY);

        //绘制坐标轴
        for (int x = xMin+xInc; x <= xMax; x += xInc) {
            int xVal = padding + (x - xMin) * (w - 2 * padding) / (xMax - xMin);
            g.drawLine(xVal, h-padding, xVal, h-padding+5);
            g.drawString(String.valueOf(x), xVal-3, h-padding+18);
        }

        for (int y = yMin+yInc; y <= yMax; y += yInc) {
            int yVal = h - padding - (y - yMin) * (h - 2 * padding) / (yMax - yMin);
            g.drawLine(padding, yVal, padding-5, yVal);
            g.drawString(String.valueOf(y), padding-23, yVal+3);
        }

        g2.setColor(Color.RED);

        //绘制曲线
        for (double i = 0; i < 19.9; i += 0.01){
            double ty = interpolate(i);
            System.out.println(interpolate(i));
            g2.drawLine((int) getX(i), (int)getY(interpolate(i)), (int)getX(i + 0.01),(int) getY(interpolate(i + 0.01)));
        }
    }

    private double getX(double x){
        return 25 + x * (getWidth() - 2 * 25) / 20;
    }

    private double getY(double y){
        return getHeight() - 25 - y * (getHeight() - 2 * 25) / 15;
    }
}
