package com.example.wimm;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.wimm.Fragments.CategoriesFragement;
import com.example.wimm.Fragments.SettingsFragement;
import com.example.wimm.Helper.DataAccess;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Update User Data
        DataAccess.UpdateCategoriesList();



        drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //when you rotate the device this will fix a bug
        if (savedInstanceState == null)
        {
            //Categories fragment starts when the app is opened
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragement()).commit();
            navigationView.setCheckedItem(R.id.nav_categories);
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.nav_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragement()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragement()).commit();
                break;
            default:
                Toast.makeText(this, "Thanks for using our application!", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}