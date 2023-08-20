package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText u_uname, u_mail, u_birth, u_oname, u_rname, u_address, u_rphone, u_rmail,
            u_web, u_opening, u_type, u_open, u_close, u_des;
    TextView u_phone;
    ImageView u_rphoto;
    String str_phone;
    Uri uri;
    int SELECT_PICTURE=200;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        u_uname=findViewById(R.id.u_uname);
        u_phone=findViewById(R.id.u_phone);
        u_mail=findViewById(R.id.u_mail);
        u_birth=findViewById(R.id.u_birth);
        u_oname=findViewById(R.id.u_oname);
        u_rname=findViewById(R.id.u_rname);
        u_address=findViewById(R.id.u_address);
        u_rphone=findViewById(R.id.u_rphone);
        u_rmail=findViewById(R.id.u_rmail);
        u_web=findViewById(R.id.u_web);
        u_opening=findViewById(R.id.u_opening);
        u_type=findViewById(R.id.u_type);
        u_open=findViewById(R.id.u_open);
        u_close=findViewById(R.id.u_close);
        u_des=findViewById(R.id.u_des);
        u_rphoto=findViewById(R.id.u_rphoto);

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        databaseReference.child("users").child(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_uname= (String) snapshot.child("User_Name").getValue();
                u_uname.setText(str_uname);
                String str_phone= (String) snapshot.child("Phone").getValue();
                u_phone.setText("Phone: " + str_phone);
                String str_mail= (String) snapshot.child("EMail").getValue();
                u_mail.setText(str_mail);
                String str_birth= (String) snapshot.child("Date_Of_Birth").getValue();
                u_birth.setText(str_birth);
                String str_oname= (String) snapshot.child("Owner_Name").getValue();
                u_oname.setText(str_oname);
                String str_rname= (String) snapshot.child("Restaurant_Name").getValue();
                u_rname.setText(str_rname);
                String str_address= (String) snapshot.child("Restaurant_Address").getValue();
                u_address.setText(str_address);
                String str_rphone= (String) snapshot.child("Restaurant_Contact_Number").getValue();
                u_rphone.setText(str_rphone);
                String str_rmail= (String) snapshot.child("Restaurant_EMail").getValue();
                u_rmail.setText(str_rmail);
                String str_web= (String) snapshot.child("Restaurant_Website").getValue();
                u_web.setText(str_web);
                String str_opening= (String) snapshot.child("Date_Of_Opening").getValue();
                u_opening.setText(str_opening);
                String str_type= (String) snapshot.child("Restaurant_Type").getValue();
                u_type.setText(str_type);
                String str_open= (String) snapshot.child("Open_Time").getValue();
                u_open.setText(str_open);
                String str_close= (String) snapshot.child("Close_Time").getValue();
                u_close.setText(str_close);
                String str_des= (String) snapshot.child("Description").getValue();
                u_des.setText(str_des);

                String str_rphoto= (String) snapshot.child("Restaurant_Photo").getValue();
                if(!str_rphoto.isEmpty()) {
                    Picasso.get().load(str_rphoto).into(u_rphoto);
                }
                else {
                    u_rphoto.setBackgroundResource(R.drawable.ic_baseline_add_a_photo_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void samePage(View view) {
        String st_uname=u_uname.getText().toString();
        String st_mail=u_mail.getText().toString();
        String st_birth=u_birth.getText().toString();
        String st_oname=u_oname.getText().toString();
        String st_rname=u_rname.getText().toString();
        String st_address=u_address.getText().toString();
        String st_rphone=u_rphone.getText().toString();
        String st_rmail=u_rmail.getText().toString();
        String st_web=u_web.getText().toString();
        String st_opening=u_opening.getText().toString();
        String st_type=u_type.getText().toString();
        String st_open=u_open.getText().toString();
        String st_close=u_close.getText().toString();
        String st_des=u_des.getText().toString();

        databaseReference.child("users").child(str_phone).child("User_Name").setValue(st_uname);
        databaseReference.child("users").child(str_phone).child("EMail").setValue(st_mail);
        databaseReference.child("users").child(str_phone).child("Date_Of_Birth").setValue(st_birth);
        databaseReference.child("users").child(str_phone).child("Owner_Name").setValue(st_oname);
        databaseReference.child("users").child(str_phone).child("Restaurant_Name").setValue(st_rname);
        databaseReference.child("users").child(str_phone).child("Restaurant_Address").setValue(st_address);
        databaseReference.child("users").child(str_phone).child("Restaurant_Contact_Number").setValue(st_rphone);
        databaseReference.child("users").child(str_phone).child("Restaurant_EMail").setValue(st_rmail);
        databaseReference.child("users").child(str_phone).child("Restaurant_Website").setValue(st_web);
        databaseReference.child("users").child(str_phone).child("Date_Of_Opening").setValue(st_opening);
        databaseReference.child("users").child(str_phone).child("Restaurant_Type").setValue(st_type);
        databaseReference.child("users").child(str_phone).child("Open_Time").setValue(st_open);
        databaseReference.child("users").child(str_phone).child("Close_Time").setValue(st_close);
        databaseReference.child("users").child(str_phone).child("Description").setValue(st_des);

        if(uri!=null) {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("restaurant_image")
                    .child(str_phone);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("users").child(str_phone)
                                    .child("Restaurant_Photo").setValue(uri.toString()).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UpdateProfileActivity.this, "Image Has Been Uploaded",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }
            });
        }
        else {

        }

        Toast.makeText(UpdateProfileActivity.this, "Informations Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    public void uploadRestaurantPhoto(View view) {
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
                    u_rphoto.setImageURI(uri);
                }
            }
        }
    }
}