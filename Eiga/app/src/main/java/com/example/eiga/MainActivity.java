package com.example.eiga;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eiga.db.FavoriteHelper;
import com.example.eiga.ui.main.NavigationFragment;

public class MainActivity extends AppCompatActivity {
    public static final int NOTIF_REQUEST_CODE = 200;
    public static final String ACTION_GET_RELEASE = "action_get_release";

    private FavoriteHelper favoriteHelper;

    private NavigationFragment navigationFragment = new NavigationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Eiga");
        }
        toolbar.inflateMenu(R.menu.main_menu);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, navigationFragment, "nav")
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent actionChangeIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(actionChangeIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
