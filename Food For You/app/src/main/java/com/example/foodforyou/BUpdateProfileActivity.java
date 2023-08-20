package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BUpdateProfileActivity extends AppCompatActivity {
    EditText u_uname, u_address;
    TextView u_phone;
    String str_bphone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupdate_profile);

        u_uname=findViewById(R.id.bu_name);
        u_phone=findViewById(R.id.bu_phone);
        u_address=findViewById(R.id.bu_address);

        Intent intent=getIntent();
        str_bphone=intent.getStringExtra("BPhone");

        databaseReference.child("customers").child(str_bphone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_uname= (String) snapshot.child("User_Name").getValue();
                u_uname.setText(str_uname);
                String str_phone= (String) snapshot.child("Phone").getValue();
                u_phone.setText("Phone: " + str_phone);
                String str_address= (String) snapshot.child("Location").getValue();
                u_address.setText(str_address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void samePage(View view) {
        String st_uname=u_uname.getText().toString();
        String st_address=u_address.getText().toString();

        databaseReference.child("customers").child(str_bphone).child("User_Name").setValue(st_uname);
        databaseReference.child("customers").child(str_bphone).child("Location").setValue(st_address);

        Toast.makeText(BUpdateProfileActivity.this, "Informations Updated Successfully", Toast.LENGTH_SHORT).show();
    }
}