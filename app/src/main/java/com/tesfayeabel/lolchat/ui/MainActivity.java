package com.tesfayeabel.lolchat.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tesfayeabel.lolchat.ChatService;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.adapter.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends LOLChatActivity {
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final ActionBar actionBar = getActionBar();
        ArrayList<LOLChatFragment> fragments = new ArrayList<LOLChatFragment>();
        fragments.add(new MainFragment());
        fragments.add(new ConversationsFragment());
        fragments.add(new SummonerSearchFragment());
        viewPager.setOffscreenPageLimit(fragments.size() - 1);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager(), fragments));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
                if (getLolChat() != null)
                    adapter.getRegisteredFragment(position).onChatConnected(getLolChat());
            }

            @Override
            public void onPageScrolled(int i, float f, int f1) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        ActionBar.TabListener tl = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.lolchat_friend).setTabListener(tl));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.lolchat_recent).setTabListener(tl));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.lolchat_search).setTabListener(tl));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_leave:
                finish();
                stopService(new Intent(this, ChatService.class));
                Intent mainIntent = new Intent(this, LoginActivity.class);
                mainIntent.setAction(Intent.ACTION_MAIN);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(mainIntent);
                return true;
            case R.id.action_about:
                Intent aboutActivity = new Intent(this, AboutActivity.class);
                startActivity(aboutActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onChatConnected() {
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
        LOLChatFragment fragment = adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onChatConnected(getLolChat());
        }
    }
}