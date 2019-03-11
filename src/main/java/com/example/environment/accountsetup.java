package com.example.environment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class accountsetup extends AppCompatActivity {

    private CircleImageView setupimage;
    private Uri mainImageURI = null;
    private String userid;
    private boolean isChanged = false;

    private EditText nametext,phonetext,statetext,districttext;
    private CardView updatecard;
    private ProgressBar setupprogress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetup);

        Toolbar setuptoolbar = findViewById(R.id.setuptoolbar);
        setSupportActionBar(setuptoolbar);
        getSupportActionBar().setTitle("Account Setup");

        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        setupimage = (CircleImageView) findViewById(R.id.setupimage);
        nametext = (EditText) findViewById(R.id.nametext);
        phonetext = (EditText) findViewById(R.id.phonetext);
        statetext = (EditText) findViewById(R.id.statetext);
        districttext = (EditText) findViewById(R.id.newpostwaterstatetext);
        updatecard = (CardView) findViewById(R.id.updatecard);
        setupprogress = (ProgressBar) findViewById(R.id.setupprogress);

        setupprogress.setVisibility(View.VISIBLE);
        updatecard.setEnabled(false);

        firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String phone = task.getResult().getString("Phone");
                        String state = task.getResult().getString("State");
                        String district = task.getResult().getString("District");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        nametext.setText(name);
                        phonetext.setText(phone);
                        statetext.setText(state);
                        districttext.setText(district);

                        RequestOptions placeholderrequest = new RequestOptions();
                        placeholderrequest.placeholder(R.drawable.defaultprofilepic);
                        Glide.with(accountsetup.this).setDefaultRequestOptions(placeholderrequest).load(image).into(setupimage);

                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(accountsetup.this, "Firestore Retrieve Error: " + error, Toast.LENGTH_SHORT).show();
                }
                setupprogress.setVisibility(View.INVISIBLE);
                updatecard.setEnabled(true);


            }
        });

        updatecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = nametext.getText().toString();
                final String phone = phonetext.getText().toString();
                final String state = statetext.getText().toString();
                final String district = districttext.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(district) && mainImageURI != null) {

                    setupprogress.setVisibility(View.VISIBLE);
                    if (isChanged) {
                        userid = firebaseAuth.getCurrentUser().getUid();
                        File newImageFile = new File(mainImageURI.getPath());
                        try {
                            compressedImageFile = new Compressor(accountsetup.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        UploadTask image_path = storageReference.child("Profile Images").child(userid + ".jpg").putBytes(thumbData);
                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task,username, phone, state,district);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(accountsetup.this, "(IMAGE error)" + error, Toast.LENGTH_LONG).show();
                                    setupprogress.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } else {
                        storeFirestore(null, username,phone,state,district);
                    }
                }
            }
        });

        setupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(accountsetup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(accountsetup.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(accountsetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
            }
        });
    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task,String user_name,String phone,String state,String district) {

        Uri download_uri;
        if(task != null) {
            download_uri = task.getResult().getUploadSessionUri();
        }else {
            download_uri=mainImageURI;
        }
        Map<String , String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("phone",phone);
        userMap.put("image",download_uri.toString());
        userMap.put("State",state);
        userMap.put("District",district);
        userMap.put("email",firebaseAuth.getCurrentUser().getEmail());
        userMap.put("points",null);

        firebaseFirestore.collection("Users").document(userid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(accountsetup.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(accountsetup.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(accountsetup.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }
                setupprogress.setVisibility(View.VISIBLE);

            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(accountsetup.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupimage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}

