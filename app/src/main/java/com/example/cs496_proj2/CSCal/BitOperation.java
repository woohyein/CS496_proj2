package com.example.cs496_proj2.CSCal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_proj2.R;

import java.util.ArrayList;

public class BitOperation extends AppCompatActivity {
    private Button andOp, orOp, xorOp, notOp, leftOp, rightOp, equalOp, clear;
    private int AND=1, OR=2, XOR=3, NOT=4, LEFT=5, RIGHT=6;
    private Button zero, one, two, three, four, five, six, seven, eight, nine;
    private EditText input, output;
    private String[] operand;
    private ArrayList<Integer> operator = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_operation);

        // Set Buttons and Texts
        andOp = (Button) findViewById(R.id.andOp);
        orOp = (Button) findViewById(R.id.orOp);
        xorOp = (Button) findViewById(R.id.xorOp);
        notOp = (Button) findViewById(R.id.notOp);
        leftOp = (Button) findViewById(R.id.leftOp);
        rightOp = (Button) findViewById(R.id.rightOp);
        equalOp = (Button) findViewById(R.id.equalOp);
        clear = (Button) findViewById(R.id.clear);
        zero = (Button) findViewById(R.id.zero);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        input = (EditText) findViewById(R.id.input);
        output = (EditText) findViewById(R.id.output);

        // Init variables
        //operator.add(-1);
        operator.clear();
        operand = new String[2];
        operand[0] = new String("");
        operand[1] = new String ("");

        // Click events
        andOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(andOp.getText());
                //operator = AND;
                operator.add(AND);
            }
        });
        orOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(orOp.getText());
                //operator = OR;
                operator.add(OR);
            }
        });
        xorOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(xorOp.getText());
                //operator = XOR;
                operator.add(XOR);
            }
        });
        notOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(notOp.getText());
                //operator = NOT;
                operator.add(NOT);
            }
        });
        leftOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(leftOp.getText());
                //operator = LEFT;
                operator.add(LEFT);
            }
        });
        rightOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(rightOp.getText());
                //operator = RIGHT;
                operator.add(RIGHT);
            }
        });
        equalOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(null);
                output.setText(null);
                //operator = -1;
                operator.clear();
                operand = new String[2];
                operand[0] = new String("");
                operand[1] = new String ("");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(zero.getText());
                setOperand("0");
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(one.getText());
                setOperand("1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(two.getText());
                setOperand("2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(three.getText());
                setOperand("3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(four.getText());
                setOperand("4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(five.getText());
                setOperand("5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(six.getText());
                setOperand("6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(seven.getText());
                setOperand("7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(eight.getText());
                setOperand("8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton(nine.getText());
                setOperand("9");
            }
        });
    }

    private void clickButton (CharSequence button) {
        input.setText(input.getText().append(button));
    }

    public boolean isBinary(String bin){
        int num = Integer.parseInt(bin);
        while (num != 0){
            if (num % 10 > 1){
                return false;
            }
            num = num / 10;
        }
        return true;
    }

    private void calculate () {
        Toast msg = Toast.makeText(getApplicationContext(), "이진수만 입력해주세요", Toast.LENGTH_SHORT);
        Toast msg2 = Toast.makeText(getApplicationContext(), "이 연산은 두 개의 피연산자가 필요합니다", Toast.LENGTH_SHORT);
        Toast msg3 = Toast.makeText(getApplicationContext(), "적어도 하나의 피연산자가 필요합니다", Toast.LENGTH_SHORT);
        Toast msg4 = Toast.makeText(getApplicationContext(), "NOT은 하나의 피연산자만 필요합니다", Toast.LENGTH_SHORT);
        Toast msg5 = Toast.makeText(getApplicationContext(), "연산자 한 개만 사용해주세요", Toast.LENGTH_SHORT);

        int op2;
        int result;

        if (operator.size()==1){
            if (operand[0].equals("")) {
                // NOT operation - need only 1 operand
                if (operator.get(0) == NOT) {
                        if (isBinary(operand[1])) {
                            op2 = Integer.parseInt(operand[1], 2);
                            result = ~op2;
                            output.setText(Integer.toBinaryString(result));
                            operand = null;
                            operator.clear();
                        } else {
                            msg.show();
                            return;
                        }}

                // Other operations
                else {msg3.show(); return;}
            }

            else {
                if (isBinary(operand[0])) {
                    int op1 = Integer.parseInt(operand[0], 2);

                    if (operator.get(0) == AND) {
                        if (!operand[1].equals("")) {
                            if (isBinary(operand[1])) {
                                op2 = Integer.parseInt(operand[1], 2);
                                result = op1 & op2;
                            } else {
                                msg.show();
                                return;
                            }
                        } else {
                            msg2.show();
                            return;
                        }
                    }

                    else if (operator.get(0) == OR) {
                        if (!operand[1].equals("")) {
                            if (isBinary(operand[1])) {
                                op2 = Integer.parseInt(operand[1], 2);
                                result = op1 | op2;
                            } else {
                                msg.show();
                                return;
                            }
                        } else {msg2.show(); return;}
                    }

                    else if (operator.get(0) == NOT) {
                        if (operand[1].equals("")) {
                            result = ~op1;
                        } else {
                            msg4.show();
                            return;
                        }}

                    else if (operator.get(0) == XOR) {
                        if (!operand[1].equals("")) {
                            if (isBinary(operand[1])) {
                                op2 = Integer.parseInt(operand[1], 2);
                                result = op1 ^ op2;
                            } else {
                                msg.show();
                                return;
                            }
                        } else {msg2.show(); return;}

                    } else if (operator.get(0) == LEFT) {
                        if (!operand[1].equals("")){
                        op2 = Integer.parseInt(operand[1]);
                        result = op1 << op2;}
                        else {msg2.show(); return;}
                    }
                    else if (operator.get(0) == RIGHT) {
                        if (!operand[1].equals("")) {
                            op2 = Integer.parseInt(operand[1]);
                            result = op1 >> op2;
                        } else {
                            msg2.show();
                            return;
                        }
                    }   else {
                        // UNACCEPTED OPERATION
                        result = 0;
                    }
                    output.setText(Integer.toBinaryString(result));
                    operand = null;
                    operator.clear();
                }
                // non-binary input
                else {
                    msg.show(); return;
                }
            }
        }
        else{
            msg5.show(); return;
        }
    }

    private void setOperand (String num) {
        if (operator.size() != 0) {
            if (operand[1].equals("")) operand[1] = num;
            else operand[1] += num;
        }
        else {
            if (operand[0].equals("")) operand[0] = num;
            else operand[0] += num;
        }
    }


}