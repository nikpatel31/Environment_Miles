package com.example.environment;

import android.content.Context;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.PrivateKey;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class rankrecycleradapter extends RecyclerView.Adapter<rankrecycleradapter.Viewholder> {

    public List<blogrank>rank_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;

    public rankrecycleradapter(List<blogrank>rank_list){

        this.rank_list=rank_list;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         storageReference = storage.getReference();
        mAuth=FirebaseAuth.getInstance();
        context=viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rank_list_item,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {


        String user_name_data = rank_list.get(i).getname();
        viewholder.setusernametext(user_name_data);

        String user_name_points= rank_list.get(i).getPoints();
        viewholder.setuserpoints(user_name_points);

        viewholder.setrank(String.valueOf(i + 1));


        String userimage_url=rank_list.get(i).getImage();
        viewholder.setuserimage(userimage_url);

    }

    @Override
    public int getItemCount() {
        return rank_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private View mView;
        private CircleImageView blog_user_image_rank;
        private TextView blog_user_name_rank,blog_points_rank,blog_rank_rank;
        public int rankorder = 0 ;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setusernametext(String desctext){
            blog_user_name_rank = mView.findViewById(R.id.blog_user_name_rank);
            blog_user_name_rank.setText(desctext);
        }
        public void setuserpoints(String pointtext){
            blog_points_rank=mView.findViewById(R.id.blog_points_rank);
            blog_points_rank.setText(pointtext);
        }
        public void setrank(String s){
            blog_rank_rank= mView.findViewById(R.id.blog_rank_rank);
            this.rankorder=rankorder;
            blog_rank_rank.setText(String.valueOf(s));
        }

        public void setuserimage(String userimage_url) {
            blog_user_image_rank =mView.findViewById(R.id.blog_user_image_rank);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.baseline_person_black_24dp);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(userimage_url)
                    .into(blog_user_image_rank);
        }
    }
}
