package com.example.tomaz.mybabytomaz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import util.Amamentacao;
import util.Atividade;
import util.Banho;
import util.Singleton;

public class DialogBanho extends DialogAmamentacao {

    private CheckBox [] checkBoxes = new CheckBox[5];
    private final int [] chkIds = {R.id.chk_shampoo,R.id.chk_condicionador,R.id.chk_sabonete,
                                    R.id.chk_creme,R.id.chk_hidratante};

    public DialogBanho(@NonNull Context context, int tema) {
        super(context, tema);
        titulo.setText("Banho");
        icone.setImageResource(R.drawable.icons8_banho_48);

        // inicializa os checks
        for(int i = 0; i < chkIds.length; i++)
            checkBoxes[i] = findViewById(chkIds[i]);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Banho banho = createAtividadeToStorage();

                // Adiciona a atividade ao Singleton
                Singleton.getInstance().getAtividades().add(banho);
                Singleton.getInstance().saveAtividades(getContext());
                if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(banho))
                    MainActivity.getAdapterListaDeAtividades().getAtividades().add(banho);
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void setDialogToEdit(final Banho banho, final int position, final ArrayList<Atividade> atividades){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date data = banho.getDataInicial();
        Time horaInicial = banho.getHoraInicio();
        Time horaFinal = banho.getHoraTermino();

        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.getInstance().getAtividades().remove(banho);
                atividades.remove(banho);
                Singleton.getInstance().saveAtividades(getContext());
                MainActivity.updateList();
                Toast.makeText(getContext(),
                        "Atividade removida com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        layoutBotoes.addView(delete, 1);


        // Seta o conteúdo do dialog de acordo com os dados da atividade
        this.data.setText(dataFormat.format(data).toString());
        this.anotacoes.setText(banho.getAnotacoes().toString());
        this.horaInicio.setText(configureTime(horaInicial));
        this.horaTermino.setText(configureTime(horaFinal));
        ArrayList<String>produtos = new ArrayList<String>(Arrays.asList(banho.getProdutosUtilizados()));
        for(CheckBox chk : checkBoxes){
            if(produtos.contains(chk.getText().toString()))
                chk.setChecked(true);
        }

        //Altera o comportamento do botão confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Banho edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(banho);
                Singleton.getInstance().getAtividades().remove(banho);
                atividades.remove(banho);
                atividades.add(position,edit);
                if(!Singleton.getInstance().getAtividades().contains(edit))
                    Singleton.getInstance().getAtividades().add(indexInSingleton,edit);
                Singleton.getInstance().saveAtividades(v.getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade editada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

    }

    private Banho createAtividadeToStorage(){
        int horaI,minI,segI,horaF,minF,segF;
        Date date = new Date(data.getText().toString());
        ArrayList<String> produtosMarcados = new ArrayList<String>();

        String [] hInit = horaInicio.getText().toString().split(":");
        if(hInit[2].contains(" PM")){
            hInit[2] = hInit[2].replace(" PM", "");
            horaI = Integer.parseInt(hInit[0]) + 12;
        }
        else {
            hInit[2] = hInit[2].replace(" AM", "");
            horaI = Integer.parseInt(hInit[0]);
        }
        minI = Integer.parseInt(hInit[1]);
        segI = Integer.parseInt(hInit[2]);

        String [] hFinal = horaTermino.getText().toString().split(":");
        if(hFinal[2].contains(" PM")){
            hFinal[2] = hFinal[2].replace(" PM", "");
            horaF = Integer.parseInt(hFinal[0]) + 12;
        }
        else {
            hFinal[2] = hFinal[2].replace(" AM", "");
            horaF = Integer.parseInt(hFinal[0]);
        }
        minF = Integer.parseInt(hFinal[1]);
        segF = Integer.parseInt(hFinal[2]);

        for(CheckBox ckb : checkBoxes){
            if(ckb.isChecked())
                produtosMarcados.add(ckb.getText().toString());
        }

        String [] marcados = produtosMarcados.toArray(new String[produtosMarcados.size()]);

        Banho banho = new Banho(date,
                anotacoes.getText().toString(),new Time(horaI,minI,segI),
                new Time(horaF,minF,segF),marcados);

        return banho;
    }
}
