package com.bellosamuel.scientificcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScientificLogic {

    public static double factorial(double n) {
        if (n < 0) return Double.NaN;
        if (n == 0) return 1;
        double fact = 1;
        for (int i = 1; i <= n; i++) fact *= i;
        return fact;
    }

    public static double nPr(double n, double r) {
        if (n < r) return 0;
        return factorial(n) / factorial(n - r);
    }

    public static double nCr(double n, double r) {
        if (n < r) return 0;
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    // Statistics logic
    public static double calculateMean(List<Double> data) {
        double sum = 0;
        for (double d : data) sum += d;
        return sum / data.size();
    }

    public static double calculateMedian(List<Double> data) {
        Collections.sort(data);
        int size = data.size();
        if (size % 2 == 0) return (data.get(size / 2) + data.get(size / 2 - 1)) / 2;
        else return data.get(size / 2);
    }

    public static String calculateMode(List<Double> data) {
        Map<Double, Integer> counts = new HashMap<>();
        for (double d : data) counts.put(d, counts.getOrDefault(d, 0) + 1);
        int max = Collections.max(counts.values());
        if (max == 1) return "No unique mode";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == max) sb.append(entry.getKey()).append(" ");
        }
        return sb.toString();
    }

    public static double calculateVariance(List<Double> data) {
        double mean = calculateMean(data);
        double temp = 0;
        for (double d : data) temp += (d - mean) * (d - mean);
        return temp / data.size();
    }

    public static double calculateStdDev(List<Double> data) {
        return Math.sqrt(calculateVariance(data));
    }

    // Matrix Logic
    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        double det = 0;
        for (int i = 0; i < n; i++) {
            det += Math.pow(-1, i) * matrix[0][i] * determinant(getSubmatrix(matrix, 0, i));
        }
        return det;
    }

    private static double[][] getSubmatrix(double[][] matrix, int row, int col) {
        int n = matrix.length;
        double[][] res = new double[n - 1][n - 1];
        int r = -1;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            r++;
            int c = -1;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                res[r][++c] = matrix[i][j];
            }
        }
        return res;
    }
    
    public static String transpose(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}