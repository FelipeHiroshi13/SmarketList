package com.example.slist.Main;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.slist.List.ListInfo;
import com.example.slist.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ListaViewHolder>{
    private List<ListInfo> lista;
    private static RecyclerViewClickListener mListener;

    Adapter( List<ListInfo> lista){
        this.lista = lista;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
        void onOptions(View view, int position);
    }


    public void setOnItemClickListener(RecyclerViewClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_lista, parent, false);
        ListaViewHolder listV = new ListaViewHolder(v, mListener);
        return listV;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        ListInfo l = lista.get(position);
        if(!l.getQuantidade().equals("")){
            holder.nome.setText(l.getQuantidade() + ", " + l.getNome());
        }else {
            holder.nome.setText(l.getNome());
        }
        if(l.getOk().equals("1")){
            holder.check.setImageResource(R.drawable.checked);
            holder.nome.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.check.setImageResource(R.drawable.uncheck);
        }
        if(l.getSugestion().equals("1")){
            holder.nome.setPaintFlags(holder.nome.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.check.setImageResource(R.drawable.lamp);
            holder.options.setImageResource(R.drawable.cancelar);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    static class ListaViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        ImageView check;
        ImageButton options;
        TextView nome;

        ListaViewHolder (View v, final RecyclerViewClickListener listener){
            super (v);
            this.view = v;
            check = view.findViewById(R.id.imageViewCheck);
            nome = view.findViewById(R.id.textNome);
            options = view.findViewById(R.id.imageButtonOption);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, getAdapterPosition());
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onOptions(view, getAdapterPosition());
                }
            });
        }
    }
}
