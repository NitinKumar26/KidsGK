package com.gadgetsfolk.kidsgk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private Button btnSignUpNow;
    private EditText edTvEmail;
    private EditText edTvPassword;
    private LinearLayout linearGoogle;
    private String mEmail;
    private String mPassword;
    private CircularProgressIndicator indicator;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        btnSignUpNow.setOnClickListener(v -> {
            mEmail = edTvEmail.getText().toString().trim();
            mPassword = edTvPassword.getText().toString().trim();
            if (!mEmail.isEmpty() && !mPassword.isEmpty() && isEmailValid(mEmail)) {
                linearGoogle.animate().alpha(0.0f); //Fades out view
                btnSignUpNow.animate().translationY(btnSignUpNow.getHeight()).alpha(0.0f); //Moves view down by its height
                indicator.setVisibility(View.VISIBLE);
                indicator.setProgressCompat(60, true);
                sendEmailVerificationLink(mEmail, mSharedPreferences);
                /*
                //SignUp User
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            //Redirect User to BottomActivity
                            Intent intent = new Intent(SignupActivity.this, BottomActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
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
                 */
            }else{
                if (mEmail.isEmpty()) edTvEmail.setError("Enter Email");
                if (mPassword.isEmpty()) edTvPassword.setError("Enter Password");
                if (!isEmailValid(mEmail)) edTvEmail.setError("Invalid email id");
            }
        });

        Intent intent = getIntent();
        if (getIntent() != null) {
            String emailLink = intent.getData().toString();

            //Confirm the link is a sign-in with email link.
            if (FirebaseAuth.getInstance().isSignInWithEmailLink(emailLink)) {
                //Retrieve this from wherever you stored it
                String email = mSharedPreferences.getString("email", null);
                //The client SDK will parse the code from the link for you.
                FirebaseAuth.getInstance().signInWithEmailLink(email, emailLink).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("SignupActivity", "Successfully signed in with email link!");
                        if (authResult.getAdditionalUserInfo() != null) authResult.getAdditionalUserInfo().isNewUser();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edTvEmail = findViewById(R.id.edTv_email);
        edTvPassword = findViewById(R.id.edTv_password);
        btnSignUpNow = findViewById(R.id.btn_sign_up_now);
        linearGoogle = findViewById(R.id.linear_google);
        indicator = findViewById(R.id.progress);
        View parentLayout = findViewById(R.id.signup_root);
        mSharedPreferences = getApplicationContext().getSharedPreferences("sign_up_details", MODE_PRIVATE);

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
    }

    private void sendEmailVerificationLink(String email, SharedPreferences preferences){
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                //URL you want to redirect back to. The domain (www.example.com) for this
                //URL must be whitelisted in the Firebase Console.
                .setUrl("https://kids-gk-6c5e9.web.app/")
                //This must be true
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.gadgetsfolk.kidsgk",
                        true, /* installIfNotAvailable */
                        "19" /* minimumVersion */
                ).build();

        FirebaseAuth.getInstance()
                .sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnSuccessListener(unused -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.apply();
                }).addOnFailureListener(e -> Log.e("Exception", e.getMessage()));
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

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
