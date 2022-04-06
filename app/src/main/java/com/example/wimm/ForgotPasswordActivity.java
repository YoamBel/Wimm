package com.example.wimm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText inputEmail;
    private Button btnresetPassword;
    private TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();
        
        inputEmail = findViewById(R.id.inputEmail);

        btnresetPassword = findViewById(R.id.btnresetPassword);
        
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);


        btnresetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetPassword();

        }



    });

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyHaveAccount:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnresetPassword:
                resetPassword();
                break;

        }
    }*/

}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyHaveAccount:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void resetPassword() {
        String email = inputEmail.getText().toString();

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

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset your password !",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this,"Try again ! Something wrong happened",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}