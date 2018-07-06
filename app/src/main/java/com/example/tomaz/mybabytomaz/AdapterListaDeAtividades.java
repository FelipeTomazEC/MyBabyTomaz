package com.example.tomaz.mybabytomaz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import util.Amamentacao;
import util.Atividade;
import util.Banho;
import util.ComparadorDeAtividades;
import util.Mamadeira;
import util.Medicacao;
import util.Singleton;
import util.Soneca;
import util.TrocaDeFralda;

class AdapterListaDeAtividades extends BaseAdapter {

    private LayoutInflater thisInflater;
    private Context ctx;
    private ArrayList<Atividade>atividades;

    public ArrayList<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(ArrayList<Atividade> atividades) {
        this.atividades = atividades;
    }

    public AdapterListaDeAtividades(Context ctx, ArrayList<Atividade> atividades){
        this.ctx = ctx;
        this.thisInflater = LayoutInflater.from(ctx);
        this.atividades = atividades;
    }

    @Override
    public int getCount() {
        return atividades.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = thisInflater.inflate(R.layout.item_lista_atividades,parent,false);
        ImageView icone = (ImageView) convertView.findViewById(R.id.icone_item_list);
        TextView tvRotulo = (TextView) convertView.findViewById(R.id.rotulo_item_list);
        TextView tvData = (TextView) convertView.findViewById(R.id.data_item_list);
        TextView tvAnotacoes = (TextView) convertView.findViewById(R.id.tv_item_anotacoes);

        Atividade atv = atividades.get(position);

        if (atv instanceof Amamentacao){
            icone.setImageResource(R.drawable.icons8_amamentacao_48);
            tvRotulo.setText("Amamentação");
        } else if(atv instanceof Banho){
            icone.setImageResource(R.drawable.icons8_banho_48);
            tvRotulo.setText("Banho");
        }else if(atv instanceof Mamadeira){
            icone.setImageResource(R.drawable.icons8_mamadeira_48);
            tvRotulo.setText("Mamadeira");
        }else if(atv instanceof Medicacao){
            icone.setImageResource(R.drawable.icons8_comprimidos_64);
            tvRotulo.setText("Medicação");
        }else if(atv instanceof Soneca){
            icone.setImageResource(R.drawable.icons8_bebe_dormindo_50);
            tvRotulo.setText("Soneca");
        }else if(atv instanceof TrocaDeFralda){
            icone.setImageResource(R.drawable.icons8_fralda_48);
            tvRotulo.setText("Troca de Fralda");
        }

        SimpleDateFormat dataformat = new SimpleDateFormat("dd/MM/yyyy");
        tvData.setText(dataformat.format(atv.getDataInicial()).toString() + " - " +
                        dataformat.format(atv.getDataFinal()).toString());
        tvAnotacoes.setText(atv.getAnotacoes());

        return convertView;
    }

}
