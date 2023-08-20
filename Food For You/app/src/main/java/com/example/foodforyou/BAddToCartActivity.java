package com.example.foodforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import soup.neumorphism.NeumorphFloatingActionButton;

public class BAddToCartActivity extends AppCompatActivity implements View.OnClickListener {
    TextView bac_fname, bac_des, bac_price, bac_offer;
    ImageView bac_fphoto;
    EditText bac_instruction, bac_count;
    NeumorphFloatingActionButton bac_remove, bac_add;
    Button bac_add_cart;
    String st_phone, st_blocation, st_fcategory, st_fcode, st_fname, st_des, st_price, st_offer, st_fphoto, st_count,
            st_bphone, st_instruction;
    int count=1;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badd_to_cart);

        bac_fname=findViewById(R.id.bac_fname);
        bac_des=findViewById(R.id.bac_des);
        bac_price=findViewById(R.id.bac_price);
        bac_offer=findViewById(R.id.bac_offer);
        bac_fphoto=findViewById(R.id.bac_fphoto);
        bac_instruction=findViewById(R.id.bac_instruction);
        bac_add=findViewById(R.id.bac_add);
        bac_remove=findViewById(R.id.bac_remove);
        bac_count=findViewById(R.id.bac_count);
        bac_add_cart=findViewById(R.id.bac_add_cart);

        Intent intent1=getIntent();
        st_phone=intent1.getStringExtra("Phone");
        st_fcategory=intent1.getStringExtra("Food Category");
        st_fcode=intent1.getStringExtra("Food Code");
        st_fname=intent1.getStringExtra("Food Name");
        st_des=intent1.getStringExtra("Food Description");
        st_price=intent1.getStringExtra("Food Price");
        st_offer=intent1.getStringExtra("Food Offer Price");
        st_fphoto=intent1.getStringExtra("Food Photo");

        bac_fname.setText(st_fname);
        bac_des.setText(st_des);
        bac_price.setText("Price: " + st_price);
        bac_offer.setText("Offer Price: " + st_offer);
        if(!st_fphoto.isEmpty()) {
            Picasso.get().load(st_fphoto).into(bac_fphoto);
        }
        else {
            bac_fphoto.setBackgroundResource(R.drawable.ic_baseline_photo_24);
        }

        bac_add.setOnClickListener(this);
        bac_remove.setOnClickListener(this);
        bac_add_cart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bac_add) {
            st_count= String.valueOf(bac_count.getText());
            if(st_count.isEmpty()) {
                Toast.makeText(this, "Invalid Count", Toast.LENGTH_SHORT).show();
            }
            count= Integer.parseInt(st_count);
            count++;
            bac_count.setText(String.valueOf(count));
        }
        if(view.getId()==R.id.bac_remove) {
            st_count= String.valueOf(bac_count.getText());
            if(st_count.isEmpty()) {
                Toast.makeText(this, "Invalid Count", Toast.LENGTH_SHORT).show();
            }
            count= Integer.parseInt(st_count);
            if(count>1) {
                count--;
            }
            else {
                count = 1;
            }
            bac_count.setText(String.valueOf(count));
        }
        if(view.getId()==R.id.bac_add_cart) {
            st_instruction=bac_instruction.getText().toString();
            st_count= String.valueOf(bac_count.getText());

            sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            st_bphone=sharedPreferences.getString("BPhone", null);
            st_blocation=sharedPreferences.getString("BLocation", null);

            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Buyer_Phone").setValue(st_bphone);
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Buyer_Location").setValue(st_blocation);
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Restaurant_Ref").setValue(st_phone);
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Food_Category").setValue(st_fcategory);
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Food_Code").setValue(st_fcode);
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Count").setValue(st_count);
            if(st_offer.isEmpty()) {
                databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                        .child(st_phone+st_fcategory+st_fcode).child("Total_Price")
                        .setValue(String.valueOf(Integer.parseInt(st_count) * Integer.parseInt(st_price)));
            }
            else {
                databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                        .child(st_phone+st_fcategory+st_fcode).child("Total_Price")
                        .setValue(String.valueOf(Integer.parseInt(st_count) * Integer.parseInt(st_offer)));
            }
            databaseReference.child("customers").child(st_bphone).child("Food_Cart")
                    .child(st_phone+st_fcategory+st_fcode).child("Instructions").setValue(st_instruction);

            Toast.makeText(this, "Item Added To Cart.", Toast.LENGTH_SHORT).show();
        }
    }
}