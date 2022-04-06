package com.example.wimm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText inputSalary;
    private EditText inputUsername,inputEmail,inputPassword,inputConformPassword;
    private Button btnRegister;
    private TextView alreadyHaveAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        inputUsername = findViewById(R.id.inputUsername);
        inputUsername.setOnClickListener(this);

        inputEmail = findViewById(R.id.inputEmail);
        inputEmail.setOnClickListener(this);

        inputPassword = findViewById(R.id.inputPassword);
        inputPassword.setOnClickListener(this);

        inputConformPassword = findViewById(R.id.inputConformPassword);
        inputConformPassword.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(this);

        inputSalary = findViewById(R.id.inputSalary);
        inputSalary.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.alreadyHaveAccount:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                registerUser();
                break;

        }


    }

    private void registerUser() {

        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String conformPassword = inputConformPassword.getText().toString();

        if (inputSalary.getText().toString().isEmpty())
        {
            inputSalary.setError("Please fill in the Salary field!");
            inputSalary.requestFocus();
            return;
        }

        int salary = Integer.parseInt(inputSalary.getText().toString());

        if(username.isEmpty())
        {
            inputUsername.setError("A Username is required!");
            inputUsername.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputEmail.setError("Please provide a valid Email!");
            inputEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            inputPassword.setError("Password is required!");
            inputPassword.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            inputPassword.setError("Please confirm the password!");
            inputPassword.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            inputPassword.setError("Password should have a Length of 6 or more!");
            inputPassword.requestFocus();
            return;
        }



        if(password.equals(conformPassword)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Add the user to the firestore database
                        Map<String,Object> userFirestore = new HashMap<>();
                        userFirestore.put("username",username);
                        userFirestore.put("email",email);
                        userFirestore.put("salary",salary);

                        firestore.collection("user").document(email.toLowerCase())
                                .set(userFirestore)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Send a verification email and redirect to login page
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent = (new Intent(RegisterActivity.this,LoginActivity.class));
                                        startActivity(intent);
                                        user.sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this,"Thanks for signing up, Check your email to verify your account !",Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "An Error occurred, try again!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Failed to Sign up, Try another Email !",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            inputConformPassword.setError("Passwords do not match!");
            inputConformPassword.requestFocus();
            return;
        }

    }
}