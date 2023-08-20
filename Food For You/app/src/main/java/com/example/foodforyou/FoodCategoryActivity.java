package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FoodCategoryActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    EditText c_name, c_category, c_code, c_price, c_offer, c_description;
    String str_phone;
    ImageView c_pic;
    Uri uri;
    int SELECT_PICTURE=200;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_catagory);

        c_pic=findViewById(R.id.c_pic);
        c_name=findViewById(R.id.c_name);
        c_category=findViewById(R.id.c_category);
        c_code=findViewById(R.id.c_code);
        c_price=findViewById(R.id.c_price);
        c_offer=findViewById(R.id.c_offer);
        c_description=findViewById(R.id.c_description);
    }

    public void showPopUp(View view)
    {
        PopupMenu popupMenu=new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.food_items);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.f_burger) {
            c_category.setText("Burger");
        }
        if(menuItem.getItemId()==R.id.f_sandwich) {
            c_category.setText("Sandwich");
        }
        if(menuItem.getItemId()==R.id.f_pasta) {
            c_category.setText("Noodles_Pasta");
        }
        if(menuItem.getItemId()==R.id.f_pizza) {
            c_category.setText("Pizza");
        }
        if(menuItem.getItemId()==R.id.f_fries) {
            c_category.setText("Fries");
        }
        if(menuItem.getItemId()==R.id.f_rice) {
            c_category.setText("Rice");
        }
        if(menuItem.getItemId()==R.id.f_meat) {
            c_category.setText("Meat");
        }
        if(menuItem.getItemId()==R.id.f_veg) {
            c_category.setText("Vegetables");
        }
        if(menuItem.getItemId()==R.id.f_salad) {
            c_category.setText("Salad");
        }
        if(menuItem.getItemId()==R.id.f_soup) {
            c_category.setText("Soup");
        }
        if(menuItem.getItemId()==R.id.f_drinks) {
            c_category.setText("Drinks");
        }
        if(menuItem.getItemId()==R.id.f_dessert) {
            c_category.setText("Dessert");
        }
        if(menuItem.getItemId()==R.id.f_ffcombo) {
            c_category.setText("Fast_Food_Combo");
        }
        if(menuItem.getItemId()==R.id.f_rcombo) {
            c_category.setText("Rice_Combo");
        }
        if(menuItem.getItemId()==R.id.f_special) {
            c_category.setText("Restaurant_Special");
        }
        if(menuItem.getItemId()==R.id.f_others) {
            c_category.setText("Others");
        }
        if(menuItem.getItemId()==R.id.f_offer) {
            c_category.setText("Special_Offers");
        }
        return true;
    }

    public void SavePage(View view) {
        String st_name=c_name.getText().toString();
        String st_category=c_category.getText().toString();
        String st_code=c_code.getText().toString();
        String st_price=c_price.getText().toString();
        String st_offer=c_offer.getText().toString();
        String st_description=c_description.getText().toString();

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        if(st_name.isEmpty()) {
            c_name.setError("Please Enter The Food Name.");
        }
        else if(!(st_category.equals("Burger") || st_category.equals("Sandwich") || st_category.equals("Noodles_Pasta") ||
                st_category.equals("Pizza") || st_category.equals("Fries") || st_category.equals("Rice") ||
                st_category.equals("Meat") || st_category.equals("Vegetables") || st_category.equals("Salad") ||
                st_category.equals("Soup") || st_category.equals("Drinks") || st_category.equals("Dessert") ||
                st_category.equals("Fast_Food_Combo") || st_category.equals("Rice_Combo") ||
                st_category.equals("Restaurant_Special") || st_category.equals("Others") ||
                st_category.equals("Special_Offers"))) {
            c_category.setError("Please Select A Valid Food Category.");
        }
        else if(st_code.isEmpty()) {
            c_code.setError("Please Enter The Food Code.");
        }
        else if(st_price.isEmpty()) {
            c_price.setError("Please Enter The Price.");
        }
        else
        {
            databaseReference.child("users").child(str_phone).child("Foods").child(st_category)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(st_code)) {
                                        Toast.makeText(FoodCategoryActivity.this,
                                                "Food Code Is Already Existed.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Phone_Ref").setValue(str_phone);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Name").setValue(st_name);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Category").setValue(st_category);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Code").setValue(st_code);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Price").setValue(st_price);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Offer_Price").setValue(st_offer);
                                        databaseReference.child("users").child(str_phone).child("Foods").child(st_category).
                                                child(st_code).child("Food_Description").setValue(st_description);

                                        if(uri!=null) {
                                            StorageReference storageReference=FirebaseStorage.getInstance().getReference()
                                                    .child("image").child(str_phone).child(st_category).child(st_code);
                                            storageReference.putFile(uri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    storageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            databaseReference.child("users").child(str_phone).child("Foods")
                                                                    .child(st_category).child(st_code).child("Food_Photo")
                                                                    .setValue(uri.toString()).
                                                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(FoodCategoryActivity.this,
                                                                                    "Image Has Been Uploaded",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        else {
                                            databaseReference.child("users").child(str_phone).child("Foods")
                                                    .child(st_category).child(st_code).child("Food_Photo").setValue("");
                                        }

                                        Toast.makeText(FoodCategoryActivity.this, "New Food Has Been Uploaded.",
                                                Toast.LENGTH_SHORT).show();


                                        Intent intent2=new Intent(FoodCategoryActivity.this, FoodCategoryActivity.class);
                                        intent2.putExtra("Phone", str_phone);

                                        startActivity(intent2);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
        }
    }

    public void UploadPhoto(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            if(requestCode==SELECT_PICTURE) {
                uri=data.getData();
                if(uri!=null) {
                    c_pic.setImageURI(uri);
                }
            }
        }
    }
}