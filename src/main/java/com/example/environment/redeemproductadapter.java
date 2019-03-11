package com.example.environment;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class redeemproductadapter extends RecyclerView.Adapter<redeemproductadapter.productviewholder> {


    private Context mCtx;
    private List<redeemlistview>productlist;

    public redeemproductadapter(Context mCtx , List<redeemlistview> productlist){
        this.mCtx = mCtx;
        this.productlist = productlist;

    }

    @NonNull
    @Override
    public redeemproductadapter.productviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.redeemlist_view,null);
        productviewholder holder=new productviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull redeemproductadapter.productviewholder productviewholder, int i) {


        redeemlistview listview=productlist.get(i);
        productviewholder.textviewheading.setText(listview.getHead());
        productviewholder.textviewdescription.setText(listview.getDescription());
        productviewholder.imageView.setImageDrawable(mCtx.getResources().getDrawable(listview.getImage()));



    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }



    class productviewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textviewheading, textviewdescription;

        public productviewholder(@NonNull View itemView) {
            super(itemView);




            imageView=itemView.findViewById(R.id.imageView);
            textviewheading=itemView.findViewById(R.id.textviewheading);
            textviewdescription=itemView.findViewById(R.id.textViewdiscription);


        }
    }
}
