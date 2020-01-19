package com.example.androidfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

//    reference - > https://www.youtube.com/watch?v=oi-UAwiBigQ

    private EditText email;
    private EditText password;
    private TextView forgotPass;

    private Button loginBtn;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgotPass);

        loginBtn = findViewById(R.id.signInBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //if user logged in goto mainActivity
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "הכנס מייל", Toast.LENGTH_LONG).show();
                else {
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("debug ----- ", "Email sent.");
                                    }
                                }
                            });
                    Toast.makeText(getBaseContext(), "מייל איפוס סיסמה בדרך אליך", Toast.LENGTH_LONG).show();

                }
            }
        });

        if (!checkConnectivity()) {
            Toast.makeText(getBaseContext(), "שגיאה. אנא וודא כי קיים חיבור לרשת", Toast.LENGTH_LONG).show();
            Log.d("debug -------- ", "splash: ");
            finish();
            finishAffinity();
        }

        }


        @Override
        protected void onStart () {
            super.onStart();
            auth.addAuthStateListener(authStateListener);
        }

    private boolean checkConnectivity() {
        boolean hasWifi = false;
        boolean hasMobileData = false;
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (info.isConnected())
                        hasWifi = true;
                }
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (info.isConnected())
                        hasMobileData = true;
                }
            }


        }

        return hasMobileData || hasWifi;
    }


        private void startSignIn () {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "הכנס שם משתמש וסיסמא", Toast.LENGTH_LONG).show();
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "בעיה בהתחברות, נסה שוב מאוחר יותר", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


        }
    }
