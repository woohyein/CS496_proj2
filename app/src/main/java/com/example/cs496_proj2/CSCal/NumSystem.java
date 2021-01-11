package com.example.cs496_proj2.CSCal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_proj2.R;

public class NumSystem extends AppCompatActivity  {

    EditText decimal;
    EditText binary;
    EditText hex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_system);

        decimal = findViewById(R.id.decimal);
        binary = findViewById(R.id.binary);
        hex = findViewById(R.id.hex);

        // Get results
        Button resultbutton = (Button) findViewById(R.id.button3);
        resultbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dec = decimal.getText().toString();
                String bin = binary.getText().toString();
                String hexa = hex.getText().toString();

                if (bin.getBytes().length<=0 && hexa.getBytes().length<=0){
                    Decclicker();
                }

                else if (dec.getBytes().length<=0 && hexa.getBytes().length<=0){
                    if (isBinary(bin)) {
                        Binclicker();
                    }
                    else{
                        Toast msg = Toast.makeText(getApplicationContext(), "이진수만 입력해주세요", Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }

                else if (dec.getBytes().length<=0 && bin.getBytes().length<=0) {
                    if (isHex(hexa)) {
                        Hexclicker();
                    }
                    else {
                        Toast msg = Toast.makeText(getApplicationContext(), "16진수만 입력해주세요", Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }

                else{
                    Toast mytoast = Toast.makeText(getApplicationContext(), "세 칸 중 한 곳에만 숫자를 입력해주세요", Toast.LENGTH_LONG);
                    mytoast.show();
                }

            }
        });

        // Clear
        Button clearbutton = (Button) findViewById(R.id.button4);
        clearbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                decimal.setText(null);
                binary.setText(null);
                hex.setText(null);
            }
        });
    }

    public void Decclicker(){
        String strdec = decimal.getText().toString();
        Long num = Long.parseLong(strdec);
        String dectobin = Long.toBinaryString(num);
        String dectohex = Long.toHexString(num);
        binary.setText(dectobin);
        hex.setText(dectohex);
    }

    public void Binclicker(){
        String strbin = binary.getText().toString();
        Long num = Long.parseLong(strbin, 2);
        String bintodec = Long.toString(num);
        String bintohex = Long.toHexString(num);
        decimal.setText(bintodec);
        hex.setText(bintohex);
    }

    public void Hexclicker(){
        String strhex = hex.getText().toString();
        Long num = Long.parseLong(strhex, 16);
        String hextodec = Long.toString(num);
        String hextobin = Long.toBinaryString(num);
        decimal.setText(hextodec);
        binary.setText(hextobin);
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

    public boolean isHex(String hex) {
        int n = hex.length();
        String hexdigit = "0123456789ABCDEFabcdef";
        for (int i = 0; i < n; i++) {
            char ch = hex.charAt(i);
            if (hexdigit.indexOf(ch) == -1) return false;
        }
        return true;
    }

}
