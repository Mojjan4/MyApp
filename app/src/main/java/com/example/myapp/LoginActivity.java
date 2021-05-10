package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private String email = "", password = "";

    private Button login;
    private EditText loginEmail, loginPassword;
    private TextView signUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        login = findViewById(R.id.login_btn_login);
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        signUp = findViewById(R.id.textSignUp);

        login.setOnClickListener(v -> validateData());

        signUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }

    private void validateData() {
        email = loginEmail.getText().toString().trim();
        password = loginPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Invalid email format");
        } else if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Enter password");
        } else {
            firebaseLogin();
        }
    }

    private void firebaseLogin() {
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String email = firebaseUser.getEmail();
            Toast.makeText(LoginActivity.this, "Logged in\n"+email, Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();


        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

        });

    }
}