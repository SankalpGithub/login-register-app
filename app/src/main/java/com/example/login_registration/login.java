package com.example.login_registration;

import static com.example.login_registration.register.email;
import static com.example.login_registration.register.otp;
import static com.example.login_registration.register.registerUser;
import static com.example.login_registration.register.username;
import static com.example.login_registration.register.verify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.net.URL;
import java.net.*;
import java.util.Iterator;
import java.util.Map;

public class login extends AppCompatActivity {
     DatabaseReference mDatabase;

    EditText login_email,login_password;
    TextView forgetpass;
    Button login_btn;
    FirebaseAuth auth;
    TextView registertext;
    ProgressBar progressBar;
    DatabaseReference registerUser2;
    String body;
    String subject;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(login.this,Content.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progressBar3);
        forgetpass = findViewById(R.id.forget_pass);
        login_email = findViewById(R.id.emailText);
        login_password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login);
        registertext = findViewById(R.id.registernow);
        auth = FirebaseAuth.getInstance();
        registerUser2 = FirebaseDatabase.getInstance().getReference("User");


        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(String.valueOf(login_email.getText()))) {
                    Toast.makeText(login.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser2.orderByChild("email").equalTo(String.valueOf(login_email.getText())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                otp();
                                subject = "Please verify your registration";
                                body = "Verification code\n" +
                                        "Please use the verification code below to Reset password :\n" +
                                        "\n" +
                                        otp +
                                        "\n" +
                                        "If you didn’t request this, you can ignore this email.\n" +
                                        "\n" +
                                        "Thanks,\n" +
                                        "The register-login team";
                                progressBar.setVisibility(View.GONE);
                                verify(String.valueOf(login_email.getText()), subject, body, getApplicationContext(), progressBar);
                                Intent intent = new Intent(login.this, otp_verification.class);
                                intent.putExtra("loginop", "1");
                                startActivity(intent);

                            } else {
                                Toast.makeText(login.this, "email does not exists!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);


                if (TextUtils.isEmpty(String.valueOf(login_email.getText()))) {
                    Toast.makeText(login.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(String.valueOf(login_password.getText()))) {
                    Toast.makeText(login.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(String.valueOf(login_email.getText()), String.valueOf(login_password.getText()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, Content.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}