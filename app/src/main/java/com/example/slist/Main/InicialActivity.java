package com.example.slist.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slist.R;
import com.example.slist.db.ListDAO;

public class InicialActivity extends AppCompatActivity {
    private Button btnDiario;
    private Button btnSemanal;
    private Button btnMensal;

    private ListDAO helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        btnDiario = findViewById(R.id.buttonDiario);
        btnSemanal = findViewById(R.id.buttonSemanal);
        btnMensal = findViewById(R.id.buttonMensal);
        helper = new ListDAO(this);
        MainActivity.setHelper(helper);
        click();
    }

    private void click(){
        btnDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InicialActivity.this, MainActivity.class);
                i.putExtra("key", "tarefaDiario");
                startActivity(i);
            }
        });
        btnSemanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InicialActivity.this, MainActivity.class);
                i.putExtra("key", "tarefaSemanal");
                startActivity(i);
            }
        });
        btnMensal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InicialActivity.this, MainActivity.class);
                i.putExtra("key", "tarefaMensal");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
