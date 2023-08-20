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

public class ChangePasswordActivity extends AppCompatActivity {
    EditText p_old, p_new, p_confirm;
    String str_phone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        p_old=findViewById(R.id.p_old);
        p_new=findViewById(R.id.p_new);
        p_confirm=findViewById(R.id.p_confirm);
    }

    public void passwordPage(View view) {
        final String st_old=p_old.getText().toString();

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        databaseReference.child("users").child(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_old= (String) snapshot.child("Password").getValue();

                String st_old=p_old.getText().toString();
                String st_new=p_new.getText().toString();
                String st_confirm=p_confirm.getText().toString();

                if(!str_old.equals(st_old)) {
                    p_old.setError("Password Is Incorrect.");
                    Toast.makeText(ChangePasswordActivity.this, "Password Didn't Matched.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if(st_new.length()<6) {
                        p_new.setError("Password Must Be At Least 6 Characters.");
                        Toast.makeText(ChangePasswordActivity.this, "Password Must Be At Least 6 Characters.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(!st_new.equals(st_confirm)) {
                        p_confirm.setError("Password Didn't Matched.");
                        Toast.makeText(ChangePasswordActivity.this, "Password Didn't Matched.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        databaseReference.child("users").child(str_phone).child("Password").setValue(st_new);
                        Toast.makeText(ChangePasswordActivity.this, "Password Has Been Changed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}