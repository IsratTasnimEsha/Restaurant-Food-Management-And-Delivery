package com.example.foodforyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BAboutRestaurantFragment extends Fragment {

    TextView br_oname, br_rname, br_address, br_rphone, br_rmail, br_web, br_type, br_opening, br_open, br_close, br_des;
    ImageView br_rphoto;
    String str_phone;
    Uri uri;
    int SELECT_PICTURE=200;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b_about_restaurant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent=getActivity().getIntent();
        str_phone=intent.getStringExtra("Phone");

        br_oname=view.findViewById(R.id.br_oname);
        br_rname=view.findViewById(R.id.br_rname);
        br_address=view.findViewById(R.id.br_address);
        br_rphone=view.findViewById(R.id.br_rphone);
        br_rmail=view.findViewById(R.id.br_rmail);
        br_web=view.findViewById(R.id.br_web);
        br_type=view.findViewById(R.id.br_type);
        br_opening=view.findViewById(R.id.br_opening);
        br_open=view.findViewById(R.id.br_open);
        br_close=view.findViewById(R.id.br_close);
        br_des=view.findViewById(R.id.br_des);
        br_rphoto=view.findViewById(R.id.br_rphoto);

        databaseReference.child("users").child(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_oname= (String) snapshot.child("Owner_Name").getValue();
                if(!str_oname.isEmpty()) {
                    br_oname.setText("Owner: " + str_oname);
                }
                String str_rname= (String) snapshot.child("Restaurant_Name").getValue();
                br_rname.setText(str_rname);
                String str_address= (String) snapshot.child("Restaurant_Address").getValue();
                br_address.setText("Location: " + str_address);
                String str_rphone= (String) snapshot.child("Restaurant_Contact_Number").getValue();
                br_rphone.setText("Contact Number: " + str_rphone);
                String str_rmail= (String) snapshot.child("Restaurant_EMail").getValue();
                if(!str_rmail.isEmpty()) {
                    br_rmail.setText("E-Mail: " +str_rmail);
                }
                String str_web= (String) snapshot.child("Restaurant_Website").getValue();
                if(!str_web.isEmpty()) {
                    br_web.setText("Visit our website " + str_web);
                }
                String str_opening= (String) snapshot.child("Date_Of_Opening").getValue();
                if(!str_opening.isEmpty()) {
                    br_opening.setText("Since " + str_opening);
                }
                String str_type= (String) snapshot.child("Restaurant_Type").getValue();
                if(!str_type.isEmpty()) {
                    br_type.setText("Restaurant Type: " + str_type);
                }
                String str_open= (String) snapshot.child("Open_Time").getValue();
                if(!str_open.isEmpty()) {
                    br_open.setText("Open: "+ str_open);
                }
                String str_close= (String) snapshot.child("Close_Time").getValue();
                if(!str_close.isEmpty()) {
                    br_close.setText("Close: " + str_close);
                }
                String str_des= (String) snapshot.child("Description").getValue();
                br_des.setText(str_des);

                String str_rphoto= (String) snapshot.child("Restaurant_Photo").getValue();
                if(!str_rphoto.isEmpty()) {
                    Picasso.get().load(str_rphoto).into(br_rphoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}