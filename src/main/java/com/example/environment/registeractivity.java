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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registeractivity extends AppCompatActivity {

    private EditText mailfield,passwordfield,confirmpasswordfiled;
    private CardView signupbuttonregister;
    private TextView logintextsignup;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        mAuth=FirebaseAuth.getInstance();



        mailfield=(EditText) findViewById(R.id.mailfield);
        passwordfield=(EditText) findViewById(R.id.passwordfield);
        confirmpasswordfiled=(EditText) findViewById(R.id.confirmpasswordfield);
        signupbuttonregister=(CardView) findViewById(R.id.sigunupbuttonregister);
        logintextsignup=(TextView) findViewById(R.id.logintextsignup);
        progressBar2=(ProgressBar) findViewById(R.id.progressBar2);


        logintextsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



        signupbuttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = mailfield.getText().toString();
                String password = passwordfield.getText().toString();
                String confirmpassword=confirmpasswordfiled.getText().toString();


                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmpassword)){

                    if(password.equals(confirmpassword)){

                        progressBar2.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()){
                                   Intent setupintent = new Intent(registeractivity.this,accountsetup.class);
                                   startActivity(setupintent);
                                   finish();
                                }
                                else{

                                    String errormsg=task.getException().getMessage();
                                    Toast.makeText(registeractivity.this,"Error : " + errormsg,Toast.LENGTH_SHORT).show();

                                }
                                progressBar2.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else{
                        Toast.makeText(registeractivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser =mAuth.getCurrentUser();
        if(currentuser != null){

            sendtomain();

        }
    }

    private void sendtomain() {

        Intent intent = new Intent(registeractivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
