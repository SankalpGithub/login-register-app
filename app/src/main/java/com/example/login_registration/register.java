package com.example.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.Math;

public class register extends AppCompatActivity {
    EditText register_email,register_password,register_name,register_username,confrim_password;
    Button verify_btn;
    TextView logintext;
    static int otp;
    ProgressBar progressBar;
     String body;
     String subject;
    static DatabaseReference registerUser;
    static String username,name,email,password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);
        register_email = findViewById(R.id.emailText);
        register_password  = findViewById(R.id.password);
        confrim_password = findViewById(R.id.confrim_password);
        register_name = findViewById(R.id.register_name);
        register_username = findViewById(R.id.register_username);
        verify_btn = findViewById(R.id.verify_btn);
        logintext = findViewById(R.id.loginnow);
        registerUser = FirebaseDatabase.getInstance().getReference().child("User");



        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this,login.class);
                startActivity(intent);
                finish();
            }
        });


        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confrimpassword;
                name = String.valueOf(register_name.getText());
                username = String.valueOf(register_username.getText());
                email = String.valueOf(register_email.getText());
                password = String.valueOf(register_password.getText());
                confrimpassword = String.valueOf(confrim_password.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(register.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.endsWith("@gmail.com")){
                    Toast.makeText(register.this, "Enter proper email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(name)){
                    Toast.makeText(register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(username)){
                    Toast.makeText(register.this, "Enter your username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confrimpassword)){
                    Toast.makeText(register.this, "confrim password does not match", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(register.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                            }else{
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
                                 progressBar.setVisibility(View.GONE);
                                verify(email,subject,body,getApplicationContext(),progressBar);
                                Intent intent = new Intent(register.this,otp_verification.class);
                                intent.putExtra("loginop","2");
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(register.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    public static int otp(){
        int max = 999999;
        int min = 100000;
         otp  = (int)(Math.random()*(max-min+1)+min);
        return otp;
    }

    public static void verify(String receiveremail, String subject, String body,Context context,ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
        String senderemail = "sankalp2004gaikwad@gmail.com";
        String emailpassword = "ddldqsakavubojsg";
        String app_username = "sankalp2004gaikwad";

        String host = "smtp.gmail.com";
        try {
            Properties properties =System.getProperties();

            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.auth","true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(app_username,emailpassword);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);

            try {
                mimeMessage.setFrom(new InternetAddress(senderemail,"register-login"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(receiveremail));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    }catch (MessagingException e){
                        Toast.makeText(context, "Faild to send OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            thread.start();
            Toast.makeText(context, "OTP has been sent", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

        } catch (MessagingException e) {
            Log.d("e", String.valueOf(e));
        }

    }





}