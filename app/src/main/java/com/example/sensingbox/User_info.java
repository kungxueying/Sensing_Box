package com.example.sensingbox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_info extends AppCompatActivity {

    ListView listView;
    String email;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    DS_user user = new DS_user();
    DS_dataset data = new DS_dataset();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference("user/" + email);
        mRef.addChildEventListener(new ChildEventListener() {//讀取

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("name"))
                    user.name= String.valueOf(dataSnapshot.getValue());
                if(dataSnapshot.getKey().equals("email"))
                    user.email= String.valueOf(dataSnapshot.getValue());

                   // user.name = mRef.child("name").getValue();
                   // user.email = mRef.child("email").getKey();

                    listView = (ListView) findViewById(R.id.list);

                    // Defined Array values to show in ListView
                    String[] values = new String[]{"Username: " + user.name,
                            //"Box Number:    "+data.boxID,
                            "Email: " + user.email,
                    };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(User_info.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, values);

                    // Assign adapter to ListView
                    listView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //t1.setText(""+dataSnapshot.getValue());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}