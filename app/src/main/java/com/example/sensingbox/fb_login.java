package com.example.sensingbox;

public class fb_login {

    public void logincheck(String email,String pwd) {
        firebase_upload fb = new firebase_upload();
        String co ="";
        fb.readData(email,pwd,co);
    }
    public String pwdcheck(String pwd,String dbpwd){
        System.out.println("登入驗證");
        System.out.println(dbpwd);
        if(pwd.equals(dbpwd))
        {
            System.out.println("yes");
            return "yes";
        }
        else{
            System.out.println("no");
            return "no";
        }


    }
}
