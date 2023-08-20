package com.example.foodforyou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BFoodAdapterClass extends RecyclerView.Adapter<BFoodAdapterClass.ViewHolder> {
    Context context;
    ArrayList<UserClass>arrayList;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    public BFoodAdapterClass(Context context, ArrayList<UserClass> userClassArrayList) {
        this.context = context;
        this.arrayList = userClassArrayList;
    }

    @NonNull
    @Override
    public BFoodAdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bfood_card, parent, false);
        return new BFoodAdapterClass.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BFoodAdapterClass.ViewHolder holder, int position) {
        UserClass userClass=arrayList.get(position);

        if(!userClass.getFood_Photo().isEmpty()) {
            Picasso.get().load(userClass.getFood_Photo()).into(holder.fd_photo);
        }
        else {
            holder.fd_photo.setBackgroundResource(R.drawable.ic_baseline_photo_24);
        }
        holder.fd_name.setText(userClass.getFood_Name());
        holder.fd_price.setText("Price: " + userClass.getFood_Price());
        if(!userClass.getFood_Offer_Price().isEmpty()) {
            holder.fd_offer.setText("Offer Price: " + userClass.getFood_Offer_Price());
        }
        if(!userClass.getFood_Description().isEmpty()) {
            holder.fd_description.setText(userClass.getFood_Description());
        }
        else {
            holder.fd_description.setText("");
        }
        holder.fd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BAddToCartActivity.class);

                intent.putExtra("Phone", userClass.getFood_Phone_Ref());
                intent.putExtra("Food Category", userClass.getFood_Category());
                intent.putExtra("Food Code", userClass.getFood_Code());
                intent.putExtra("Food Name", userClass.getFood_Name());
                intent.putExtra("Food Description", userClass.getFood_Description());
                intent.putExtra("Food Price", userClass.getFood_Price());
                intent.putExtra("Food Offer Price", userClass.getFood_Offer_Price());
                intent.putExtra("Food Photo", userClass.getFood_Photo());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView fd_photo;
        TextView fd_name, fd_price, fd_offer, fd_description;
        CardView fd_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fd_photo=itemView.findViewById(R.id.bfd_rphoto);
            fd_name=itemView.findViewById(R.id.bfd_name);
            fd_price=itemView.findViewById(R.id.bfd_price);
            fd_offer=itemView.findViewById(R.id.bfd_offer);
            fd_description=itemView.findViewById(R.id.bfd_description);
            fd_card=itemView.findViewById(R.id.bfd_card);
        }
    }
}