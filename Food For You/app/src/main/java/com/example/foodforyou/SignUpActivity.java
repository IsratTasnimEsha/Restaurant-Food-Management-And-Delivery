package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    EditText r_uname, r_phone, r_mail, r_birth, r_oname, r_rname, r_address, r_rphone, r_rmail, r_web,
            r_opening, r_type, r_open, r_close, r_des, r_pass, r_cpass;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        r_uname=findViewById(R.id.r_uname);
        r_phone=findViewById(R.id.r_phone);
        r_mail=findViewById(R.id.r_mail);
        r_birth=findViewById(R.id.r_birth);
        r_oname=findViewById(R.id.r_oname);
        r_rname=findViewById(R.id.r_rname);
        r_address=findViewById(R.id.r_address);
        r_rphone=findViewById(R.id.r_rphone);
        r_rmail=findViewById(R.id.r_rmail);
        r_web=findViewById(R.id.r_web);
        r_opening=findViewById(R.id.r_opening);
        r_type=findViewById(R.id.r_type);
        r_open=findViewById(R.id.r_open);
        r_close=findViewById(R.id.r_close);
        r_des=findViewById(R.id.r_des);
        r_pass=findViewById(R.id.r_pass);
        r_cpass=findViewById(R.id.r_cpass);
    }

    public void updatePage(View view) {
        String st_uname=r_uname.getText().toString();
        String st_phone=r_phone.getText().toString();
        String st_mail=r_mail.getText().toString();
        String st_birth=r_birth.getText().toString();
        String st_oname=r_oname.getText().toString();
        String st_rname=r_rname.getText().toString();
        String st_address=r_address.getText().toString();
        String st_rphone=r_rphone.getText().toString();
        String st_rmail=r_rmail.getText().toString();
        String st_web=r_web.getText().toString();
        String st_opening=r_opening.getText().toString();
        String st_type=r_type.getText().toString();
        String st_open=r_open.getText().toString();
        String st_close=r_close.getText().toString();
        String st_des=r_des.getText().toString();
        String st_pass=r_pass.getText().toString();
        String st_cpass=r_cpass.getText().toString();

        if(st_phone.isEmpty()) {
            r_phone.setError("Phone Number Is Required.");
            Toast.makeText(this, "Phone Number Is Required.", Toast.LENGTH_SHORT).show();
        }
        else if(st_rname.isEmpty()) {
            r_rname.setError("Restaurant Name Is Required.");
            Toast.makeText(this, "Restaurant Name Is Required.", Toast.LENGTH_SHORT).show();
        }
        else if(st_address.isEmpty()) {
            r_address.setError("Restaurant Address Is Required.");
            Toast.makeText(this, "Restaurant Address Is Required.", Toast.LENGTH_SHORT).show();
        }
        else if(st_rphone.isEmpty()) {
            r_rphone.setError("Restaurant Contact Number Is Required.");
            Toast.makeText(this, "Restaurant Contact Number Is Required.", Toast.LENGTH_SHORT).show();
        }
        else if(st_pass.length()<6) {
            r_pass.setError("Password Must Be At Least 6 Characters.");
            Toast.makeText(this, "Password Must Be At Least 6 Characters.", Toast.LENGTH_SHORT).show();
        }
        else if(!st_cpass.equals(st_pass)) {
            r_cpass.setError("Password Didn't Matched.");
            Toast.makeText(this, "Password Didn't Matched.", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(st_phone)) {
                        Toast.makeText(SignUpActivity.this,
                                "Phone Number Is Already Registered.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        databaseReference.child("users").child(st_phone).child("User_Name").setValue(st_uname);
                        databaseReference.child("users").child(st_phone).child("Phone").setValue(st_phone);
                        databaseReference.child("users").child(st_phone).child("EMail").setValue(st_mail);
                        databaseReference.child("users").child(st_phone).child("Date_Of_Birth").setValue(st_birth);
                        databaseReference.child("users").child(st_phone).child("Owner_Name").setValue(st_oname);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Name").setValue(st_rname);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Address").setValue(st_address);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Contact_Number").setValue(st_rphone);
                        databaseReference.child("users").child(st_phone).child("Restaurant_EMail").setValue(st_rmail);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Website").setValue(st_web);
                        databaseReference.child("users").child(st_phone).child("Date_Of_Opening").setValue(st_opening);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Type").setValue(st_type);
                        databaseReference.child("users").child(st_phone).child("Open_Time").setValue(st_open);
                        databaseReference.child("users").child(st_phone).child("Close_Time").setValue(st_close);
                        databaseReference.child("users").child(st_phone).child("Description").setValue(st_des);
                        databaseReference.child("users").child(st_phone).child("Password").setValue(st_pass);
                        databaseReference.child("users").child(st_phone).child("Restaurant_Photo").setValue("");
                        databaseReference.child("users").child(st_phone).child("Food_Orders").setValue("");
                        databaseReference.child("users").child(st_phone).child("Notifications").setValue("");
                        databaseReference.child("users").child(st_phone).child("Rating").setValue("0.00");
                        databaseReference.child("users").child(st_phone).child("Review").setValue("0");

                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}