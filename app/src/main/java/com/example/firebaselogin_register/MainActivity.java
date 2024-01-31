package com.example.firebaselogin_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassword;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail = findViewById(R.id.email_id);
        editTextLoginPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_bar);

        showHidePassword();

        //Register User
        TextView textviewregister = findViewById(R.id.textview_link_register);
        textviewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent (MainActivity.this,RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        auth = FirebaseAuth.getInstance();
        //LOGIN USER
        Button buttonLogin = findViewById(R.id.Login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassword.getText().toString();

                //Check if all the data are present before signing in

                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required !");
                    editTextLoginEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(MainActivity.this, "Please Re-enter Email !", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is required !");
                    editTextLoginEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(MainActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    editTextLoginPassword.setError("Password is required !");
                    editTextLoginPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPassword);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textPassword) {

        auth.signInWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Sucessfull !", Toast.LENGTH_SHORT).show();
                    Intent UserProfileActivity = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(UserProfileActivity);
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Show and hide password by eye eyecon
    private void showHidePassword() {
        ImageView imageViewShowHidePass = findViewById(R.id.imageview_show_hide_password);
        imageViewShowHidePass.setImageResource(R.drawable.ic_show_pwd);

        imageViewShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If pwd ois visible then hide it
                if(editTextLoginPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.ic_show_pwd); //chaneicon
                }
                else {
                    editTextLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });
    }
}