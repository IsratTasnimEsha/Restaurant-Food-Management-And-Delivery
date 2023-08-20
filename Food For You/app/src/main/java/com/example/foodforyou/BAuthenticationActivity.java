package com.example.foodforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BAuthenticationActivity extends AppCompatActivity {
    EditText bauth_code1, bauth_code2, bauth_code3, bauth_code4;
    TextView bauth_phone, bauth_get;
    Button bauth_verify;
    boolean resendEnabled=false;
    int resendTimer=60, selectedPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bauthentication);

        bauth_code1=findViewById(R.id.bauth_code1);
        bauth_code2=findViewById(R.id.bauth_code2);
        bauth_code3=findViewById(R.id.bauth_code3);
        bauth_code4=findViewById(R.id.bauth_code4);
        bauth_phone=findViewById(R.id.bauth_phone);
        bauth_get=findViewById(R.id.bauth_get);
        bauth_verify=findViewById(R.id.bauth_verify);

        String st_bphone=getIntent().getStringExtra("BPhone");
        bauth_phone.setText(st_bphone);

        bauth_code1.addTextChangedListener(textWatcher);
        bauth_code2.addTextChangedListener(textWatcher);
        bauth_code3.addTextChangedListener(textWatcher);
        bauth_code4.addTextChangedListener(textWatcher);

        showKeyBoard(bauth_code1);
        startCountDownTimer();

        bauth_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendEnabled) {
                    startCountDownTimer();
                }
            }
        });

        bauth_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=bauth_code1.getText().toString() + bauth_code2.getText().toString() +
                        bauth_code3.getText().toString() + bauth_code4.getText().toString();
                if(otp.length()==4) {

                }
            }
        });
    }

    private void showKeyBoard(EditText code) {
        code.requestFocus();
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(code, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {
        resendEnabled=false;
        bauth_get.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTimer*1000, 1000) {

            @Override
            public void onTick(long l) {
                bauth_get.setText("Resend Code (" + (l/1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled=true;
                bauth_get.setText("Resend Code");
                bauth_get.setTextColor(Color.parseColor("#000000"));
            }
        }.start();
    }

    private final TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()>0) {
                if(selectedPosition==0) {
                    selectedPosition=1;
                    showKeyBoard(bauth_code2);
                }
                else if(selectedPosition==1) {
                    selectedPosition=2;
                    showKeyBoard(bauth_code3);
                }
                else if(selectedPosition==2) {
                    selectedPosition=3;
                    showKeyBoard(bauth_code4);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DEL) {
            if(selectedPosition==3) {
                selectedPosition=2;
                showKeyBoard(bauth_code3);
            }
            else if(selectedPosition==2) {
                selectedPosition=1;
                showKeyBoard(bauth_code2);
            }
            else if(selectedPosition==1) {
                selectedPosition=0;
                showKeyBoard(bauth_code1);
            }
        }
        else {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    };
}