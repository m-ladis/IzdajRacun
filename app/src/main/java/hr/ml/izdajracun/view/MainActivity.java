package hr.ml.izdajracun.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import hr.ml.izdajracun.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
