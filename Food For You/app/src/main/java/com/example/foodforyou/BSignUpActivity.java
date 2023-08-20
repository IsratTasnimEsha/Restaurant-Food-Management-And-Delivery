package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class BSignUpActivity extends AppCompatActivity {

    EditText bur_name, bur_phone, bur_address;
    Button bur_signup;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsign_up);

        bur_name=findViewById(R.id.bur_name);
        bur_phone=findViewById(R.id.bur_phone);
        bur_address=findViewById(R.id.bur_address);
        bur_signup=findViewById(R.id.bur_signup);

        bur_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st_name=bur_name.getText().toString();
                String st_phone=bur_phone.getText().toString();
                String st_address=bur_address.getText().toString();

                if(st_name.isEmpty()) {
                    bur_name.setError("User Name Is Required.");
                    Toast.makeText(BSignUpActivity.this, "User Name Is Required.", Toast.LENGTH_SHORT).show();
                }
                else if(st_phone.isEmpty()) {
                    bur_phone.setError("Phone Number Is Required.");
                    Toast.makeText(BSignUpActivity.this, "Phone Number Is Required.", Toast.LENGTH_SHORT).show();
                }
                else if(st_address.isEmpty()) {
                    bur_address.setError("Location Is Required.");
                    Toast.makeText(BSignUpActivity.this, "Location Is Required.", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(st_phone)) {
                                Toast.makeText(BSignUpActivity.this,
                                        "Phone Number Is Already Registered.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("customers").child(st_phone).child("User_Name").setValue(st_name);
                                databaseReference.child("customers").child(st_phone).child("Phone").setValue(st_phone);
                                databaseReference.child("customers").child(st_phone).child("Location").setValue(st_address);
                                databaseReference.child("customers").child(st_phone).child("Food_Cart").setValue("");
                                databaseReference.child("customers").child(st_phone).child("Food_Orders").setValue("");
                                databaseReference.child("customers").child(st_phone).child("Notifications").setValue("");

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        "+88"+bur_phone.getText().toString(),
                                        60,
                                        TimeUnit.SECONDS,
                                        BSignUpActivity.this,
                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            }
//
                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                Toast.makeText(BSignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String s,
                                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                Intent intent=new Intent(new Intent(BSignUpActivity.this,
                                                        BAuthenticationActivity.class));
                                                intent.putExtra("BName", st_name);
                                                intent.putExtra("BPhone", st_phone);
                                                intent.putExtra("BLocation", st_address);
                                                startActivity(intent);
                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}