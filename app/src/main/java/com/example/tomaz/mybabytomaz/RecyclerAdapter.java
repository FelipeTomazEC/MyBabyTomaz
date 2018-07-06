package com.example.tomaz.mybabytomaz;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import util.Atividade;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<Integer> iconesId;
    private ArrayList<String> textoIcones;
    private Context context;

    public RecyclerAdapter(ArrayList<Integer> iconesId, ArrayList<String> textoIcones, Context context) {
        this.iconesId = iconesId;
        this.textoIcones = textoIcones;
        this.context = context;
      }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.icone.setImageResource(iconesId.get(position));
        holder.textoIcone.setText(textoIcones.get(position));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                switch (position) {
                    case Atividade.AMAMENTACAO: {
                        new DialogAmamentacao(context,R.layout.dialog_amamentacao).show();
                        break;
                    }
                    case Atividade.MAMADEIRA: {
                        new DialogMamadeira(context,R.layout.dialog_mamadeira).show();
                        break;
                    }
                    case Atividade.TROCA_FRALDA: {
                        new DialogTrocaDeFralda(context).show();
                        break;
                    }
                    case Atividade.MEDICACAO: {
                        new DialogMedicacao(context).show();
                        break;
                    }
                    case Atividade.BANHO: {
                        new DialogBanho(context,R.layout.dialog_banho).show();
                        break;
                    }
                    case Atividade.SONECA : {
                        new DialogSoneca(context).show();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return textoIcones.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public ImageButton icone;
    public TextView textoIcone;
    private ItemClickListener itemClickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        icone = (ImageButton) itemView.findViewById(R.id.icone_atividade);
        textoIcone = (TextView) itemView.findViewById(R.id.text_icone);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }
}


