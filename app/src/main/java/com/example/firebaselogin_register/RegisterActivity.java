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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterEmail, editTextRegisterPass, editTextRegisterName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Sign Up");

        findViews();

        //  (RESOLVED)    CRASHING HERE(A BUG EXIST HERE)****
        showHidePass();


        Button ButtonRegister = findViewById(R.id.button_register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPassword = editTextRegisterPass.getText().toString();
                String textName = editTextRegisterName.getText().toString();

                //Check if all the data is present before registering
                if(TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Name !", Toast.LENGTH_SHORT).show();
                    editTextRegisterName.setError("Name is required !");
                    editTextRegisterName.requestFocus();
                }
                else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required !");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please Re-enter Email !", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid Email is required !");
                    editTextRegisterEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Password is required !");
                    editTextRegisterEmail.requestFocus();
                }
                else if (textPassword.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "Password too short !", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Password should be atleast 6 digit !");
                    editTextRegisterEmail.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textEmail, textName, textPassword);
                }
            }
        });
    }

    private void registerUser(String textEmail, String textName, String textPassword) {
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(RegisterActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Check if the user created successful or not
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();

                                if (firebaseUser != null) {
                                    //Update Display name of the user
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
                                    firebaseUser.updateProfile(profileChangeRequest);

                                    Toast.makeText(RegisterActivity.this, "Registration Sucessfull !", Toast.LENGTH_SHORT).show();

                                    // Open UserProfileActivity when clicked
                                    Intent UserProfileActivity = new Intent(RegisterActivity.this, UserProfileActivity.class);

                                    //Stop user from going back to register activity on pressing back button
                                    UserProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(UserProfileActivity);
                                    finish();
                                }
                             else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                    });

    }


    // A BUG EXIST IN THIS CODE  (RESOLVED)
    private void showHidePass() {
        ImageView imageViewShowHidePass = findViewById(R.id.imageview_show_hide_password_signup);
        imageViewShowHidePass.setImageResource(R.drawable.ic_show_pwd);

        imageViewShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If pwd ois visible then hide it
                if (editTextRegisterPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextRegisterPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.ic_show_pwd); //changeeicon
                } else {
                    editTextRegisterPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });
    }

    private void findViews () {
        editTextRegisterEmail = findViewById(R.id.edittext_register_email);
        editTextRegisterName = findViewById(R.id.edittext_user_name);
        editTextRegisterPass = findViewById(R.id.edittext_register_pass);
        progressBar = findViewById(R.id.progress_bar);
    }}




