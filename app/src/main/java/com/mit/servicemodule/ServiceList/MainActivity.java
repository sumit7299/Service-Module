package com.mit.servicemodule.ServiceList;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mit.servicemodule.R;

public class MainActivity extends AppCompatActivity  {


    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        frame=findViewById(R.id.frame);
        loadFragment(new ServiceList());


    }

    public void loadFragment(Fragment fragment) {

        if (getContext() != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame, fragment);
            transaction.commit();

        }
    }

}