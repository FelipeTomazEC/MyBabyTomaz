package com.example.tomaz.mybabytomaz;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import util.Bebe;
import util.Singleton;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNomeDoBebe;
    private EditText edtDataDeNascimento;
    private RadioGroup rgSexo;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        edtNomeDoBebe = findViewById(R.id.edtNomeDoBebe);
        edtDataDeNascimento = findViewById(R.id.edtDataDeNascimento);
        rgSexo = findViewById(R.id.rgSexo);
        rbFeminino = findViewById(R.id.rbFeminino);
        rbMasculino = findViewById(R.id.rbMasculino);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Singleton.getInstance().loadBaby(this);

        if(!(Singleton.getInstance().getBebe() == null)){
            Intent it = new Intent(this, MainActivity.class);
            Log.e("CSI", "Entrou no OnStart dessa activity");
            startActivity(it);
            finish();
        }
    }

    public void cadastrarBebe(View view) {
        String nome = edtNomeDoBebe.getText().toString();
        String data = edtDataDeNascimento.getText().toString();
        String sexo;

        if(!nome.equals("") && !data.equals("")) {

            if (rgSexo.getCheckedRadioButtonId() == rbMasculino.getId())
                sexo = "Masculino";
            else
                sexo = "Feminino";

            Singleton.getInstance().setBebe(new Bebe(nome, data, sexo));
            Singleton.getInstance().saveBaby(this);
            Toast.makeText(this, "Cadastro realizado com sucesso.",
                    Toast.LENGTH_LONG).show();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }else{
            Toast.makeText(this, "Preencha todos os dados !", Toast.LENGTH_SHORT).show();
        }

    }


    public void mostrarCalendario(View view) {
        final Calendar calendario = Calendar.getInstance();
        DatePickerDialog dialogCalendario  = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(Calendar.YEAR,year);
                calendario.set(Calendar.MONTH,month);
                calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                edtDataDeNascimento.setText(dataFormat.format(calendario.getTime()));
            }
        },calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));
                
                dialogCalendario.show();
    }
}
