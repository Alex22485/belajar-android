package org.santrenkoding.loginfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {
            // not signed in

            startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                     ))
                        .build(),
                RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return;


            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                    showSnackbar(R.string.sign_in_cancelled);
                    finish();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(SplashActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
//                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    showSnackbar(R.string.unknown_error);
                    Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            showSnackbar(R.string.unknown_sign_in_response);
        }
    }
}
