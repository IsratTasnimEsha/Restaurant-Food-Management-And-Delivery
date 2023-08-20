package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PastaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String str_phone;
    ArrayList<UserClass>arrayList;
    AdapterClass adapterClass;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasta);

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        recyclerView=findViewById(R.id.fp_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList= new ArrayList<>();
        adapterClass= new AdapterClass(this, arrayList);
        recyclerView.setAdapter(adapterClass);

        databaseReference.child("users").child(str_phone).child("Foods").child("Noodles_Pasta")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            UserClass userClass=dataSnapshot.getValue(UserClass.class);
                            arrayList.add(userClass);
                        }
                        adapterClass.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}