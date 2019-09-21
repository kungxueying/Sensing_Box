package com.example.sensingbox;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {

    private boolean login_flag;
    private Dialog dialog;

    private TextView account;
    private TextView password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = (TextView) findViewById(R.id.account);
        password = (TextView) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.login);

        String txt_account = account.getText().toString();
        String txt_password = password.getText().toString();

        //login
        login_flag = login_func(txt_account,txt_password);
    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }

    public boolean login_func(String account, String password){


        return false;
    }
    public void goTo_Main_Screen (View view){
        if(login_flag==true){
            Intent intent = new Intent (this, main_screen.class);
            startActivity(intent);
        }else{
            dialog = new Dialog(this);
            dialog.setTitle("login failed!");
        }
    }
}
