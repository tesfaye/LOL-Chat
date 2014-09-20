package com.tesfayeabel.lolchat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.ui.LOLChatFragment;
import com.tesfayeabel.lolchat.ui.LoginActivity;
import com.tesfayeabel.lolchat.ui.MainFragment;
import com.tesfayeabel.lolchat.ui.SummonerSearch;
import com.tesfayeabel.lolchat.ui.adapter.RecentConversations;

import java.util.ArrayList;

public class LOLChatMain extends Activity implements ServiceConnection {
    private ViewPager viewPager;
    private ArrayList<LOLChatFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final ActionBar actionBar = getActionBar();
        fragments = new ArrayList<LOLChatFragment>();
        fragments.add(new MainFragment());
        fragments.add(new RecentConversations());
        fragments.add(new SummonerSearch());
        viewPager.setOffscreenPageLimit(fragments.size() - 1);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
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
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.lolchat_conversation).setTabListener(tl));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.lolchat_search).setTabListener(tl));
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        ChatService chatService = ((ChatService.LocalBinder) service).getService();
        LolChat lolChat = chatService.getLolChat();
        fragments.get(viewPager.getCurrentItem()).onChatConnected(lolChat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
    }
}