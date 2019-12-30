package com.dev.finalval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdaptador extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder> {

    //NUEVO
    private OnItemClickListener mListener;
    Context ctx;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgcomida;

        public ViewHolder(@androidx.annotation.NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgcomida = (ImageView) itemView.findViewById(R.id.imgcomida);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }


    public List<Comida> comidaLista;

    public RecyclerViewAdaptador(){}

    public RecyclerViewAdaptador (List<Comida> comidaLista){
        this.comidaLista = comidaLista;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec,parent,false);

        ctx = parent.getContext();

        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int i) {
        Picasso.with(ctx).load(comidaLista.get(i).getImage()).fit().into(holder.imgcomida);

    }

    @Override
    public int getItemCount() {
        return comidaLista.size();
    }

}