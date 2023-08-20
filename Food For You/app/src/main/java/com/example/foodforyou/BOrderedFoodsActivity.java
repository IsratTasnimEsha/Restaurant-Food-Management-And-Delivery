package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BOrderedFoodsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<BUserClass> arrayList;
    BOrderAdapterClass bOrderAdapterClass;
    String str_bphone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bordered_foods);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        str_bphone=sharedPreferences.getString("BPhone", null);

        recyclerView=findViewById(R.id.bfo_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList= new ArrayList<>();
        bOrderAdapterClass= new BOrderAdapterClass(this, arrayList);
        recyclerView.setAdapter(bOrderAdapterClass);

        databaseReference.child("customers").child(str_bphone).child("Food_Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            BUserClass bUserClass=dataSnapshot.getValue(BUserClass.class);
                            arrayList.add(bUserClass);
                        }
                        bOrderAdapterClass.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}