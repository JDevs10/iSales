package com.iSales.pages.home.fragment;


import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iSales.R;
import com.iSales.helper.DebugMe;
import com.iSales.pages.ticketing.model.DebugItem;
import com.iSales.pages.ticketing.task.SaveLogs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.iSales.pages.home.fragment.AProposFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AProposFragment extends Fragment {
    private static final String TAG = com.iSales.pages.home.fragment.AProposFragment.class.getSimpleName();

    private ProgressDialog progressDialog;
    private TextView versionApp;
    int[] sampleImages = {R.drawable.logo_isales, R.drawable.logo_isales, R.drawable.logo_isales};

    public AProposFragment() {
        // Required empty public constructor
    }

    public static com.iSales.pages.home.fragment.AProposFragment newInstance() {
        com.iSales.pages.home.fragment.AProposFragment fragment = new com.iSales.pages.home.fragment.AProposFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SaveLogs(getContext()).writeLogFile(
                new DebugItem(
                        (System.currentTimeMillis()/1000),
                        "DEB",
                        AProposFragment.class.getSimpleName(),
                        "onCreate()",
                        "Response: ",
                        ""
                )
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_a_propos, container, false);
        versionApp = rootView.findViewById(R.id.fragment_a_propos_versionApp_tv);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PackageManager pm = getContext().getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(getContext().getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        versionApp.setText("Version: "+pInfo.versionName);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(0, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

}
