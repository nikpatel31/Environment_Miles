package com.example.environment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar maintoolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FloatingActionButton addissuebutton;
    private String current_user_id;
    private BottomNavigationView mainbottomnavigation;
    private homefragment homefragment1;
    private rankfragment rankfragment1;
    private redeemfragment redeemfragment1;
    private profilefragment profilefragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        maintoolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(maintoolbar);

        //addissuebutton=(FloatingActionButton)findViewById(R.id.addissuebutton);
        // intent for floating button
        //addissuebutton.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
                  //  Intent intent = new Intent(MainActivity.this , Newissueactivity.class);
                   // startActivity(intent);
          //  }
       // });


        getSupportActionBar().setTitle("Environment Miles");

        if(mAuth.getCurrentUser() != null) {

            mainbottomnavigation = (BottomNavigationView) findViewById(R.id.mainbottomnavigation);

            //fragment works begin here
            homefragment1 = new homefragment();
            rankfragment1 = new rankfragment();
            redeemfragment1 = new redeemfragment();
            profilefragment1 = new profilefragment();

            replacefragment(homefragment1);
            mainbottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.bottom_action_home:
                            replacefragment(homefragment1);
                            return true;
                        case R.id.bottom_action_rank:
                            replacefragment(rankfragment1);
                            return true;
                        case R.id.bottom_action_redeem:
                            replacefragment(redeemfragment1);
                            return true;
                        case R.id.bottom_action_profile:
                            replacefragment(profilefragment1);
                            return true;

                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser == null){
            sendtologin();
        }
        else{
            current_user_id=mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){

                            Intent newintent = new Intent(MainActivity.this, accountsetup.class);
                            startActivity(newintent);
                            finish();
                        }
                    }
                    else{
                        String errormsg=task.getException().getMessage();
                        Toast.makeText(MainActivity.this,"Error :" + errormsg,Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.main_menu,menu);

            return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_settings_btn:
                Intent settingsIntent = new Intent(MainActivity.this, accountsetup.class);
                startActivity(settingsIntent);
                return true;


                default:
                    return false;
        }
    }

    private void logout() {
        mAuth.signOut();
        sendtologin();


    }


    private void sendtologin() {

        Intent intent = new Intent(MainActivity.this,loginactivity.class);
        startActivity(intent);

    }

    private void replacefragment(Fragment fragment){

        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.maincontainer,fragment);
        fragmentTransaction.commit();


    }

}
