package com.dev.finalval;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddSaveActivity extends AppCompatActivity {

    ImageView img;
    Button btnsubir;
    EditText txtnombre,txtprecio;
    DatabaseReference databaseReference;

    private static final int GALLERY_INTENT = 1;
    StorageReference myStorage;
    String downUrl="";
    Bundle recibido;
    Comida modrec = null;
    boolean modificar=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_save);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        myStorage = FirebaseStorage.getInstance().getReference();

        img = findViewById(R.id.imgfoto);
        btnsubir = findViewById(R.id.btnsubir);
        txtnombre = findViewById(R.id.txtnombre);
        txtprecio = findViewById(R.id.txtprecio);

        recibido = getIntent().getExtras();
        if(recibido!=null){
            modrec = (Comida) recibido.getSerializable("modelo");
            txtprecio.setText(modrec.getInfo()+"");
            txtnombre.setText(modrec.getNombre());
            downUrl = modrec.getImage();
            Picasso.with(this).load(downUrl).fit().into(img);
            modificar = true;
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        btnsubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(modificar){
                    anadir(modrec.getId(),downUrl);
                    Intent intent = new Intent(AddSaveActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String key = databaseReference.child("app").child("menu").push().getKey();
                    anadir(key,downUrl);
                    Intent intent = new Intent(AddSaveActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void anadir(String key,String url){
        String nombre = txtnombre.getText().toString();
        String precio = txtprecio.getText().toString();

        Comida mod = new Comida();
        mod.setId(key);
        mod.setNombre(nombre);
        mod.setInfo(Integer.parseInt(precio));
        mod.setImage(url);
        databaseReference.child("app").child("menu").child(key).setValue(mod);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            final Uri uri = data.getData();

            final StorageReference filePath = myStorage.child("Fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //mProgressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downUrl = uri.toString();

                        }
                    });

                }

            });

            Picasso.with(AddSaveActivity.this)
                    .load(uri)
                    .fit()
                    .into(img);
            btnsubir.setClickable(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddSaveActivity.this, AdminActivity.class);
        startActivity(intent);
    }
}
