package com.example.sensingbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private boolean register_flag;

    private TextView account;
    private TextView password;
    private TextView user;
    private TextView box;
    private String eeepwd;


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
        DS_user newuser = new DS_user();
        String replaceStr = account.replace('.', '_');
        newuser.email = replaceStr;
        newuser.pwd = password;
        fb_register register = new fb_register();
        register.registerToFB(newuser);



        //return false;
        return true;
    }
    public void readData(String email, final String ipwd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef = database.getReference("user/" + email);

        System.out.println("讀取 login db1");
        //Log.e("144",eeepwd);
        System.out.println(eeepwd);
        System.out.println(email);
        System.out.println(ipwd);
        eeepwd = ipwd;
        Log.e("123", eeepwd);

        myRef.addChildEventListener(new ChildEventListener() {//讀取

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot.getKey());
                if (dataSnapshot.getKey().equals("pwd")) {

                    String dbpwd = String.valueOf(dataSnapshot.getValue());
                    Log.e("1111", dbpwd);

                    Log.e("1122211", "登入驗證");

                    if (eeepwd.equals(dbpwd)) {
                        Intent intent = new Intent(register.this, login.class);
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(register.this)
                                .setMessage("login failed!")
                                .create()
                                .show();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //t1.setText(""+dataSnapshot.getValue());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("新增", snapshot.child("name").toString());
                    System.out.println("讀取 db3");
                }

            }
        });
    }

        public void goTo_BlueTooth (View view){

        String txt_account = account.getText().toString();
        String txt_password = password.getText().toString();
        String txt_user = user.getText().toString();
        String txt_box = box.getText().toString();

        //login
        register_flag = register_check(txt_account,txt_password,txt_user,txt_box);
            String replaceStr = txt_account.replace('.', '_');
            readData(replaceStr,txt_password);
    }
}
