package com.tesfaye.lolchat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Abel Tesfaye on 8/24/2014.
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_lolchat_main, container, false);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = ((EditText) rootView.findViewById(R.id.section_label)).getText().toString();
                final String password = ((EditText) rootView.findViewById(R.id.section_label1)).getText().toString();
                ((LOLChatMain)getActivity()).bind(username, password);
            }
        });
        return rootView;
    }
}