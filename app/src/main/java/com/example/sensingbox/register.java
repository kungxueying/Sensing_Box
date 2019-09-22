package com.example.sensingbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class register extends AppCompatActivity {

    private boolean register_flag;

    private TextView account;
    private TextView password;
    private TextView user;
    private TextView box;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = (TextView) findViewById(R.id.account);
        password = (TextView) findViewById(R.id.password);
        user = (TextView) findViewById(R.id.username);
        box = (TextView) findViewById(R.id.number);

    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }

    public boolean register_check(String account, String password,String user,String box){
        if(account.equals("111")&&password.equals("111")&&user.equals("111")&&box.equals("111")){

            return true;
        }

        return false;
    }
    public void goTo_Main_Screen (View view){

        String txt_account = account.getText().toString();
        String txt_password = password.getText().toString();
        String txt_user = user.getText().toString();
        String txt_box = box.getText().toString();

        //login
        register_flag = register_check(txt_account,txt_password,txt_user,txt_box);

        if(register_flag==true){
            Intent intent = new Intent (this, main_screen.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this)
                    .setMessage("register failed!")
                    .create()
                    .show();
        }
    }
}
