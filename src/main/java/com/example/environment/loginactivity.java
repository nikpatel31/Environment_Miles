package com.example.environment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginactivity extends AppCompatActivity {

    private EditText emailtext,passwordtext;
    private CardView loginbutton,signupbutton;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);




        mAuth= FirebaseAuth.getInstance();


        emailtext=(EditText)findViewById(R.id.emailtext);
        passwordtext=(EditText)findViewById(R.id.passwordfield);
        loginbutton=(CardView) findViewById(R.id.loginbutton);
        signupbutton=(CardView) findViewById(R.id.signupbutton);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(loginactivity.this,registeractivity.class);
                startActivity(intent);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String loginemail=emailtext.getText().toString();
                String loginpassword=passwordtext.getText().toString();

                if (!TextUtils.isEmpty(loginemail) && !TextUtils.isEmpty(loginpassword)){
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginemail,loginpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                sendtomain();

                            }
                            else{
                                String errormsg=task.getException().getMessage();
                                Toast.makeText(loginactivity.this,"Error : " + errormsg , Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();

        if(currentuser != null){
            sendtomain();
        }
    }

    private void sendtomain() {

        Intent loginintent = new Intent(loginactivity.this,MainActivity.class);
        startActivity(loginintent);
        finish();
    }

}


