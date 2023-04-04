package com.example.login_registration;

import static com.example.login_registration.register.email;
import static com.example.login_registration.register.name;
import static com.example.login_registration.register.otp;
import static com.example.login_registration.register.password;
import static com.example.login_registration.register.registerUser;
import static com.example.login_registration.register.username;
import static com.example.login_registration.register.verify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class otp_verification extends AppCompatActivity {
    EditText enter_otp;
    Button verify_otp;
    TextView resendotp;
    static FirebaseAuth auth;
    ProgressBar progressBar4;
    String body;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        enter_otp = findViewById(R.id.enter_otp);
        resendotp = findViewById(R.id.resendOtp);
        verify_otp = findViewById(R.id.verify_otp);
        progressBar4 = findViewById(R.id.progressBar4);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();


        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp();
                subject ="Please verify your registration";
                body = "Verification code\n" +
                        "Please use the verification code below to register :\n" +
                        "\n" +
                        otp +
                        "\n" +
                        "If you didn’t request this, you can ignore this email.\n" +
                        "\n" +
                        "Thanks,\n" +
                        "The register-login team";

                verify(email,subject,body,getApplicationContext(),progressBar4);
            }
        });

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar4.setVisibility(View.VISIBLE);
                String otp_text = String.valueOf(enter_otp.getText());
                if (otp_text.equals(String.valueOf(otp))){
                    if (bundle.get("loginop").equals("2")) {
                        logindata();
                    }else {
                        resetpass();
                    }
                }else{
                    Toast.makeText(otp_verification.this, "OTP does not match", Toast.LENGTH_SHORT).show();
                    progressBar4.setVisibility(View.GONE);
                }
            }
        });

    }

    public void resetpass(){
        Intent intent = new Intent(otp_verification.this, reset_password.class);
        startActivity(intent);
        finish();
    }

    public void logindata(){


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar4.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            insertdata();
                            Toast.makeText(otp_verification.this, "Register successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(otp_verification.this, login.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(otp_verification.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void insertdata() {
        users user = new users(name,username,email,password);
        registerUser.child(auth.getUid()).setValue(user);
    }
}