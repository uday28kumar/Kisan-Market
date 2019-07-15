package com.example.uk.kisanmarket;
/* Developed by Uday Kumar (205116027)
    CA Department, MCA, NIT Trichy
    11/08/2018
 */

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class CerealActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cereal);

        // add toolbar
        toolbar = (Toolbar) findViewById(R.id.cereal_toolBar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.cereal_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.cereal_viewPager);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        //Adding Fragment
        viewPageAdapter.AddFragment(new FragmentRice(), "Rice");
        viewPageAdapter.AddFragment(new FragmentWheat(),"Wheat");
        viewPageAdapter.AddFragment(new FragmentPulces(),"Pulces");
        //adapter Setup
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_close) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
