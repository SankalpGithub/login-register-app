package com.example.login_registration;

import static com.example.login_registration.register.registerUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.UserWriteRecord;

public class reset_password extends AppCompatActivity {

    EditText new_passoword,con_new_pass;
    Button reset;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        new_passoword = findViewById(R.id.new_password);
        con_new_pass = findViewById(R.id.con_new_pass);
        reset = findViewById(R.id.reset);
        auth = FirebaseAuth.getInstance();
        DatabaseReference registerUser2;
        registerUser2 = FirebaseDatabase.getInstance().getReference().child("User");


        String newpass = String.valueOf(new_passoword.getText());
        String con_newpass = String.valueOf(con_new_pass.getText());

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!newpass.equals(con_newpass)){
                    Toast.makeText(reset_password.this, "confrim password does not match", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser2.child("j0NhNI3RWIShZtBNjtD1s8pgTvD3").child("password").setValue(String.valueOf(new_passoword.getText()));
                    Toast.makeText(reset_password.this, "confrim password match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}