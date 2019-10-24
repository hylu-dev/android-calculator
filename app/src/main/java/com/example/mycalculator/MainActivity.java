package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String MAX_MSG = "Max length reached, press clear or undo";

    String undoStr = "";
    String redoStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     * This function reads from a string and interprets the contained expression
     * and returns the result
     *
     * @param str The string to be interpreted
     *
     * @return The Double result
     */
    private Double evaluate(String str) {
        if (str.equals("")) {
            return 0D;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '+') {
                return evaluate(str.substring(0, i)) + evaluate(str.substring(i+1, str.length()));
            }
            if (str.charAt(i) == '-') {
                return evaluate(str.substring(0, i)) - evaluate(str.substring(i+1, str.length()));
            }
            if (str.charAt(i) == '×') {
                return evaluate(str.substring(0, i)) * evaluate(str.substring(i+1, str.length()));
            }
            if (str.charAt(i) == '÷') {
                return evaluate(str.substring(0, i)) / evaluate(str.substring(i+1, str.length()));
            }
        }
        return Double.parseDouble(str);
    }

    /*
     * This function undo's the latest calculator command upon button press
     * and reverts to the previous state of the textview
     *
     * @param v The view of the attached button
     */
    public void undo(View v) {
        TextView t1 = findViewById(R.id.textview);
        String newResult = "";
        int resultIndex = 0;
        if (undoStr.isEmpty()) {
            return;
        }
        // remove the latest symbol or last result from undo stack
        if (undoStr.endsWith(")")) {
            redoStr = undoStr.substring(undoStr.lastIndexOf("(")) + redoStr;
            undoStr = undoStr.substring(0, undoStr.lastIndexOf("("));
        } else {
            redoStr = undoStr.substring(undoStr.length()-1) + redoStr;
            undoStr = undoStr.substring(0, undoStr.length()-1);
        }

        for (int i = 0; i < undoStr.length(); i++) {
            if (undoStr.charAt(i) == 'c') {
                newResult = "";
            }
            else if (undoStr.charAt(i) == '(') {
                resultIndex = i;
            }
            else if (undoStr.charAt(i) == ')') {
                newResult = undoStr.substring(resultIndex+1, i);
            } else {
                newResult = newResult.concat(undoStr.substring(i, i+1));
            }
        }
        t1.setText(newResult);
    }

    /*
     * This function redo's the latest calculator command upon button press
     *
     * @param v The view of the attached button
     */
    public void redo(View v) {
        TextView t1 = findViewById(R.id.textview);
        String newResult = t1.getText().toString();

        if (redoStr.isEmpty()) {
            return;
        }
        if (redoStr.startsWith("(")) {
            int resultIndex = redoStr.indexOf(")") + 1;
            undoStr = undoStr.concat(redoStr.substring(0, resultIndex));
            newResult = redoStr.substring(1, resultIndex - 1);
            redoStr = redoStr.substring(resultIndex);
        } else if (redoStr.startsWith("c")) {
            undoStr = undoStr.concat(redoStr.substring(0, 1));
            newResult = "";
            redoStr = redoStr.substring(1);
        } else {
            undoStr = undoStr.concat(redoStr.substring(0, 1));
            newResult = newResult + redoStr.substring(0, 1);
            redoStr = redoStr.substring(1);
        }
        t1.setText(newResult);
    }

    /*
     * This function empties the textview
     *
     * @param v The view of the attached button
     */
    public void clear(View v) {
        TextView t1 = findViewById(R.id.textview);
        if (!(undoStr.endsWith("c"))) {
            undoStr = undoStr.concat("c");
        }
        t1.setText("");
    }

    /*
     * This function evaluates the textview upon button press
     *
     * @param v The view of the attached button
     */
    public void call_eval(View v) {
        TextView t1 = findViewById(R.id.textview);
        String expr = t1.getText().toString();
        String result = Double.toString(evaluate(expr));
        undoStr = undoStr.concat("(" + result + ")");
        t1.setText(result);
    }

    /*
     * This function enters a symbol to the textview corresponding to the button pressed
     *
     * @param v The view of the attached button
     */
    public void enterSymbol(View v) {
        TextView t1 = findViewById(R.id.textview);
        String b = ((Button)v).getText().toString();
        redoStr = "";
        if (t1.getText().length() < 36) {
            undoStr = undoStr.concat(b);
            t1.append(b);
        } else {
            t1.setText(MAX_MSG);
        }
    }

}
