package com.atiq.cmsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView cseDepartment, eeeDepartment, physicsDepartment, mathematicsDepartment, chemistryDepartment;
    private LinearLayout cseNoData, eeeNoData, physicsNoData, mathematicsNoData, chemistryNoData;
    private List<TeacherData> cseList, eeeList, physicsList, mathList, chemList;
    private TeacherAdapter adapter;
    private DatabaseReference databaseReference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        cseDepartment = findViewById(R.id.cseDepartment);
        eeeDepartment = findViewById(R.id.eeeDepartment);
        physicsDepartment = findViewById(R.id.physicsDepartment);
        mathematicsDepartment = findViewById(R.id.mathematicsDepartment);
        chemistryDepartment = findViewById(R.id.chemistryDepartment);

        cseNoData = findViewById(R.id.cseNoData);
        eeeNoData = findViewById(R.id.eeeNoData);
        physicsNoData = findViewById(R.id.physicsNoData);
        mathematicsNoData = findViewById(R.id.mathematicsNoData);
        chemistryNoData = findViewById(R.id.chemistryNoData);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("teachers");
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddTeacher.class));
            }
        });
        
        cseDepartment();
        eeeDepartment();
        physicsDepartment();
        mathematicsDepartment();
        chemistryDepartment();

    }

    private void chemistryDepartment() {
        dbRef = databaseReference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chemList = new ArrayList<>();
                if(!snapshot.exists()){
                    chemistryNoData.setVisibility(View.VISIBLE);
                    chemistryDepartment.setVisibility(View.GONE);
                }else{
                    chemistryNoData.setVisibility(View.GONE);
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dSnapshot:snapshot.getChildren()){
                        TeacherData data = dSnapshot.getValue(TeacherData.class);
                        chemList.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(chemList, UpdateFaculty.this,"Mathematics");
                    chemistryDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mathematicsDepartment() {
        dbRef = databaseReference.child("Mathematics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mathList = new ArrayList<>();
                if(!snapshot.exists()){
                    mathematicsNoData.setVisibility(View.VISIBLE);
                    mathematicsDepartment.setVisibility(View.GONE);
                }else{
                    mathematicsNoData.setVisibility(View.GONE);
                    mathematicsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dSnapshot:snapshot.getChildren()){
                        TeacherData data = dSnapshot.getValue(TeacherData.class);
                        mathList.add(data);
                    }
                    mathematicsDepartment.setHasFixedSize(true);
                    mathematicsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(mathList, UpdateFaculty.this,"Mathematics");
                    mathematicsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void physicsDepartment() {
        dbRef = databaseReference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                physicsList = new ArrayList<>();
                if(!snapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else{
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dSnapshot:snapshot.getChildren()){
                        TeacherData data = dSnapshot.getValue(TeacherData.class);
                        physicsList.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(physicsList, UpdateFaculty.this,"Physics");
                    physicsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eeeDepartment() {
        dbRef = databaseReference.child("EEE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eeeList = new ArrayList<>();
                if(!snapshot.exists()){
                    eeeNoData.setVisibility(View.VISIBLE);
                    eeeDepartment.setVisibility(View.GONE);
                }else{
                    eeeNoData.setVisibility(View.GONE);
                    eeeDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dSnapshot:snapshot.getChildren()){
                        TeacherData data = dSnapshot.getValue(TeacherData.class);
                        eeeList.add(data);
                    }
                    eeeDepartment.setHasFixedSize(true);
                    eeeDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(eeeList, UpdateFaculty.this, "EEE");
                    eeeDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cseDepartment() {
        dbRef = databaseReference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cseList = new ArrayList<>();
                if(!snapshot.exists()){
                    cseNoData.setVisibility(View.VISIBLE);
                    cseDepartment.setVisibility(View.GONE);
                }else{
                    cseNoData.setVisibility(View.GONE);
                    cseDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dSnapshot:snapshot.getChildren()){
                        TeacherData data = dSnapshot.getValue(TeacherData.class);
                        cseList.add(data);
                    }
                    cseDepartment.setHasFixedSize(true);
                    cseDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(cseList, UpdateFaculty.this, "CSE");
                    cseDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}