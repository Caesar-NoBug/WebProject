

import org.apache.commons.math3.linear.*;

import java.util.Map;
import java.util.Scanner;

public class GaussianElimination {

    private static int N = 10;

    //求矩阵的F范数对应的条件数
    public static double getConditionNumber(double data[][]){
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        double[] singular = svd.getSingularValues();//获取奇异值
        double maxv = singular[0];
        double minv = singular[data.length - 1];
        double conditionNum = maxv / minv;
        return conditionNum;
    }

    public static void main(String[] args) {

        System.out.println("是否随机生成矩阵？");
        Scanner sc = new Scanner(System.in);
        String isRand;
        isRand = sc.next();

        System.out.println("请输入矩阵阶数：");
        N = sc.nextInt();

        //初始化矩阵
        //原矩阵
        double A[][] = new double[N][N];
        //增广矩阵
        double matrix[][] = new double[N][N + 1];
        //结果
        double ans[] = new double[N];

        for (int i = 0; i < N; i++) {
            if(i - 1 >= 0) A[i][i - 1] = 8;
            A[i][i] = 6;
            if(i + 1 < N) A[i][i + 1] = 1;
        }

        for (int i = 0; i < N; i++) {
            if(i - 1 >= 0) matrix[i][i - 1] = 8;
            matrix[i][i] = 6;
            if(i + 1 < N) matrix[i][i + 1] = 1;
            matrix[i][N] = 15;
        }
        matrix[0][N] = 7;
        matrix[N - 1][N] = 14;

        if("y".equals(isRand)){
            for (int i = 0; i < N; i++) {
                for (int j = 0; j <= N; j++) {
                    matrix[i][j] = Math.random();
                }
            }
        }

        System.out.println("矩阵的条件数为：" + getConditionNumber(A));
        //处理输入
        String op;

        System.out.println("是否手动选择主元?(y为是，n为否)");
        op = sc.next();
        //消元
        for (int i = 0; i < N; i++) {
            double max = -2e9;
            int posi = 0;
            //手动选取主元
            if("y".equals(op)){
                System.out.println("可供选择的主元有(请输入所选编号，从0开始): ");
                for (int j = i; j < N; j++)
                    System.out.print(matrix[j][i] + ", ");
                posi = sc.nextInt();
                posi += i;
                //System.out.println("当前主元：" + matrix[posi][i]);
            }
            //取第一个不为 0 的元素做主元
            else{
                for (int j = i; j < N; j++)
                    if(matrix[j][i] != 0){
                        posi = j;
                        break;
                    }

            }

            //将主元所在行与当前最高行交换
            double temp[] = matrix[i];
            matrix[i] = matrix[posi];
            matrix[posi] = temp;

            //使主元变成 1
            double first = matrix[i][i];
            for (int j = i; j <= N; j++) {
                matrix[i][j] /= first;
            }

            //消元
            for (int j = i + 1; j < N; j++) {
                double value = matrix[j][i];
                for (int k = i; k <= N; k++) {
                    matrix[j][k] -= matrix[i][k] * value;
                }
            }

        }

        //求解
        for (int i = N - 1; i >= 0; i--) {
            double x = matrix[i][N];
            for (int j = i + 1; j < N; j++) {
                x -= matrix[i][j] * ans[j];
            }
            ans[i] = x;
        }

        //输出结果
        System.out.println("矩阵的解为：");
        for (int i = 0; i < ans.length; i++) {
            System.out.println(ans[i] + ", ");
        }


    }
}
