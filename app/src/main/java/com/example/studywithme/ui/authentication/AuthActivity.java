package com.example.studywithme.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.join.SessionListActivity;
import com.example.studywithme.ui.viewmodels.AuthViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.example.studywithme.utils.ToastMaster;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Activity managing the Authentication of a user
 */
public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_sign_up);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initViews();
        initAuthViewModel();
        initGoogleSignInClient();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Logger.log(e.getMessage());
            }
        }
    }

    /**
     * initializes all views
     */
    private void initViews() {
        ImageView backgroundImage = findViewById(R.id.iv_sign_in_background_image);
        backgroundImage.setImageResource(R.drawable.sign_in);
        SignInButton googleSignInButton = findViewById(R.id.bt_google_sign_in);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    /**
     * initializes the AuthViewModel
     *
     * @see AuthViewModel
     */
    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    /**
     * initializes the Google Sign-In Client using the token set in google-services.json
     */
    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    /**
     * Starts the Sign-In process
     */
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    /**
     * Fetches the AuthenticationCredentials provided by Google
     *
     * @param googleSignInAccount the Account of the user requesting to login
     */
    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    /**
     * Requests the sign-in of a new user in the backend
     *
     * @param googleAuthCredential the fetched authenticationCredentials provided by Google
     */
    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.getAuthenticatedUser().observe(this, authenticatedUser -> {
            if (authenticatedUser.isNew()) {
                createNewUser(authenticatedUser);
            } else {
                startSessionListActivity(authenticatedUser.getUid());
            }
        });
    }

    /**
     * Requests to store a new User in the backend
     *
     * @param authenticatedUser the user to be stored
     */
    private void createNewUser(User authenticatedUser) {
        authViewModel.createUser(authenticatedUser);
        authViewModel.getCreatedUser().observe(this, user -> {
            if (user.isCreated()) {
                ToastMaster.showToast(this, user.getName());
            }
            startSessionListActivity(user.getUid());
        });
    }

    /**
     * Starts the SessionListActivity
     *
     * @param userId the ID of the now logged-in User
     */
    private void startSessionListActivity(String userId) {
        Intent intent = new Intent(AuthActivity.this, SessionListActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
        finish();
    }
}
