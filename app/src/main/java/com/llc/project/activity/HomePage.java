package com.llc.project.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.llc.project.MainActivity;
import com.llc.project.R;
import com.llc.project.adapters.NavigationAdapter;
import com.llc.project.model.NavigationModel;


public class HomePage extends FragmentActivity {

    //View Elements
    DrawerLayout drawerLayout;
    LinearLayout mLlDrawerList;
    ListView mLvNavigation;
    NavigationAdapter mAdapter;
    NavigationModel[] drawerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Initialization();
        InitializationViews();
        SetAdapter();
        addSliderMenuData();
        SetClickEvents();

        //to open on homepage
        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    //Add slider menuu data using model
    void addSliderMenuData() {
        drawerItem[0] = new NavigationModel("Home", R.drawable.ic_menu_add);
    }

    //Listview adding on adapter
    void SetAdapter() {
        mAdapter = new NavigationAdapter(this, R.layout.adapter_navigation_item, drawerItem);
        mLvNavigation.setAdapter(mAdapter);
    }

    void Initialization() {
        drawerItem = new NavigationModel[1];
    }

    // View initilization
    void InitializationViews() {
        mLlDrawerList = (LinearLayout) findViewById(R.id.testing);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLvNavigation = (ListView) findViewById(R.id.lv_navigation);

        //Slider menu header added
        View header = getLayoutInflater().inflate(R.layout.app_common_navigation_header, null);
        mLvNavigation.addHeaderView(header);

    }

    void SetClickEvents() {

        mLvNavigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open page for navigation click event
                displayView(position);
            }
        });
    }

    /**
     * Select a fragement based on clicking a navigation menu
     * @param position
     */
    public void displayView(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragment = new MainActivity();
                break;

            default:
                fragment = new MainActivity();
                break;
        }
        if (fragment != null) {

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
            drawerLayout.closeDrawer(mLlDrawerList);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
