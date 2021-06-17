package com.example.slist.Main;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.slist.List.ListInfo;
import com.example.slist.List.TypeList;
import com.example.slist.R;
import com.example.slist.db.ListDAO;
import com.example.slist.db.TaskContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static ListDAO helper;
    public static List<ListInfo> listaPrincipal;

    private Adapter adapter;
    private ListInfo listAlert;

    private RecyclerView listaRecy;
    private ImageView check;
    private ImageButton options;

    private final  int NEW_ITEM = 0;
    private final int ALTER_ITEM = 1;
    private final Integer opcao [] = {R.string.editar, R.string.excluir};

    private TypeList typeList;
    private SugestionList sugestionList;


    private static String tipoCompra;

    public static void setHelper(ListDAO helper) {
        MainActivity.helper = helper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tipoCompra = getIntent().getStringExtra("key");

        helper = new ListDAO(this);
        typeList = new TypeList(helper);
        typeList.setTypeList(tipoCompra);


        listaRecy = findViewById(R.id.listaRecy);
        check = findViewById(R.id.imageViewCheck);
        options = findViewById(R.id.imageButtonOption);

        listaRecy.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaRecy.setLayoutManager(llm);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(NEW_ITEM, null);
            }
        });


        sugestionList = new SugestionList(helper);
            sugestionList.listaSugestao(listaPrincipal, tipoCompra);


        adapter = new Adapter(listaPrincipal);


        listaRecy.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String tarefa = listaPrincipal.get(position).getNome();
                String sqlQuery;
                if(listaPrincipal.get(position).getSugestion().equals("1")){
                    sqlQuery = String.format("UPDATE %s SET %s = 0, %s =0 WHERE %s = '%s'", tipoCompra,
                            TaskContract.Columns.SUGESTION, TaskContract.Columns.OK,TaskContract.Columns.NOME, tarefa);
                    atualizaBD(sqlQuery);
                    refresh();
                }else{
                    switch (listaPrincipal.get(position).getOk()) {
                        case "0":
                            listaPrincipal.get(position).setOk("1");
                            sqlQuery = String.format("UPDATE %s SET %s = 1 WHERE %s = '%s'", tipoCompra,
                                    TaskContract.Columns.OK, TaskContract.Columns.NOME, tarefa);
                            atualizaBD(sqlQuery);
                            break;
                        case "1":
                            listaPrincipal.get(position).setOk("0");
                            sqlQuery = String.format("UPDATE %s SET %s = 0 WHERE %s = '%s'", tipoCompra,
                                    TaskContract.Columns.OK, TaskContract.Columns.NOME, tarefa);
                            atualizaBD(sqlQuery);
                    }
                }
                adapter = new Adapter(listaPrincipal);
                adapter.setOnItemClickListener(this);
                listaRecy.setAdapter(adapter);
                if(helper.listComplete(listaPrincipal)){
                    showFinish();
                }
            }

            @Override
            public void onOptions(View view, int position) {
                showOptions(listaPrincipal.get(position));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlert(final int caso, final ListInfo listInfo){
        listAlert = new ListInfo();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View alertDialog = getLayoutInflater().inflate(R.layout.alert_list, null);

        final ImageView icone = alertDialog.findViewById(R.id.imageViewIcone);
        final EditText editTextNome = alertDialog.findViewById(R.id.editTextNome);
        final EditText editTextQuant = alertDialog.findViewById(R.id.editTextQuant);
        final TextView title = alertDialog.findViewById(R.id.textAdd);

        typeList.tipoCompraIcone(tipoCompra, icone);

        builder.setView(alertDialog);

        if(caso == ALTER_ITEM){
            title.setText(R.string.alterar);
            editTextNome.setHint(listInfo.getNome().toString());
            editTextQuant.setHint(listInfo.getQuantidade().toString());
        }

        builder.setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nome = editTextNome.getText().toString();
                String quantidade = editTextQuant.getText().toString();
                switch (caso){
                    case NEW_ITEM:
                        saveItem(nome, quantidade);
                        break;
                    case ALTER_ITEM:
                        long id = listInfo.getId();
                        String sqlQuery = String.format("UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = %d", tipoCompra, TaskContract.Columns.NOME,nome,
                                TaskContract.Columns.QUANTIA, quantidade, TaskContract.Columns._ID, id);
                        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                        sqLiteDatabase.execSQL(sqlQuery);
                        refresh();
                        break;
                }
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    private void alertDeletar(final ListInfo list){
        String nome = list.getNome();
        if(list.getPeso() > 2) {
            String sql = String.format("UPDATE %s SET %s = -1, %s = %s-1 WHERE %s = '%s'", tipoCompra, TaskContract.Columns.OK,
                    TaskContract.Columns.PESO, TaskContract.Columns.PESO, TaskContract.Columns.NOME, nome);
            SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
        }else{
            String sql = String.format("DELETE FROM %s WHERE %s = '%s'", tipoCompra, TaskContract.Columns.NOME, nome);
            SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
        }
        typeList.setTypeList(tipoCompra);

        adapter = new Adapter(listaPrincipal);
        listaRecy.setAdapter(adapter);
    }

    private void atualizaBD(String sqlQuery){
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(sqlQuery);
    }

    private void showOptions(final ListInfo listInfo){
        if(listInfo.getSugestion().equals("1")){
            alertDeletar(listInfo);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(listInfo.getNome());
            builder.setItems(R.array.opcao, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            showEdit(listInfo);
                            break;
                        case 1:
                            alertDeletar(listInfo);
                            break;
                    }
                }
            });

            builder.create();
            builder.show();
        }
    }

    private  void showEdit(ListInfo listInfo) {
        showAlert(ALTER_ITEM, listInfo);
    }

    private boolean saveItem(String nome, String quantidade){
        if (nome.equals("")) {
            Toast.makeText(MainActivity.this, R.string.sem_nome, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!helper.inserirLista(nome, quantidade, tipoCompra))
            Toast.makeText(MainActivity.this, R.string.item_incluso, Toast.LENGTH_SHORT).show();
        refresh();
        return true;
    }

    private void refresh(){
        typeList.setTypeList(tipoCompra);
        adapter = new Adapter(listaPrincipal);
        listaRecy.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showFinish(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View alertDialog = getLayoutInflater().inflate(R.layout.alert_finish, null);

        builder.setView(alertDialog);

        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sugestionList.finalizaLista(listaPrincipal, tipoCompra);
                finish();

            }
        });
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        listaPrincipal.clear();
    }


}
