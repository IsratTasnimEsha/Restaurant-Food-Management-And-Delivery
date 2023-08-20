package com.example.foodforyou;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BSaladFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<UserClass> arrayList;
    BFoodAdapterClass adapterClass;
    String str_phone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b_salad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent=getActivity().getIntent();
        str_phone=intent.getStringExtra("Phone");

        recyclerView=view.findViewById(R.id.bfb_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList= new ArrayList<>();
        adapterClass= new BFoodAdapterClass(getContext(), arrayList);
        recyclerView.setAdapter(adapterClass);

        databaseReference.child("users").child(str_phone).child("Foods").child("Salad")
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