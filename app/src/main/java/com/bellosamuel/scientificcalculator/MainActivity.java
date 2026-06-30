package com.bellosamuel.scientificcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvExpression, tvResult;
    private StringBuilder currentInput = new StringBuilder();
    private String operator = "";
    private double firstValue = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        int[] ids = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, 
            R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnAdd, R.id.btnSub, 
            R.id.btnMul, R.id.btnDiv, R.id.btnEqual, R.id.btnAC, R.id.btnExit,
            R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnLog, R.id.btnLn,
            R.id.btnSqrt, R.id.btnSq, R.id.btnPow, R.id.btnFact, R.id.btnPerc,
            R.id.btnSinh, R.id.btnCosh, R.id.btnTanh, R.id.btnNPr, R.id.btnNCr,
            R.id.btnMatrix, R.id.btnStats
        };
        for (int id : ids) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id >= R.id.btn0 && id <= R.id.btn9 || id == R.id.btnDot) {
            currentInput.append(((Button)v).getText().toString());
            tvResult.setText(currentInput.toString());
        } else if (id == R.id.btnAC) {
            currentInput.setLength(0);
            firstValue = Double.NaN;
            operator = "";
            tvResult.setText("0");
            tvExpression.setText("");
        } else if (id == R.id.btnExit) {
            finish();
        } else if (id == R.id.btnEqual) {
            calculateResult();
        } else if (id == R.id.btnMatrix) {
            showMatrixDialog();
        } else if (id == R.id.btnStats) {
            showStatsDialog();
        } else if (isSingleOp(id)) {
            handleSingleOperation(id);
        } else {
            handleBinaryOperator(((Button)v).getText().toString());
        }
    }

    private boolean isSingleOp(int id) {
        return id == R.id.btnSin || id == R.id.btnCos || id == R.id.btnTan ||
               id == R.id.btnLog || id == R.id.btnLn || id == R.id.btnSqrt ||
               id == R.id.btnSq || id == R.id.btnFact || id == R.id.btnSinh ||
               id == R.id.btnCosh || id == R.id.btnTanh;
    }

    private void handleBinaryOperator(String op) {
        if (currentInput.length() > 0) {
            firstValue = Double.parseDouble(currentInput.toString());
            operator = op;
            tvExpression.setText(firstValue + " " + operator);
            currentInput.setLength(0);
        }
    }

    private void handleSingleOperation(int id) {
        if (currentInput.length() == 0) return;
        double val = Double.parseDouble(currentInput.toString());
        double res = 0;
        if (id == R.id.btnSin) res = Math.sin(Math.toRadians(val));
        else if (id == R.id.btnCos) res = Math.cos(Math.toRadians(val));
        else if (id == R.id.btnTan) res = Math.tan(Math.toRadians(val));
        else if (id == R.id.btnLog) res = Math.log10(val);
        else if (id == R.id.btnLn) res = Math.log(val);
        else if (id == R.id.btnSqrt) res = Math.sqrt(val);
        else if (id == R.id.btnSq) res = val * val;
        else if (id == R.id.btnFact) res = ScientificLogic.factorial(val);
        else if (id == R.id.btnSinh) res = Math.sinh(val);
        else if (id == R.id.btnCosh) res = Math.cosh(val);
        else if (id == R.id.btnTanh) res = Math.tanh(val);
        tvResult.setText(String.valueOf(res));
        currentInput.setLength(0);
        currentInput.append(res);
    }

    private void calculateResult() {
        if (Double.isNaN(firstValue) || currentInput.length() == 0) return;
        double secondValue = Double.parseDouble(currentInput.toString());
        double result = 0;
        switch (operator) {
            case "+": result = firstValue + secondValue; break;
            case "-": result = firstValue - secondValue; break;
            case "*": result = firstValue * secondValue; break;
            case "/": result = firstValue / secondValue; break;
            case "x^y": result = Math.pow(firstValue, secondValue); break;
            case "%": result = (firstValue / 100) * secondValue; break;
            case "nPr": result = ScientificLogic.nPr(firstValue, secondValue); break;
            case "nCr": result = ScientificLogic.nCr(firstValue, secondValue); break;
        }
        tvResult.setText(String.valueOf(result));
        tvExpression.setText("");
        currentInput.setLength(0);
        currentInput.append(result);
        firstValue = Double.NaN;
    }

    private void showStatsDialog() {
        final EditText input = new EditText(this);
        input.setHint("Numbers separated by commas");
        new AlertDialog.Builder(this).setTitle("Statistics").setView(input)
            .setPositiveButton("Calculate", (d, w) -> {
                try {
                    String[] parts = input.getText().toString().split(",");
                    List<Double> data = new ArrayList<>();
                    for (String p : parts) data.add(Double.parseDouble(p.trim()));
                    String res = "Mean: " + ScientificLogic.calculateMean(data) +
                                 "\nMedian: " + ScientificLogic.calculateMedian(data) +
                                 "\nStdDev: " + ScientificLogic.calculateStdDev(data);
                    new AlertDialog.Builder(this).setMessage(res).show();
                } catch (Exception e) { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show(); }
            }).show();
    }

    private void showMatrixDialog() {
        final EditText input = new EditText(this);
        input.setHint("e.g. 1,2,3,4 (for 2x2)");
        new AlertDialog.Builder(this).setTitle("Matrix Determinant").setView(input)
            .setPositiveButton("Solve", (d, w) -> {
                try {
                    String[] parts = input.getText().toString().split(",");
                    int n = (int) Math.sqrt(parts.length);
                    double[][] matrix = new double[n][n];
                    int k = 0;
                    for(int i=0; i<n; i++) for(int j=0; j<n; j++) matrix[i][j] = Double.parseDouble(parts[k++].trim());
                    tvResult.setText("Det: " + ScientificLogic.determinant(matrix));
                } catch (Exception e) { Toast.makeText(this, "Size error", Toast.LENGTH_SHORT).show(); }
            }).show();
    }
}