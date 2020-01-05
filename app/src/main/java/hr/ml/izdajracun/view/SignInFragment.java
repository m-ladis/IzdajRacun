package hr.ml.izdajracun.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.viewmodel.GoogleSignInViewModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";
    private static final int RC_SIGN_IN = 1000;

    private GoogleSignInClient googleSignInClient;
    private NavController navController;

    private Button signInButton;

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //getting references to UI
        signInButton = root.findViewById(R.id.sign_in_button);

        //adding onClickListeners
        signInButton.setOnClickListener(this);

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(GoogleSignInViewModel.scopes)
                        .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), signInOptions);

        navController = NavHostFragment.findNavController(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == signInButton){
            signInWithScopes();
        }
    }

    private void signInWithScopes() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            switch (resultCode){
                case RESULT_CANCELED:
                    Log.d(TAG, "sign_in canceled");
                    break;

                case RESULT_FIRST_USER:
                    Log.d(TAG, "sign_in first user");
                    handleSignInResult(data);
                    break;

                case RESULT_OK:
                    Log.d(TAG, "sign_in OK");
                    handleSignInResult(data);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + resultCode);
            }
        }
    }

    private void handleSignInResult(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.
                getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if(userSignInWithRequiredScopes(account)){
                navController.navigate(R.id.action_signInFragment_to_propertiesFragment);
            }

        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
        }

    }

    private boolean userSignInWithRequiredScopes(GoogleSignInAccount account){
        boolean signedInWithScopes = false;

        if(account != null){
            if(GoogleSignIn.hasPermissions(account, GoogleSignInViewModel.scopes)) {
                signedInWithScopes = true;
            }
        }

        return signedInWithScopes;
    }
}
