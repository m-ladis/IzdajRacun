package hr.ml.izdajracun.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import hr.ml.izdajracun.R;
import hr.ml.izdajracun.viewmodel.GoogleSignInViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private GoogleSignInViewModel googleSignInViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        googleSignInViewModel = ViewModelProviders.of(this).get(GoogleSignInViewModel.class);

        googleSignInViewModel.authState
                .observe(this, new Observer<GoogleSignInViewModel.AuthState>() {
            @Override
            public void onChanged(GoogleSignInViewModel.AuthState authState) {
                switch (authState){
                    case UNAUTHENTICATED:
                        Log.d(TAG, "UNAUTHENTICATED");

                        navController.navigate(R.id.signInFragment, null,
                                new NavOptions.Builder()
                                        .setPopUpTo(R.id.propertiesFragment, true)
                                        .build());

                        break;

                    case AUTHENTICATED:
                        Log.d(TAG, "AUTHENTICATED");
                }
            }
        });

        googleSignInViewModel.checkAuthState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu_item:
                sign_out();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sign_out() {
        googleSignInViewModel.signOut();
    }
}
