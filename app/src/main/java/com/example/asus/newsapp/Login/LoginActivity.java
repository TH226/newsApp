package com.example.asus.newsapp.Login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.newsapp.Activity.MainActivity;
import com.example.asus.newsapp.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText etUserName;
    private EditText etPassword;
    private TextView tvForgetPassword;
    private TextView tvRegist;
    private ImageView ivPhoto;
    private Dialog dialog;
    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private TextView canclePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "0b46f72dff820ff93386b8d44e04675d");
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegist = (TextView) findViewById(R.id.tv_user_regist);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FindPasswordActivity.class));
            }
        });
        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this,"点击了拍照",Toast.LENGTH_SHORT).show();
               showSelectPhoto();
            }
        });
    }

    public void showSelectPhoto() {
        dialog = new Dialog(LoginActivity.this,R.style.ActionSheetDialogStyle);
        //填充对话框布局
        inflate = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_select_phpto,null);

        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        canclePhoto = (TextView) inflate.findViewById(R.id.canclePhoto);

        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        canclePhoto.setOnClickListener(this);

        //布局设置给Dialog
        dialog.setContentView(inflate);
       //获取当前的Activity所在窗体
        Window dialogWindow = dialog.getWindow();
        //设置弹窗从下方弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置弹窗距离底部的距离
        //降属性设置给弹窗
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
    @Override
    public void onClick(View v) {
          switch (v.getId()){
              case R.id.takePhoto:
                  Toast.makeText(this,"点击了拍照",Toast.LENGTH_SHORT).show();
                  break;
              case R.id.choosePhoto:
                  Toast.makeText(this,"点击了从相册选择",Toast.LENGTH_SHORT).show();
                  break;
              case R.id.canclePhoto:
                  Toast.makeText(this,"点击了取消",Toast.LENGTH_SHORT).show();
                  dialog.dismiss();
                  break;
          }

    }
    public void Login(View view) {
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (username.equals("") || password.equals("")) {
            return;
        }

        BmobUser.loginByAccount(username, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                  if(user != null){
                      Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(LoginActivity.this, MainActivity.class));
                  }else {
                      Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                  }
            }

        });
    }


}
