package com.tesfayeabel.lolchat.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<LOLChatFragment> registeredFragments;
    private ArrayList<LOLChatFragment> fragments;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<LOLChatFragment> fragments) {
        super(fm);
        this.fragments = fragments;
        registeredFragments = new SparseArray<LOLChatFragment>();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LOLChatFragment fragment = (LOLChatFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public LOLChatFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}