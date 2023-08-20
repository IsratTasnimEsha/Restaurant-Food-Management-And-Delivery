package com.example.foodforyou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderAdapterClass extends RecyclerView.Adapter<OrderAdapterClass.MyViewHolder> {
    Context context;
    ArrayList<BUserClass> bUserClassArrayList;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public OrderAdapterClass(Context context, ArrayList<BUserClass> bUserClassArrayList) {
        this.context = context;
        this.bUserClassArrayList = bUserClassArrayList;
    }

    @NonNull
    @Override
    public OrderAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_order, parent, false);
        return new OrderAdapterClass.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterClass.MyViewHolder holder, int position) {
        BUserClass bUserClass = bUserClassArrayList.get(position);

        databaseReference.child("users").child(bUserClass.getRestaurant_Ref())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.fo_fname.setText(snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Name").getValue().toString());
                        String st_fphoto = snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Photo").getValue().toString();
                        if (!st_fphoto.isEmpty()) {
                            Picasso.get().load(st_fphoto).into(holder.fo_fphoto);
                        } else {
                            holder.fo_fphoto.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.fo_bphone.setText(bUserClass.getBuyer_Phone());
        holder.fo_blocation.setText(bUserClass.getBuyer_Location());
        holder.fo_count.setText("Quantity: " + bUserClass.getCount());
        holder.fo_price.setText("Total Price: " + bUserClass.getTotal_Price() + " Taka");
        holder.fo_time.setText("Order Time: " + bUserClass.getTime());

        if (!bUserClass.getTime_Limit().isEmpty()) {
            String st_time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            char ch_hour[] = {st_time.charAt(0), st_time.charAt(1)};
            int hour = Integer.parseInt(new String(ch_hour));

            char ch_min[] = {st_time.charAt(3), st_time.charAt(4)};
            int min = Integer.parseInt(new String(ch_min));

            char ch_l_hour[] = {bUserClass.getTime_Limit().charAt(0), bUserClass.getTime_Limit().charAt(1)};
            int l_hour = Integer.parseInt(new String(ch_l_hour));

            char ch_l_min[] = {bUserClass.getTime_Limit().charAt(3), bUserClass.getTime_Limit().charAt(4)};
            int l_min = Integer.parseInt(new String(ch_l_min));

            if(l_hour < hour) {
                holder.fo_remain.setText("Remaining: Time's Up");
            }

            else {
                if(l_hour == hour) {
                    if (l_min < min) {
                        holder.fo_remain.setText("Remaining: Time's Up");
                    }

                    else {
                        workWithTime(holder, hour, min, l_hour, l_min);
                    }
                }
                else {
                    workWithTime(holder, hour, min, l_hour, l_min);
                }
            }
        }

        if (bUserClass.getTime_Limit().isEmpty()) {
            holder.fo_limit.setText("Time Limit: Unlimited");
        } else {
            holder.fo_limit.setText("Time Limit: " + bUserClass.getTime_Limit());
        }
        if(bUserClass.getInstructions().isEmpty()) {
            holder.fo_instruction.setText("Instructions : Nope");
        } else {
            holder.fo_instruction.setText("Instructions :" + bUserClass.getInstructions());
        }
        holder.fo_status.setText("Status: " + bUserClass.getStatus());
        holder.fo_bill.setText("Bill: " + bUserClass.getBill());

        holder.fo_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(bUserClass.getTime())) {
                            holder.fo_status.setText("Accepted");

                            databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                    .child(bUserClass.getTime()).child("Status").setValue("Accepted");
                            databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                    .child(bUserClass.getTime()).child("Status").setValue("Accepted");
                        }
                        else {
                            Toast.makeText(context, "Order Is Already Cancelled By Consumer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.fo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                        .child(bUserClass.getTime()).removeValue();
                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(bUserClass.getTime()))
                                {
                                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).
                                            child("Food_Orders").child(bUserClass.getTime()).child("Status")
                                            .setValue("Not Available");
                                } else {
                                    Toast.makeText(context, "Order Is Already Cancelled By Consumer",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    private void workWithTime(MyViewHolder holder, int hour, int min, int l_hour, int l_min) {
        LocalTime time2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time2 = LocalTime.of(l_hour, l_min, 00);
        }
        LocalTime time1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time1 = LocalTime.of(hour, min, 00);
        }
        long hours = 0, minutes = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hours = ChronoUnit.HOURS.between(time1, time2);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            minutes = ChronoUnit.MINUTES.between(time1, time2) % 60;
        }
        holder.fo_remain.setText("Remaining: " + String.valueOf(String.valueOf(hours)) + "h " + minutes + "m");
    }

    @Override
    public int getItemCount() {
        return bUserClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fo_fname, fo_bphone, fo_blocation, fo_price, fo_count, fo_instruction, fo_time, fo_limit, fo_status, fo_bill, fo_remain;
        ImageView fo_fphoto, fo_accept, fo_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fo_fname = itemView.findViewById(R.id.fo_fname);
            fo_bphone = itemView.findViewById(R.id.fo_bphone);
            fo_blocation = itemView.findViewById(R.id.fo_blocation);
            fo_price = itemView.findViewById(R.id.fo_price);
            fo_count = itemView.findViewById(R.id.fo_count);
            fo_accept = itemView.findViewById(R.id.fo_accept);
            fo_cancel = itemView.findViewById(R.id.fo_cancel);
            fo_fphoto = itemView.findViewById(R.id.fo_fphoto);
            fo_instruction = itemView.findViewById(R.id.fo_instruction);
            fo_time = itemView.findViewById(R.id.fo_time);
            fo_limit = itemView.findViewById(R.id.fo_limit);
            fo_status = itemView.findViewById(R.id.fo_status);
            fo_bill=itemView.findViewById(R.id.fo_bill);
            fo_remain=itemView.findViewById(R.id.fo_remain);
        }
    }
}