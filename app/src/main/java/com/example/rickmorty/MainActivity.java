package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.rickmorty.adapter.MainViewPagerAdapter;
import com.example.rickmorty.fragments.CharacterFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private ViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.t_toolbar);
        setSupportActionBar(toolbar);


        vPager = findViewById(R.id.vp_pager);
        setUpViewPager();
    }


    private void setUpViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        titles.add("Characters");
        titles.add("Locations");
        titles.add("Episodes");


        //TODO: add fragments
        //TODO: hacer los fragments de localizaciones y episodios
        fragments.add(CharacterFragment.newInstance());
        fragments.add(CharacterFragment.newInstance());
        fragments.add(CharacterFragment.newInstance());

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments, titles);

        vPager.setAdapter(mainViewPagerAdapter);
    }



}