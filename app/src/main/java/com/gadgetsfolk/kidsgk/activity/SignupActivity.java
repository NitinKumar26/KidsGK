package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class SignupActivity extends AppCompatActivity {
    private String email;
    private String password;
    private CircularProgressIndicator indicator;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText edTvEmail = findViewById(R.id.edTv_email);
        EditText edTvPassword = findViewById(R.id.edTv_password);
        Button btnSignUpNow = findViewById(R.id.btn_sign_up_now);
        LinearLayout linearGoogle = findViewById(R.id.linear_google);
        indicator = findViewById(R.id.progress);
        View parentLayout = findViewById(R.id.signup_root);

        edTvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edTvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edTvEmail.getError() != null) edTvEmail.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edTvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edTvPassword.getError()!= null) edTvPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUpNow.setOnClickListener(v -> {
            email = edTvEmail.getText().toString().trim();
            password = edTvPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                linearGoogle.animate().alpha(0.0f); //Fades out view
                btnSignUpNow.animate().translationY(btnSignUpNow.getHeight()).alpha(0.0f); //Moves view down by its height
                indicator.setVisibility(View.VISIBLE);
                indicator.setProgressCompat(60, true);

                //SignUp User
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            //Redirect User to BottomActivity
                            Intent intent = new Intent(SignupActivity.this, BottomActivity.class);
                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> {
                            indicator.setIndicatorDirection(CircularProgressIndicator.INDICATOR_DIRECTION_COUNTERCLOCKWISE);
                            indicator.setProgressCompat(0, true);
                            indicator.setVisibility(View.INVISIBLE);

                            btnSignUpNow.animate().translationY(0).alpha(1.0f); //Returns the view to its starting position
                            linearGoogle.animate().alpha(1.0f); //Fades in view
                            btnSignUpNow.setEnabled(false);

                            if (e.getMessage() != null) {
                                Snackbar snackbar = Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("RETRY", v1 -> {
                                    snackbar.dismiss();
                                    indicator.setIndicatorDirection(CircularProgressIndicator.INDICATOR_DIRECTION_CLOCKWISE);
                                    btnSignUpNow.setEnabled(true);
                                });
                                snackbar.show();
                            }
                            Log.e("signUpFailure", e.getMessage());
                        });
            }else{
                if (email.isEmpty()) edTvEmail.setError("Enter Email");
                if (password.isEmpty()) edTvPassword.setError("Enter Password");

            }
        });
    }

    private void saveUserInFireStore(String userId, String email){
        User user = new User(userId, email);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    indicator.setProgressCompat(80, true);
                    if (documentSnapshot.exists()){
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(userId)
                                .set(user).addOnSuccessListener(unused -> {

                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }
}
