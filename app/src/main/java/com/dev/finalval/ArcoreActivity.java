package com.dev.finalval;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.squareup.picasso.Picasso;

public class ArcoreActivity extends AppCompatActivity {

    private ArFragment arFragment;
    Bundle bundle;
    String dato="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcore);

        //bundle = getIntent().getExtras();
       // if(bundle!=null){
           // Comida comida = (Comida) bundle.getSerializable("modelo");
            //dato = comida.getNombre();
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
            arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                Anchor anchor = hitResult.createAnchor();
                ModelRenderable.builder()
                        .setSource(this, Uri.parse("pizza.sfb"))
                        .build()
                        .thenAccept(modelRenderable -> addModelScene(anchor, modelRenderable))
                        .exceptionally(throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage())
                                    .show();
                            return null;
                        });
            });

       // }



    }

    private void addModelScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}
