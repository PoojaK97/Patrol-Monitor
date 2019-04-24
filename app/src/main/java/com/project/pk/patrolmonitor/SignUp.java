
package com.project.pk.patrolmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.provider.Settings.Secure;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView login;
    private Button register;
    private EditText password;
    private EditText email;
    private EditText pol_id;
    private ProgressDialog progressDialog;
    String path;
    String path1;
    //private String android_id = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        progressDialog = new ProgressDialog(this);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        pol_id = findViewById(R.id.police_id);
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view==register)
                    userSignUp();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view==login){
                    finish();
                }
            }
        });

    }

    private void userSignUp(){
        String mPol_id=pol_id.getText().toString().trim();
        final String mEmail=email.getText().toString().trim();
        String mPassword =password.getText().toString().trim();
        path="USERS/"+mPol_id+"/Status";
        path1="USERS/"+mPol_id+"/DeviceID";

        if (TextUtils.isEmpty(mPol_id)) {
            Toast.makeText(this, "Please enter the Police ID assigned to you", Toast.LENGTH_SHORT).show();
            pol_id.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mEmail)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if (mPassword.length()<6){
            password.setError("Minimum length of password should be 6");
            password.requestFocus();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("USERS/"+mPol_id+"/Email");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (!mEmail.equals(value)) {
                    Toast.makeText(getApplicationContext(),"Incorrect Details", Toast.LENGTH_SHORT).show();
                    i=1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (i == 0){
            pol_id.requestFocus();
            password.requestFocus();
            return;
        }

        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Woo-hoo! Successfully registered", Toast.LENGTH_SHORT).show();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                            ref.setValue("Off Duty");
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path1);
                            //ref1.setValue(android_id);
                            startActivity(new Intent(com.project.pk.patrolmonitor.SignUp.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException()instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUp.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                            }
                                else {
                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
    }

}