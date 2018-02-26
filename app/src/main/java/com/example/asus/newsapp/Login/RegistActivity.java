package com.example.asus.newsapp.Login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.newsapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegistActivity extends Activity {
    private EditText etUserName;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etEmail;
    private Button btnRegist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etRePassword = (EditText) findViewById(R.id.et_repassword);
        etEmail = (EditText) findViewById(R.id.et_email);
        btnRegist = (Button) findViewById(R.id.btn_regist);
        Regist();

    }
    public void Regist(){
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String rePassword = etRePassword.getText().toString();
                String email = etEmail.getText().toString();
                if (username.equals("") || password.equals("")||rePassword.equals("")||etEmail.equals("")) {
                    Toast.makeText(RegistActivity.this, "请完善您的信息", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.equals(rePassword)){
                    Toast.makeText(RegistActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                BmobUser userObject = new BmobUser();
                userObject.setUsername(username);
                userObject.setPassword(password);
                userObject.setEmail(email);
                userObject.signUp(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if(e==null){
                            Toast.makeText(RegistActivity.this, "注册成功,验证邮件稍后将发送到您的邮箱", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
