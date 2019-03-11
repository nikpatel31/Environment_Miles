package com.example.environment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class blogrecycleradapter extends RecyclerView.Adapter<blogrecycleradapter.Viewholder> {

    public List<blogpost>blog_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private String downloaduri;

    public blogrecycleradapter(List<blogpost>blog_list){

        this.blog_list=blog_list;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item,viewGroup, false);
        context=viewGroup.getContext();
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, int i) {

        String  desc_data = blog_list.get(i).getDescription();
        viewholder.setdesctext(desc_data);


        String address_data=blog_list.get(i).getAddress();
        viewholder.setpostaddress(address_data);
        // adding something for the image url

        //lets see how we can do it
        //other methods
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("post_images")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 String image_Url = uri.toString() ;// Got the download URL for 'users/me/profile.png'
                    downloaduri =image_Url;
            }
        });
        // here the above 2 methods


        String thumburi = blog_list.get(i).getImage_thumb1();
       // viewholder.setblogimage(downloaduri,thumburi);

        String userid= blog_list.get(i).getUser_id();
        firebaseFirestore.collection("City_Ahmedabad").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String username = task.getResult().getString("name");
                    String userimage = task.getResult().getString("image_url1");

                    viewholder.setuserdata(username,userimage);
                }
                else{

                }

            }
        });
        // user data will be here

        long milliseconds =blog_list.get(i).getTimestamp().getTime();
        String datestring = DateFormat.format("MM/dd/yyyy",new Date(milliseconds)).toString();
        viewholder.settime(datestring);



    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private View mview;

        private TextView descview;
        private TextView addressview;
        private ImageView blogimageview;
        private TextView blogdate;
        private TextView blogusername;
        private CircleImageView bloguserimage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }
        public void setdesctext(String desctext){
            descview=mview.findViewById(R.id.blog_desc);
            descview.setText(desctext);
        }

        public void setblogimage( String downloaduri , String thumburi){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.baseline_person_black_24dp);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloaduri)
                    .thumbnail(Glide.with(context).load(thumburi))
                    .into(blogimageview);
        }
        public void settime(String date){
            blogdate = mview.findViewById(R.id.blog_date);
            blogdate.setText(date);

        }

        public void setuserdata(String name , String image ){

            blogusername=mview.findViewById(R.id.blog_user_name);
            bloguserimage=mview.findViewById(R.id.blog_user_image);

            blogusername.setText("Citizen");
            RequestOptions placeholderoption = new RequestOptions();
            placeholderoption.placeholder(R.drawable.defaultprofilepic);

            Glide.with(context).applyDefaultRequestOptions(placeholderoption).load(image).into(bloguserimage);

        }
        public void setpostaddress(String addresstext){
            addressview=mview.findViewById(R.id.blog_address);
            addressview.setText(addresstext);
        }

    }

}
