package com.hanhhoai.orderfoodltdd.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanhhoai.orderfoodltdd.Database.AccountDatabase;
import com.hanhhoai.orderfoodltdd.Entity.Account;
import com.hanhhoai.orderfoodltdd.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText Edt_user;
    private EditText Edt_pass;
    private CheckBox cb_remember;
    private Button btn_login;
    private ImageView ImageView_addacc;
    private ImageView ImageView_cnfb;
    private ImageView ImageView_cngm;
    private ImageView ImageView_Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        CheckLogin();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            login();
            }
        });
        ImageView_addacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(MainActivity.this, com.hanhhoai.orderfoodltdd.Activity.SignUpActivity2.class);
               startActivity(intent);
            }
        });
        ImageView_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.hanhhoai.orderfoodltdd.Activity.ResetAccountActivity.class);
                startActivity(intent);
            }
        });
    }
    private void login(){

        SharedPreferences preferences =MainActivity.this.getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String user = Edt_user.getText().toString();
        String pass = Edt_pass.getText().toString();

        AccountDatabase database = AccountDatabase.getInstance(getApplicationContext());

        List<Account> accountList = database.daoAccount().ACCOUNT_LIST();

        String us = "admin";
        String ps = "123456";

        for( Account account : accountList){
            if (user.equals(account.getUser())){
                us=account.getUser();
                ps = account.getPass();
                Log.e("USER",""+us+""+ps);
                break;
            }
        }
        if(user.isEmpty()){
            Edt_user.setError("Tên đăng nhập trống !");
            return;
        }
        if (user.equals(us) == false){
            Edt_user.setError("Tài khoản không tồn tại !");
            return;
        }
        if(pass.isEmpty()){
            Edt_pass.setError("Mật khẩu trống !");
            return;
        }
        if (pass.equals(ps) == false){
            Edt_pass.setError("Mật khẩu không đúng !");
            return;
        }
        if(cb_remember.isChecked()){

            editor.putString("user",user);
            editor.putString("pass",pass);
            editor.putBoolean("check",true);
            editor.commit();

        }
        else if (cb_remember.isChecked() == false){
            editor.clear();
        }
        Intent intent = new Intent(MainActivity.this, com.hanhhoai.orderfoodltdd.Activity.Home_Activity2.class);
        startActivity(intent);
    }
    private void CheckLogin(){
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        boolean check = preferences.getBoolean("check",false);
        if(check){
            String user = preferences.getString("user",null);
            String pass = preferences.getString("pass",null);
            Edt_user.setText(user);
            Edt_pass.setText(pass);
            cb_remember.isChecked();
        }
        else{
            Edt_user.setText("");
            Edt_pass.setText("");
        }
    }
    private void mapping() {
        Edt_user =findViewById(R.id.edt_user);
        Edt_pass =findViewById(R.id.edt_pass);
        cb_remember=findViewById(R.id.cb_remember);
        btn_login=findViewById(R.id.btn_login);
        ImageView_addacc=findViewById(R.id.imv_addacc);
        ImageView_Reset=findViewById(R.id.imv_reset);
    }
}