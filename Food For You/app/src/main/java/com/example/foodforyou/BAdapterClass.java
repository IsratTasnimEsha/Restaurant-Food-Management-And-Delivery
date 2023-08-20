package com.example.foodforyou;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import soup.neumorphism.NeumorphFloatingActionButton;

public class BAdapterClass extends RecyclerView.Adapter<BAdapterClass.MyViewHolder> {
    Context context;
    ArrayList<RestaurantClass>list;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public void setFilteredList(ArrayList<RestaurantClass>filteredList) {
        this.list=filteredList;
        notifyDataSetChanged();
    }

    public BAdapterClass(Context context, ArrayList<RestaurantClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.brestaurant_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RestaurantClass restaurantClass=list.get(position);

        holder.br_rname.setText(restaurantClass.getRestaurant_Name());
        holder.br_radderss.setText(restaurantClass.getRestaurant_Address());
        if(!restaurantClass.getRestaurant_Photo().isEmpty()) {
            Picasso.get().load(restaurantClass.getRestaurant_Photo()).into(holder.br_rphoto);
        }
        else {
            holder.br_rphoto.setBackgroundResource(R.drawable.ic_baseline_photo_24);
        }
        holder.br_rating.setText(restaurantClass.getRating());
        holder.br_review.setText(restaurantClass.getReview());

        holder.br_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BRestaurantActivity.class);
                intent.putExtra("Phone", restaurantClass.getPhone());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.br_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rating_dialog = new Dialog(context);
                holder.rating_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                holder.rating_dialog.setContentView(R.layout.b_restaurant_rating_dialog);
                holder.rating_dialog.show();

                holder.brrd_dismiss = holder.rating_dialog.findViewById(R.id.brrd_dismiss);
                holder.brrd_submit = holder.rating_dialog.findViewById(R.id.brrd_submit);
                holder.brrd_ratingbar = holder.rating_dialog.findViewById(R.id.brrd_ratingBar);
                holder.brrd_rating_text = holder.rating_dialog.findViewById(R.id.brrd_rating_text);

                holder.brrd_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.rating_dialog.dismiss();
                    }
                });

                holder.brrd_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        holder.brrd_rating_text.setText("Rating: " + v);
                    }
                });

                holder.brrd_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String st_rating=String.valueOf(holder.brrd_ratingbar.getRating());
                        System.out.println(st_rating);

                        Integer str_review = Integer.parseInt(restaurantClass.getReview()) + 1;
                        Float str_rating = ( ( Float.parseFloat(restaurantClass.getRating()) * (str_review-1) ) +
                                Float.parseFloat(st_rating) ) / str_review;

                        databaseReference.child("users").child(restaurantClass.getPhone()).child("Review")
                                .setValue(String.valueOf(str_review));
                        databaseReference.child("users").child(restaurantClass.getPhone()).child("Rating")
                                .setValue(String.format("%.1f", str_rating));

                        holder.rating_dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<RestaurantClass>filteredList) {
        list=filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView br_rname, br_radderss, brrd_rating_text, br_rating, br_review;
        ImageView br_rphoto;
        CardView br_card, br_rate_us, brrd_submit;
        Dialog rating_dialog;
        NeumorphFloatingActionButton brrd_dismiss;
        RatingBar brrd_ratingbar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            br_rname=itemView.findViewById(R.id.bfd_rname);
            br_radderss=itemView.findViewById(R.id.bfd_raddress);
            br_rphoto=itemView.findViewById(R.id.bfd_rphoto);
            br_card=itemView.findViewById(R.id.bfd_card);
            br_rate_us=itemView.findViewById(R.id.bfd_rate_us);
            br_rating=itemView.findViewById(R.id.bfd_rating);
            br_review=itemView.findViewById(R.id.bfd_review);
        }
    }
}