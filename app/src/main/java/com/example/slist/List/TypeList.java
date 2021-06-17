package com.example.slist.List;

import android.widget.ImageView;

import com.example.slist.Main.MainActivity;
import com.example.slist.R;
import com.example.slist.db.ListDAO;

import java.util.ArrayList;
import java.util.List;

public class TypeList {
    private ListDAO helper;

    public TypeList(ListDAO helper) {
        this.helper = helper;
    }

    public void tipoCompraIcone(String tipo, ImageView imageView){
        if(tipo.equals("tarefaDiario")){
            imageView.setImageResource(R.drawable.diario);
        }
        if(tipo.equals("tarefaSemanal")){
            imageView.setImageResource(R.drawable.semanal);
        }
        if(tipo.equals("tarefaMensal")){
            imageView.setImageResource(R.drawable.mensal);
        }

    }

    public void setTypeList(String tipo){
        if(tipo.equals("tarefaDiario")){
            MainActivity.listaPrincipal = helper.getList(tipo);
        }
        if(tipo.equals("tarefaSemanal")){
            MainActivity.listaPrincipal = helper.getList(tipo);
        }
        if(tipo.equals("tarefaMensal")){
            MainActivity.listaPrincipal = helper.getList(tipo);
        }
    }

}
