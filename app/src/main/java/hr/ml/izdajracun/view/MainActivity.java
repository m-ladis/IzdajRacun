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
import hr.ml.izdajracun.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainActivityViewModel viewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.authState
                .observe(this, new Observer<MainActivityViewModel.AuthState>() {
            @Override
            public void onChanged(MainActivityViewModel.AuthState authState) {
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

        viewModel.checkAuthState();
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
        viewModel.signOut();
    }
}
