package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<BUserClass> arrayList;
    OrderAdapterClass orderAdapterClass;
    String str_phone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        recyclerView=findViewById(R.id.fo_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList= new ArrayList<>();
        orderAdapterClass= new OrderAdapterClass(this, arrayList);
        recyclerView.setAdapter(orderAdapterClass);

        databaseReference.child("users").child(str_phone).child("Food_Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            BUserClass bUserClass=dataSnapshot.getValue(BUserClass.class);
                            arrayList.add(bUserClass);
                        }
                        orderAdapterClass.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}