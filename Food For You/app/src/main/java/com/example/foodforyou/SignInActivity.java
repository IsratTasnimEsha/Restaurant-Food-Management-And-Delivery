package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText l_phone, l_pass;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        l_phone=findViewById(R.id.l_phone);
        l_pass=findViewById(R.id.l_pass);
    }

    public void signup(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void updatePage(View view) {
        String st_phone=l_phone.getText().toString();
        String st_pass=l_pass.getText().toString();

        if(st_phone.isEmpty()) {
            l_phone.setError("Please Enter The Phone Number.");
        }
        else if(st_pass.isEmpty()) {
            l_pass.setError("Please Enter The Password.");
        }
        else {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(st_phone)) {
                        String str_pass = (String) snapshot.child(st_phone).child("Password").getValue();

                        if (str_pass.equals(st_pass)) {
                            Intent intent=new Intent(SignInActivity.this, HomeActivity.class);
                            intent.putExtra("Phone", st_phone);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "Password Is Wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Phone Number Is Not Registered Yet.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}