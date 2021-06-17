package com.example.slist.db;


public class TaskContract {
    public static final String DB_NAME = "com.example.listatarefas";
    public static int DB_VERSION = 1;
    public static final String TABLE = "tarefaDiario";
    public static final String TABLE_SEMANAL = "tarefaSemanal";
    public static final String TABLE_MENSAL = "tarefaMensal";



    public class Columns{
        public static final String _ID = "_id";
        public static final String NOME = "nome";
        public static final String QUANTIA = "quantia";
        public static final String CATEGORIA = "categoria";
        public static final String PESO = "peso";
        public static final String OK = "ok";
        public static final String SUGESTION = "sugestion";
    }
}
