package com.example.login_registration;






import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView emailtext,name2,usern2;
    Button logout,content;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailtext = findViewById(R.id.emailText);
        logout = findViewById(R.id.logout);
        name2 = findViewById(R.id.name);
        content = findViewById(R.id.content);
        usern2 = findViewById(R.id.username);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = auth.getUid();
        progressBar = findViewById(R.id.progressBar2);
        String emaild = user.getEmail();
        progressBar.setVisibility(View.VISIBLE);


        if (user == null){
            Intent intent = new Intent(MainActivity.this,login.class);
            startActivity(intent);
            finish();
        }else {
            emailtext.setText(emaild);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        });


        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Faild to get name", Toast.LENGTH_SHORT).show();
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult();
                    String namedb = String.valueOf(dataSnapshot.child("name").getValue());
                    String usernamedb = String.valueOf(dataSnapshot.child("username").getValue());
                   name2.setText(namedb);
                   usern2.setText(usernamedb);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Content.class);
                startActivity(intent);
                finish();
            }
        });
    }
}