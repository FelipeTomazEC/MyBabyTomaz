package com.example.tomaz.mybabytomaz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.Amamentacao;
import util.Atividade;
import util.Mamadeira;
import util.Singleton;

public class DialogMamadeira extends DialogAmamentacao{

    private EditText edtQuantidade;

    public DialogMamadeira(@NonNull Context context,int tema) {
        super(context,tema);
        edtQuantidade = this.findViewById(R.id.edt_quantidade);
        titulo.setText("Mamadeira");
        icone.setImageResource(R.drawable.icons8_mamadeira_48);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Mamadeira mamadeira = createAtividadeToStorage();
                    //Add a atividade criada no array de atividades e a salva
                    Singleton.getInstance().getAtividades().add(mamadeira);
                    if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(mamadeira))
                        MainActivity.getAdapterListaDeAtividades().getAtividades().add(mamadeira);
                    Singleton.getInstance().saveAtividades(getContext());
                    MainActivity.updateList();
                    Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                    dismiss();

                }catch (NumberFormatException e){
                    Toast.makeText(v.getContext(), "Preencha com um número válido !",
                            Toast.LENGTH_SHORT).show();
                    edtQuantidade.requestFocus();
                }


            }
        });
    }


    private Mamadeira createAtividadeToStorage() throws NumberFormatException {

        int horaI,minI,segI,horaF,minF,segF;
        Date date = new Date(data.getText().toString());
        double quant = 0;
        quant = Double.parseDouble(edtQuantidade.getText().toString());
        String [] dInit = horaInicio.getText().toString().split(":");
        if(dInit[2].contains(" PM")){
            dInit[2] = dInit[2].replace(" PM", "");
            horaI = Integer.parseInt(dInit[0]) + 12;
        }
        else {
            dInit[2] = dInit[2].replace(" AM", "");
            horaI = Integer.parseInt(dInit[0]);
        }
        minI = Integer.parseInt(dInit[1]);
        segI = Integer.parseInt(dInit[2]);

        String [] dFinal = horaTermino.getText().toString().split(":");
        if(dFinal[2].contains(" PM")){
            dFinal[2] = dFinal[2].replace(" PM", "");
            horaF = Integer.parseInt(dFinal[0]) + 12;
        }
        else {
            dFinal[2] = dFinal[2].replace(" AM", "");
            horaF = Integer.parseInt(dFinal[0]);
        }
        minF = Integer.parseInt(dFinal[1]);
        segF = Integer.parseInt(dFinal[2]);

        Mamadeira mamadeira = new Mamadeira(date,
                anotacoes.getText().toString(),new Time(horaI,minI,segI),
                new Time(horaF,minF,segF),quant);
        return mamadeira;
    }

    public void setDialogToEdit(final Mamadeira mamadeira, final int position,final ArrayList<Atividade> atividades){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date data = mamadeira.getDataInicial();
        Time hInicial = mamadeira.getHoraInicial();
        Time hFinal = mamadeira.getHoraFinal();

        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getAtividades().remove(mamadeira);
                atividades.remove(mamadeira);
                Singleton.getInstance().saveAtividades(getContext());
                MainActivity.updateList();
                Toast.makeText(getContext(),
                        "Atividade removida com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        layoutBotoes.addView(delete, 1);

        //Seta o comportamento do botão confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mamadeira edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(mamadeira);
                Singleton.getInstance().getAtividades().remove(mamadeira);
                atividades.remove(mamadeira);
                atividades.add(position,edit);
                if(!Singleton.getInstance().getAtividades().contains(edit))
                    Singleton.getInstance().getAtividades().add(indexInSingleton,edit);
                Singleton.getInstance().saveAtividades(v.getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade editada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        // Seta o conteúdo do dialog de acordo com os dados da atividade
        this.data.setText(dataFormat.format(data).toString());
        this.horaInicio.setText(configureTime(hInicial));
        this.horaTermino.setText(configureTime(hFinal));
        this.anotacoes.setText(mamadeira.getAnotacoes());
        this.edtQuantidade.setText(String.valueOf(mamadeira.getQuantidade()));

    }

}
