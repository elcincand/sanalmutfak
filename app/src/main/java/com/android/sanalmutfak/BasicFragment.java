package com.android.sanalmutfak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasicFragment extends Fragment {

    private ImageButton madd;
    private ListView mfood;
    private ListView mbozuk;
    private String tempKey2;
    ArrayList<DataModelBasic> dataModelsBasic = new ArrayList<DataModelBasic>();
    private List<String> keyarray = new ArrayList<>();

    private ListAdapterBasic adapterbasic;

    DatabaseReference mbasicRef;

    public BasicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        mfood = (ListView) view.findViewById(R.id.food);
        // mbozuk = (ListView) view.findViewById(R.id.bozuk);
        madd = (ImageButton) view.findViewById(R.id.foodAddButton);

        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFoodFragment fragment = new AddFoodFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        displayFood();

        return view;
    }


    public void displayFood() {
        mbasicRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sanalmutfak-d81ad.firebaseio.com/kitchens/"
                + LoginFragment.logkitchen + "/foods/");

        adapterbasic = new ListAdapterBasic(dataModelsBasic, getActivity(), BasicFragment.this);
        mfood.setAdapter(adapterbasic);


        mbasicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String bname = (String) childSnapShot.child("foodname").getValue();
                    String bskt = (String) childSnapShot.child("skt").getValue();
                   // String but = (String) childSnapShot.child("ut").getValue();

                    tempKey2 = dataSnapshot.child(childSnapShot.getKey()).getKey().toString();
                    Log.d("keditemp", tempKey2);

                    keyarray.add(tempKey2);
                    Log.d("kedi", String.valueOf(keyarray));


                    dataModelsBasic.add(new DataModelBasic(bname, bskt));
                    adapterbasic.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void removeItemBasic(int position) {
        DataModelBasic toRemovebasic = adapterbasic.getItem(position);
        adapterbasic.remove(toRemovebasic);
        adapterbasic.notifyDataSetChanged();
        Log.d("kediremove", keyarray.get(position));
        mbasicRef.child(keyarray.get(position)).removeValue();
        BasicFragment fragment = new BasicFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }



 public void addShopListBasic(int position){
     DataModelBasic toRemovebasic = adapterbasic.getItem(position);
     adapterbasic.add(toRemovebasic);
     adapterbasic.notifyDataSetChanged();
     Log.d("kediremove", keyarray.get(position));
     mbasicRef.child(keyarray.get(position)).removeValue();
     BasicFragment fragment = new BasicFragment();
     FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
     fragmentTransaction.replace(R.id.main_container, fragment);
     fragmentTransaction.addToBackStack(null);
     fragmentTransaction.commit();

 }

}