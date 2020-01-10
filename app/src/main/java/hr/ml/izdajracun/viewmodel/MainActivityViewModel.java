package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivityViewModel extends AndroidViewModel {

    public enum AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    public static final Scope scopes = new Scope(Scopes.DRIVE_APPFOLDER);
    public MutableLiveData<AuthState> authState = new MutableLiveData<>();

    private Application application;
    private GoogleSignInClient googleSignInClient;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        this.application = application;


        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(MainActivityViewModel.scopes)
                        .build();

        googleSignInClient = GoogleSignIn.getClient(application, signInOptions);

        updateAuthState(application);
    }

    public void checkAuthState(){
        updateAuthState(application);
    }

    private void updateAuthState(Application app){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(app);
        authState.setValue(account != null ? AuthState.AUTHENTICATED : AuthState.UNAUTHENTICATED);
    }

    public void signOut(){
        googleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isCanceled()){
                            updateAuthState(application);
                        }

                        if(task.isSuccessful()){
                            updateAuthState(application);
                        }
                    }
                });
    }
}
