package com.example.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import bsh.EvalError;
import bsh.Interpreter;


public class MainActivity extends AppCompatActivity {

    private TextView calculo;
    private String display = "";
    private CharSequence ultimoNumero = "";
    private int posicaoUltimoNumero, numero = 0;

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
        ultimoNumero = b.getText();
        posicaoUltimoNumero = display.length()-1;
        updateScreen();
    }

    public void onClickOperator(View v){
        if(display == "") return;

        Button b = (Button) v;
        char ultimoChar = display.charAt(display.length()-1);

        if (isOperator(ultimoChar)){
            display = display.substring(0, display.length() - 1) + b.getText();
        }else{
            display += b.getText();
        }
        updateScreen();
}

    public void onClickUndo(View v){
        if(display.length()>0){
            display = display.substring(0, display.length() - 1);
            if((display.length() > 1 && isOperator(display.charAt(display.length()-1)) == false)){
                ultimoNumero = String.valueOf(display.charAt(display.length()-1));
                posicaoUltimoNumero = display.length()-1;
            }else {
                if (display.length() > 1){
                    ultimoNumero = String.valueOf(display.charAt(display.length()-2));
                    posicaoUltimoNumero = display.length()-2;
                }
            }
            Toast.makeText(this, display, Toast.LENGTH_SHORT).show();
            updateScreen();
        }
    }
    public void onClickSignal(View v){
        if (display.length()==0) return;

        if (numero >= 0 || (numero * -1 != Integer.parseInt(ultimoNumero.toString()))){
            numero = Integer.parseInt(ultimoNumero.toString());
        }

        numero = numero * -1;

        if (posicaoUltimoNumero == display.length()-1){
            inverterUltimoNumero(numero);
        }else{
            char ultimoChar = display.charAt(display.length()-1);
            inverterUltimoChar(numero, ultimoChar);
        }
        updateScreen();
    }

    public void inverterUltimoNumero(int numero) {

        if (display.length() == 1) {
            display = Integer.toString(numero);
            posicaoUltimoNumero++;
        } else {
            if (display.substring(posicaoUltimoNumero - 1).contains("-")) {
                if (display.length() == 2) {
                    display = Integer.toString(numero);
                } else {
                    display = display.substring(0, posicaoUltimoNumero - 1) + ultimoNumero;
                }
                posicaoUltimoNumero--;
            } else {
                if (posicaoUltimoNumero == 1) {
                    display = display.charAt(0) + Integer.toString(numero);
                } else {
                    display = display.substring(0, posicaoUltimoNumero) + numero;
                }
            posicaoUltimoNumero++;
            }
        }
    }


    public void inverterUltimoChar(int numero, char ultimoChar){

        if (display.length() == 2) {
            display = Integer.toString(numero) + ultimoChar;
            posicaoUltimoNumero++;
        }else{
            if (display.substring(posicaoUltimoNumero - 1).contains("-")) {
                if (display.length() == 3 ) {
                display = Integer.toString(numero) + ultimoChar;
            }else{
                    display = display.substring(0, posicaoUltimoNumero - 1) + ultimoNumero + ultimoChar;
                }
                posicaoUltimoNumero--;
            } else {
                if (posicaoUltimoNumero == 1) {
                    display = display.charAt(0) + Integer.toString(numero) + ultimoChar;
                } else {
                    display = display.substring(0, posicaoUltimoNumero) + numero + ultimoChar;
                }
                posicaoUltimoNumero++;
            }
        }
    }

    public boolean isOperator(char op){
        switch (op){
            case '%':
            case '+':
            case '-':
            case 'x':
            case '/':return true;
            default: return false;
        }
    }

    public void onClickEqual(View view) {
        try {
            Interpreter i = new Interpreter();

            display = display.replaceAll("x", "*");
           /* if (display.contains(".") == false){

            }*/
            if (display.contains("%")){
                int posicaoPercent = display.indexOf("%");

                String antesPercent = display.substring(0, posicaoPercent);
                String depoisPercent = display.substring(posicaoPercent+1);
                display = "(" + antesPercent + "*" + depoisPercent + ") / 100";
            }

            i.eval("result = " + display);
            display = i.get("result").toString();
            updateScreen();

        }catch (EvalError e){
            e.printStackTrace();
        }
    }
/*
    public String numeroAntes(String display){
        String a [] = new String[] {display};
        int posicao = 0;
        for (int i = 0; i < display.length(); i++){
            String c = String.valueOf(i);

            if(c == "&" || c == "*" || c == "-" || c == "" || c == "÷" || c == "+"){
                posicao = i;
                break;
            }
        }
        return display.substring(0, posicao);
    }*/
}
