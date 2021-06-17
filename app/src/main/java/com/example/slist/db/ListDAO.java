package com.example.slist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.slist.List.ListInfo;

import java.util.ArrayList;
import java.util.List;

public class ListDAO extends SQLiteOpenHelper {


    public ListDAO(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql= String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s BOOLEAN, %s BOOLEAN)", TaskContract.TABLE, TaskContract.Columns.NOME,
                TaskContract.Columns.QUANTIA, TaskContract.Columns.CATEGORIA, TaskContract.Columns.PESO, TaskContract.Columns.OK, TaskContract.Columns.SUGESTION);
        sqLiteDatabase.execSQL(sql);
        String sqlDiario= String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s BOOLEAN, %s BOOLEAN)", TaskContract.TABLE_SEMANAL, TaskContract.Columns.NOME,
                TaskContract.Columns.QUANTIA, TaskContract.Columns.CATEGORIA, TaskContract.Columns.PESO, TaskContract.Columns.OK, TaskContract.Columns.SUGESTION);
        sqLiteDatabase.execSQL(sqlDiario);
        String sqlMensal= String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s BOOLEAN, %s BOOLEAN)", TaskContract.TABLE_MENSAL, TaskContract.Columns.NOME,
                TaskContract.Columns.QUANTIA, TaskContract.Columns.CATEGORIA, TaskContract.Columns.PESO, TaskContract.Columns.OK, TaskContract.Columns.SUGESTION);
        sqLiteDatabase.execSQL(sqlMensal);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
    }


    public List<ListInfo> getList(String tipo){
        List<ListInfo> listaPrincipal = new ArrayList<>();
        Cursor cursor = getCursor(tipo);

        while(cursor.moveToNext()){
            if(!cursor.getString(cursor.getColumnIndex("ok")).equals("-1") && cursor.getInt(cursor.getColumnIndex("peso")) > 0) {
                ListInfo c = new ListInfo();
                c.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                c.setQuantidade(cursor.getString(cursor.getColumnIndex("quantia")));
                c.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
                c.setPeso(cursor.getInt(cursor.getColumnIndex("peso")));
                c.setOk(cursor.getString(cursor.getColumnIndex("ok")));
                c.setSugestion(cursor.getString(cursor.getColumnIndex("sugestion")));
                listaPrincipal.add(c);
            }
        }

        cursor.close();

        return listaPrincipal;
    }

    private Cursor getCursor(String tipo){
        Cursor cursor =  null;
        if(tipo.equals("tarefaDiario")){
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TaskContract.TABLE + " ORDER BY " +TaskContract.Columns.SUGESTION+ " DESC "+ ";", null);
        }
        if(tipo.equals("tarefaSemanal")){
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TaskContract.TABLE_SEMANAL + " ORDER BY " +TaskContract.Columns.SUGESTION+ " DESC "+ ";", null);
        }
        if(tipo.equals("tarefaMensal")){
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TaskContract.TABLE_MENSAL + " ORDER BY " +TaskContract.Columns.SUGESTION+ " DESC "+ ";", null);
        }
        return cursor;
    }

    public boolean listComplete(List<ListInfo> lista){
        for(int positon = 0; positon < lista.size(); positon++){
            if(lista.get(positon).getOk().equals("0") && lista.get(positon).getSugestion().equals("0"))
                return false;
        }
        return true;
    }

    public boolean inserirLista (String nome, String quantidade, String tipoCompra){
        Cursor cursor = getCursor(tipoCompra);
        if(!contemLista(cursor, nome)) {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(TaskContract.Columns.NOME, nome);
            values.put(TaskContract.Columns.QUANTIA, quantidade);
            values.put(TaskContract.Columns.OK, "0");
            values.put(TaskContract.Columns.PESO, 1);
            values.put(TaskContract.Columns.SUGESTION, 0);
            getWritableDatabase().insert(tipoCompra, null, values);
            return true;
        }
        cursor = getCursor(tipoCompra);
        if(contemListaSugestao(cursor, nome, tipoCompra))
            return true;
        return false;
    }

    public boolean contemLista(Cursor cursor, String nome){
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("nome")).equals(nome)){
                return true;
            }
        }
        return false;
    }


    public boolean contemListaSugestao(Cursor cursor, String nome, String tipoCompra){
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("nome")).equals(nome)){
                String sqlQuery = String.format("UPDATE %s SET %s = 0, %s = %s+1, %s =0 WHERE %s = '%s'", tipoCompra,
                        TaskContract.Columns.OK, TaskContract.Columns.PESO, TaskContract.Columns.PESO,TaskContract.Columns.SUGESTION, TaskContract.Columns.NOME, nome);
                SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                sqLiteDatabase.execSQL(sqlQuery);
                return true;
            }
        }
        return false;
    }

    public List<ListInfo> getListSugestion(String tipo){
        List<ListInfo> listaAuxiliar = new ArrayList<>();
        Cursor cursor = getCursor(tipo);

        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("ok")).equals("-1") && cursor.getInt(cursor.getColumnIndex("peso")) > 2) {
                ListInfo c = new ListInfo();
                String sqlQuery = String.format("UPDATE %s SET %s = 0 WHERE %s = '%s'", tipo,
                        TaskContract.Columns.OK, TaskContract.Columns.NOME, cursor.getColumnIndex("nome"));
                SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                sqLiteDatabase.execSQL(sqlQuery);
                c.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                c.setQuantidade(cursor.getString(cursor.getColumnIndex("quantia")));
                c.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
                c.setPeso(cursor.getInt(cursor.getColumnIndex("peso")));
                c.setOk(cursor.getString(cursor.getColumnIndex("ok")));
                c.setSugestion(cursor.getString(cursor.getColumnIndex("sugestion")));
                listaAuxiliar.add(c);
            }
        }

        cursor.close();

        return listaAuxiliar;
    }




}
