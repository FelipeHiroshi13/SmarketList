package com.example.slist.Main;
import android.database.sqlite.SQLiteDatabase;

import com.example.slist.List.ListInfo;
import com.example.slist.db.ListDAO;
import com.example.slist.db.TaskContract;

import java.util.ArrayList;
import java.util.List;

public class SugestionList {
    private ListDAO helper;

    public SugestionList(ListDAO helper) {
        this.helper = helper;
    }

    public void listaSugestao(List<ListInfo> list, String tipoCompra){
        List<ListInfo> listaAxualiar = helper.getListSugestion(tipoCompra);
        list.addAll(listaAxualiar);
        for(int position = 0; position < list.size(); position++){
            if(list.get(position).getPeso() > 1 && list.get(position).getOk().equals("-1")){
                setSugestion(tipoCompra, list.get(position).getNome());
            }
//            if(list.get(position).getPeso() > 1 && list.get(position).getOk().equals("-1")){
//                setZero(tipoCompra, list.get(position).getNome());
//            }
        }
    }

    public void finalizaLista(List<ListInfo> list, String tipoCompra){
        for(int position = 0; position < list.size(); position++){
            if(list.get(position).getOk().equals("1")){
                setItemPoint(tipoCompra, list.get(position).getNome());
            }else{
                if(list.get(position).getPeso() > 1 && list.get(position).getSugestion().equals("1"))
                    dropPoint(tipoCompra, list.get(position).getNome());
                else
                    setMinus(tipoCompra, list.get(position).getNome());
            }

        }
    }

    private void setItemPoint(String tipoCompra, String nome){
        String sqlQuery = String.format("UPDATE %s SET %s = 1, %s = %s +1 WHERE %s = '%s'", tipoCompra,
                TaskContract.Columns.SUGESTION, TaskContract.Columns.PESO, TaskContract.Columns.PESO, TaskContract.Columns.NOME, nome);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sqlQuery);
    }

    private void dropPoint(String tipoCompra, String nome){
        String sqlQuery = String.format("UPDATE %s SET %s = 1, %s = 0, %s = %s -1 WHERE %s = '%s'", tipoCompra,
                TaskContract.Columns.SUGESTION, TaskContract.Columns.OK, TaskContract.Columns.PESO, TaskContract.Columns.PESO, TaskContract.Columns.NOME, nome);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sqlQuery);
    }

    private void setMinus(String tipoCompra, String nome){
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'", tipoCompra, TaskContract.Columns.NOME, nome);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    private void setSugestion(String tipoCompra, String nome){
        String sqlQuery = String.format("UPDATE %s SET %s = 0  WHERE %s = '%s'", tipoCompra,
                TaskContract.Columns.OK, TaskContract.Columns.NOME, nome);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sqlQuery);
    }

    private void setZero(String tipoCompra, String nome){
        String sqlQuery = String.format("UPDATE %s SET %s = 0  WHERE %s = '%s'", tipoCompra,
                TaskContract.Columns.OK, TaskContract.Columns.NOME, nome);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sqlQuery);
    }

    public boolean inicialTime(List<ListInfo> lista){
        if(lista.size() == 0)
            return true;
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getOk().equals("0")){
                return false;
            }
        }
        return true;
    }



}
