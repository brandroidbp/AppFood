package com.dev.finalval;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdaptador adaptador;

    private RecyclerView recSolicitado;
    private RecyclerViewAdaptador adapSolicitado;

    DatabaseReference databaseReference;

    public static List<Comida> menu = new ArrayList<>();
    public static List<Comida> solicitados = new ArrayList<>();

    public ListaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new RecyclerViewAdaptador(menu);
        recyclerView.setAdapter(adaptador);
        getData();

        recSolicitado = (RecyclerView) view.findViewById(R.id.recsol);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recSolicitado.setLayoutManager(horizontalLayoutManagaer);

        adapSolicitado = new RecyclerViewAdaptador(solicitados);
        recSolicitado.setAdapter(adapSolicitado);
        getSolicitados();


        adaptador.setOnItemClickListener(new RecyclerViewAdaptador.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getContext(), ArcoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("comida",menu.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        adapSolicitado.setOnItemClickListener(new RecyclerViewAdaptador.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {

            }
        });

        return view;
    }

    private void getSolicitados() {
        databaseReference.child("app").child("solicitados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                solicitados.clear();

                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Comida c = objSnapshot.getValue(Comida.class);
                    solicitados.add(c);

                }

                recSolicitado.setAdapter(adapSolicitado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData() {
        databaseReference.child("app").child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menu.clear();

                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Comida c = objSnapshot.getValue(Comida.class);
                    menu.add(c);

                }

                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
