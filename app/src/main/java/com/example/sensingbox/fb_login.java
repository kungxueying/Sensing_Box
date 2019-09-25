package com.example.sensingbox;

public class fb_login {

    public static boolean llllllflag=false;

    public void logincheck(String email,String pwd) {
        firebase_upload fb = new firebase_upload();
        String co ="";
        fb.readData(email,pwd,co);

    }
    public boolean pwdcheck(String pwd,String dbpwd){
        System.out.println("登入驗證");
        System.out.println(dbpwd);
        if(pwd.equals(dbpwd))
        {
            System.out.println("yes");
            llllllflag=true;
            return true;
        }
        else{
            System.out.println("no");
            llllllflag=false;
            return false;
        }


    }
    public boolean return_flag(){return llllllflag;}
}
