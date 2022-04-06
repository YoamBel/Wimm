package com.example.wimm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wimm.Helper.DataAccess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText inputPassword,inputEmail;
    private TextView forgotPassword,textViewSignUp;
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.inputEmail);
        inputEmail.setOnClickListener(this);

        inputPassword = findViewById(R.id.inputPassword);
        inputPassword.setOnClickListener(this);

        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(this);

        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewSignUp:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.btnlogin:
                userLogin();
                break;
        }


    }

    private void userLogin() {
        String email = inputEmail.getText().toString().toLowerCase();
        String password = inputPassword.getText().toString();

        if(email.isEmpty())
        {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputEmail.setError("Please provide valid email !");
            inputEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            inputPassword.setError("Password should have a Length of 6 or more!");
            inputPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser userAuthInstance = FirebaseAuth.getInstance().getCurrentUser();
                    if(userAuthInstance.isEmailVerified()) {
                        DataAccess.SetUser(email);
                        Intent intent = (new Intent(LoginActivity.this, MainActivity.class));
                        startActivity(intent);
                    }

                    else
                        Toast.makeText(LoginActivity.this,"Check you email to verify your account !",Toast.LENGTH_LONG).show();
                }

                else Toast.makeText(LoginActivity.this,"Failed to login ! Please check you credentials !",Toast.LENGTH_LONG).show();

            }
        });
    }
}