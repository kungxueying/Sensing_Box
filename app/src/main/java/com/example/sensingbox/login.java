package com.example.sensingbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {

    private boolean login_flag;

    private TextView account;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = (TextView) findViewById(R.id.account);
        password = (TextView) findViewById(R.id.password);
    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }

    public boolean login_check(String account, String password){
        if(account.equals("111")&&password.equals("111")){
            return false;
        }

        return true;
    }
    public void goTo_BlueTooth (View view){

        String txt_account = account.getText().toString();
        String txt_password = password.getText().toString();

        //login
        login_flag = login_check(txt_account,txt_password);

        if(login_flag==true){
            Intent intent = new Intent (this, Select_Bluetooth.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this)
                    .setMessage("login failed!")
                    .create()
                    .show();
        }
    }
}
