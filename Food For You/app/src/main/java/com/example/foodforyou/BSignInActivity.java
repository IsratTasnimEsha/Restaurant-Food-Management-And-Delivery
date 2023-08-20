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

public class BSignInActivity extends AppCompatActivity {

    EditText bul_phone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsign_in);

        bul_phone=findViewById(R.id.bul_phone);
    }

    public void signup(View view) {
        startActivity(new Intent(this, BSignUpActivity.class));
    }

    public void updatePage(View view) {
        String st_bphone=bul_phone.getText().toString();
        if(st_bphone.isEmpty()) {
            bul_phone.setError("Please Enter The Phone Number.");
        }
        else {
            databaseReference.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(st_bphone)) {
                        Intent intent=new Intent(BSignInActivity.this, BHomeActivity.class);
                        intent.putExtra("BPhone", st_bphone);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(BSignInActivity.this, "Phone Number Is Not Registered Yet.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}