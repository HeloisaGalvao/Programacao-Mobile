package com.example.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView calculo;
    private String display = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculo = findViewById(R.id.txt_calculo);
        calculo.setText(display);
    }

    private void updateScreen(){
        calculo.setText(display);
    }

    public void onClickNumber(View v){
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

}
