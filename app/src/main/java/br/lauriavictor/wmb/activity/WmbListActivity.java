package br.lauriavictor.wmb.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.adapter.ViewPagerAdapter;
import br.lauriavictor.wmb.fragment.RatingFragment;
import br.lauriavictor.wmb.fragment.TypeFragment;

import android.support.design.widget.TabLayout;

public class WmbListActivity extends AppCompatActivity {

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sobre Cervejas");

        tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(new TypeFragment(), "tipos mais procurados");
        viewPagerAdapter.AddFragment(new RatingFragment(), "mais avaliadas");

        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }
}
