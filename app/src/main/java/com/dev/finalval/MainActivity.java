package com.dev.finalval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    boolean isHome=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().add(R.id.frame,new ListaFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentTransaction fragmentSel = getSupportFragmentManager().beginTransaction();
                    switch (menuItem.getItemId()){
                        case R.id.home:
                            fragmentSel.replace(R.id.frame,new ListaFragment());
                            isHome=true;
                            break;
                        case R.id.user:
                            fragmentSel.replace(R.id.frame,new UsuarioFragment());
                            isHome = false;
                            break;
                        case R.id.map:
                            fragmentSel.replace(R.id.frame,new MapFragment());
                            isHome=false;
                            break;

                    }

                    fragmentSel.commit();
                    return true;
                }

            };


    @Override
    public void onBackPressed() {
        if (!isHome) { //Si la vista actual no es el fragment Home
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
            bottomNavigationView.setSelectedItemId(R.id.home); //Selecciona el fragment Home
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Cerrar Sesión");
            //dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
    }

}
