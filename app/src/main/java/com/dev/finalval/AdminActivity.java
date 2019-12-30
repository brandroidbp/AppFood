package com.dev.finalval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    FloatingActionButton fadd,fsave,fdelete;
    RecyclerViewAdaptador adaptador;
    RecyclerView recyclerView;
    public static List<Comida> comidaAdmin = new ArrayList<>();
    DatabaseReference databaseReference;
    Comida comidasel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        fadd = findViewById(R.id.fadd);
        fsave = findViewById(R.id.fsave);
        fdelete = findViewById(R.id.fdelete);

        recyclerView = (RecyclerView) findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new RecyclerViewAdaptador(comidaAdmin);
        recyclerView.setAdapter(adaptador);
        getData();

        adaptador.setOnItemClickListener(new RecyclerViewAdaptador.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                comidasel = comidaAdmin.get(position);
                Toast.makeText(AdminActivity.this, "Menu Seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

        fadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AddSaveActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comidasel!=null){
                    Intent intent = new Intent(AdminActivity.this, AddSaveActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("modelo", comidasel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

        fdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comidasel!=null){
                    databaseReference.child("app").child("menu").child(comidasel.getId()).removeValue();
                    comidasel=null;
                }
            }
        });

    }

    public void getData(){
        databaseReference.child("app").child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comidaAdmin.clear();

                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Comida c = objSnapshot.getValue(Comida.class);
                    comidaAdmin.add(c);

                }

                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
