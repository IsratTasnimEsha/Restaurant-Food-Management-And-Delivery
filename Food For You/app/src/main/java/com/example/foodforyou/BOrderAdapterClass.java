package com.example.foodforyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BOrderAdapterClass extends RecyclerView.Adapter<BOrderAdapterClass.MyViewHolder> {
    Context context;
    ArrayList<BUserClass> bUserClassArrayList;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public BOrderAdapterClass(Context context, ArrayList<BUserClass> bUserClassArrayList) {
        this.context = context;
        this.bUserClassArrayList = bUserClassArrayList;
    }

    @NonNull
    @Override
    public BOrderAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bfood_order, parent, false);
        return new BOrderAdapterClass.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BOrderAdapterClass.MyViewHolder holder, int position) {
        BUserClass bUserClass = bUserClassArrayList.get(position);

        databaseReference.child("users").child(bUserClass.getRestaurant_Ref())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.bfo_fname.setText(snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Name").getValue().toString());
                        holder.bfo_rname.setText(snapshot.child("Restaurant_Name").getValue().toString());
                        String st_fphoto = snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Photo").getValue().toString();
                        if (!st_fphoto.isEmpty()) {
                            Picasso.get().load(st_fphoto).into(holder.bfo_fphoto);
                        } else {
                            holder.bfo_fphoto.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.bfo_count.setText("Quantity: " + bUserClass.getCount());
        holder.bfo_price.setText("Total Price: " + bUserClass.getTotal_Price() + " Taka");
        holder.bfo_time.setText("Order Time: " + bUserClass.getTime());
        if(bUserClass.getTime_Limit().isEmpty()) {
            holder.bfo_limit.setText("Time Limit: Unlimited");
        }
        else {
            holder.bfo_limit.setText("Time Limit: " + bUserClass.getTime_Limit());
        }
        holder.bfo_status.setText("Status: " + bUserClass.getStatus());
        holder.bfo_bill.setText("Bill: " + bUserClass.getBill());
        holder.bfo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(bUserClass.getTime())) {
                                            databaseReference.child("users").child(bUserClass.getRestaurant_Ref())
                                                    .child("Food_Orders").child(bUserClass.getTime()).child("Status")
                                                    .setValue("Cancelled");
                                        }
                                        else {
                                            Toast.makeText(context, "Food Is Not Available",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                        .child(bUserClass.getTime()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bUserClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bfo_fname, bfo_rname, bfo_price, bfo_count, bfo_txt_confirm, bfo_time, bfo_limit, bfo_status, bfo_bill;
        ImageView bfo_fphoto, bfo_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bfo_fname=itemView.findViewById(R.id.bfo_fname);
            bfo_rname=itemView.findViewById(R.id.bfo_rname);
            bfo_price=itemView.findViewById(R.id.bfo_price);
            bfo_count=itemView.findViewById(R.id.bfo_count);
            bfo_cancel=itemView.findViewById(R.id.bfo_cancel);
            bfo_txt_confirm=itemView.findViewById(R.id.bfo_txt_confirm);
            bfo_fphoto=itemView.findViewById(R.id.bfo_fphoto);
            bfo_time=itemView.findViewById(R.id.bfo_time);
            bfo_limit=itemView.findViewById(R.id.bfo_limit);
            bfo_status=itemView.findViewById(R.id.bfo_status);
            bfo_bill=itemView.findViewById(R.id.bfo_bill);
        }
    }
}