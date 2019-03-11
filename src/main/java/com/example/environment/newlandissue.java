package com.example.environment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;


public class newlandissue extends AppCompatActivity {




    private Toolbar newPostlandToolbar;

    private ImageView newPostlandImage1;
    private ImageView newPostlandImage2;
    private EditText newPostlandDesc;
    private EditText newpostlanddistricttext;
    private EditText newpostlandstatetext;
    private EditText newpostlandphonetext;
    private CardView newPostlandBtn;
    private ProgressBar newPostlandProgress;
    private Uri postlandImageUri1 = null;
    private Uri postlandImageUri2 = null;
    private Spinner landspinner;
    private EditText newpostlandaddresstext;

    //citycodes
    // changes done from her to here to here

    private String state[] = new String[28];

    // changes done for the datbase till here


    private static String Gujarat  = "0001";
    private static String Ahmedabad="GJ01";


    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;


    private Bitmap compressedImageFile1;
    private Bitmap compressedImageFile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlandissue);


        //changes

        state[0] = "Gujarat";
        state[1] = "Andhra Pradesh";
        state[2] = "Assam";
        state[3] = "Bihar";
        state[4] = "Chhattisgarh";
        state[5] = "Goa";
        state[6] = "Haryana";
        state[7] = "Himachal Pradesh";
        state[8] = "Jammu and Kashmir";
        state[9] = "Maharashtra";
        state[10] = "Rajasthan";
        state[11] = "Arunachal Pradesh";
        state[12] = "Jharkhand";
        state[13] = "Karnataka";
        state[14] = "Kerala";
        state[15] = "Madhya Pradesh";
        state[16] = "Manipur";
        state[17] = "Meghalaya";
        state[18] = "Mizoram";
        state[19] = "Nagaland";
        state[20] = "Odisha";
        state[21] = "Punjab";
        state[22] = "Sikkim";
        state[23] = "Tamil Nadu";
        state[24] = "Telangana";
        state[25] = "Tripura";
        state[26] = "Uttarakhand";
        state[27] = "Uttar Pradesh";

        final String[] waterspiner1 = new String[1];
        landspinner=(Spinner)findViewById(R.id.spinnerforissueland);
        final String valueinStringland = landspinner.getSelectedItem().toString();
        //  waterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //@Override
        //public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //   String waterspinner2;
        //   Toast.makeText(newwaterissue.this, "YOUR SELECTION IS : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        //     waterspinner2 = parent.getItemAtPosition(position).toString();
        //    waterspiner1 =waterspinner2;

        //   }

        //  @Override
        //  public void onNothingSelected(AdapterView<?> parent) {
        //    String waterspinner1=parent.getItemAtPosition(1).toString();
        // }
        //});



        //chNangesend

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostlandToolbar = findViewById(R.id.issuetoolbar);
        setSupportActionBar(newPostlandToolbar);
        getSupportActionBar().setTitle("Add New Issue for Land");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostlandImage1 = (ImageView) findViewById(R.id.image1land);
        newPostlandImage2= (ImageView) findViewById(R.id.image2land);
        newPostlandDesc = (EditText) findViewById(R.id.descriptiontextland);
        newPostlandBtn = (CardView) findViewById(R.id.newpostuploadbuttonland);
        newPostlandProgress = (ProgressBar) findViewById(R.id.newpostlandprogress);
        newpostlanddistricttext=(EditText)findViewById(R.id.newpostlanddistricttext);
        newpostlandstatetext=(EditText)findViewById(R.id.newpostlandstatetext);
        newpostlandphonetext=(EditText)findViewById(R.id.newpostlandmobiletext);
        newpostlandaddresstext=(EditText)findViewById(R.id.newpostlandaddresstext);

        newPostlandImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(newlandissue.this);

            }
        });

        newPostlandImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(newlandissue.this);

            }
        });


        newPostlandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostlandDesc.getText().toString();
                final String stateland=newpostlandstatetext.getText().toString();
                final String districtland=newpostlanddistricttext.getText().toString();
                final String phoneland=newpostlandphonetext.getText().toString();
                final String addressland=newpostlandaddresstext.getText().toString();

                if(!TextUtils.isEmpty(desc)&& !TextUtils.isEmpty(addressland) && postlandImageUri1 != null && postlandImageUri2 != null){

                    newPostlandProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    //PHOTO UPLOAD

                    File newImageFile1 = new File(postlandImageUri1.getPath());
                    File newImageFile2 = new File(postlandImageUri2.getPath());



                    try {

                        compressedImageFile1 = new Compressor(newlandissue.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile1);

                        compressedImageFile2 = new Compressor(newlandissue.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile2);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    compressedImageFile1.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
                    byte[] imageData1 = baos1.toByteArray();

                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    compressedImageFile1.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                    byte[] imageData2 = baos2.toByteArray();



                    // PHOTO UPLOAD

                    UploadTask filePath1 = storageReference.child("Issue_land").child(randomName + ".jpg").putBytes(imageData1);
                    UploadTask filePath2 = storageReference.child("Issue_land").child(randomName + ".jpg").putBytes(imageData2);

                    filePath1.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri1 = storageReference.getDownloadUrl().toString();
                            final String downloadUri2 = storageReference.getDownloadUrl().toString();

                            if(task.isSuccessful()) {

                                File newThumbFile1 = new File(postlandImageUri1.getPath());
                                File newThumbFile2 = new File(postlandImageUri2.getPath());

                                try {

                                    compressedImageFile1 = new Compressor(newlandissue.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(1)
                                            .compressToBitmap(newThumbFile1);

                                    compressedImageFile2 = new Compressor(newlandissue.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(1)
                                            .compressToBitmap(newThumbFile2);


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                                compressedImageFile1.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
                                byte[] thumbData1 = baos1.toByteArray();

                                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                compressedImageFile2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                                byte[] thumbData2 = baos2.toByteArray();


                                //check fromm issue


                                UploadTask uploadTask1 = storageReference.child("Issue_water/thumb")
                                        .child(randomName + ".jpg").putBytes(thumbData1);

                                UploadTask uploadTask2 = storageReference.child("Issue_water/thumb")
                                        .child(randomName + ".jpg").putBytes(thumbData2);



                                uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String downloadthumbUri1 = storageReference.getDownloadUrl().toString();
                                        String downloadthumbUri2 = storageReference.getDownloadUrl().toString();


                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("user_id", current_user_id);
                                        postMap.put("email", firebaseAuth.getCurrentUser().getEmail());
                                        postMap.put("Resolved by:",valueinStringland);
                                        postMap.put("image_url1", downloadUri1);
                                        postMap.put("image_url2", downloadUri2);
                                        postMap.put("image_thumb1", downloadthumbUri1);
                                        postMap.put("image_thumb2", downloadthumbUri2);
                                        postMap.put("description", desc);
                                        postMap.put("address",addressland);
                                        postMap.put("State", stateland);
                                        postMap.put("District", districtland);
                                        postMap.put("Contact No.", phoneland);
                                        postMap.put("timestamp", FieldValue.serverTimestamp());
                                        postMap.put("Points","0");
                                        postMap.put("Status","Not Resolved");

                                        firebaseFirestore.collection("Issue_land").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if (task.isSuccessful()) {

                                                    Toast.makeText(newlandissue.this, "Issue was Successfully added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(newlandissue.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {


                                                }

                                                newPostlandProgress.setVisibility(View.INVISIBLE);

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //Error handling

                                    }
                                });




                                // add other work here to upload on the database

                                //changes start

                                for (int  j = 0; j < 28; j++) {

                                    if ((stateland).equals(state[j])) {


                                        final int finalJ = j;
                                        uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                String downloadthumbUri1 = storageReference.getDownloadUrl().toString();
                                                String downloadthumbUri2 = storageReference.getDownloadUrl().toString();

                                                Map<String, Object> postMap = new HashMap<>();

                                                postMap.put("user_id", current_user_id);
                                                postMap.put("email", firebaseAuth.getCurrentUser().getEmail());
                                                postMap.put("Resolved by:",valueinStringland);
                                                postMap.put("image_url1", downloadUri1);
                                                postMap.put("image_url2", downloadUri2);
                                                postMap.put("image_thumb1", downloadthumbUri1);
                                                postMap.put("image_thumb2", downloadthumbUri2);
                                                postMap.put("description", desc);
                                                postMap.put("address",addressland);
                                                postMap.put("State", stateland);
                                                postMap.put("District", districtland);
                                                postMap.put("Contact No.", phoneland);
                                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                                postMap.put("Points","0");
                                                postMap.put("Status","Not Resolved");


                                                firebaseFirestore.collection("State_" + state[finalJ]).add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if (task.isSuccessful()) {

                                                            if (districtland.equals("Ahmedabad")) {

                                                                String downloadthumbUri1 = storageReference.getDownloadUrl().toString();
                                                                String downloadthumbUri2 = storageReference.getDownloadUrl().toString();

                                                                Map<String, Object> postMap = new HashMap<>();

                                                                postMap.put("user_id", current_user_id);
                                                                postMap.put("email", firebaseAuth.getCurrentUser().getEmail());
                                                                postMap.put("Resolved by:",valueinStringland);
                                                                postMap.put("image_url1", downloadUri1);
                                                                postMap.put("image_url2", downloadUri2);
                                                                postMap.put("image_thumb1", downloadthumbUri1);
                                                                postMap.put("image_thumb2", downloadthumbUri2);
                                                                postMap.put("description", desc);
                                                                postMap.put("address",addressland);
                                                                postMap.put("State", stateland);
                                                                postMap.put("District", districtland);
                                                                postMap.put("Contact No.", phoneland);
                                                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                                                postMap.put("Points","0");
                                                                postMap.put("Status","Not Resolved");
                                                                firebaseFirestore.collection("City_Ahmedabad").add(postMap);

                                                            }

                                                            Intent mainIntent = new Intent(newlandissue.this, MainActivity.class);
                                                            startActivity(mainIntent);
                                                            finish();

                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }
                            }
                            else {

                                newPostlandProgress.setVisibility(View.INVISIBLE);

                            }

                        }
                    });


                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode== RESULT_OK) {

                postlandImageUri1 = result.getUri();
                newPostlandImage1.setImageURI(postlandImageUri1);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}

