package com.example.asus.newsapp.Login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.newsapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FindPasswordActivity extends Activity {
    private EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_activity);
        etEmail = (EditText) findViewById(R.id.et_email);
    }
    public void findpassword(View view){
        String email = etEmail.getText().toString();
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(FindPasswordActivity.this, "验证邮箱已发送", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(FindPasswordActivity.this, "验证邮箱发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
