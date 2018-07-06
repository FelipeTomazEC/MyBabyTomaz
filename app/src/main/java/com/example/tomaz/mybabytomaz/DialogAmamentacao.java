package com.example.tomaz.mybabytomaz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import util.Amamentacao;
import util.Atividade;
import util.Singleton;

public class DialogAmamentacao extends Dialog {

    protected Button btnConfirmar;
    protected Button btnCancelar;
    protected ImageView icone;
    protected TextView titulo;
    protected TextView horaInicio;
    protected TextView data;
    protected TextView horaTermino;
    private LinearLayout layoutHoraInicial;
    private LinearLayout layoutHoraFinal;
    private LinearLayout layoutData;
    protected EditText anotacoes;
    protected LinearLayout layoutBotoes;
    private int tema;


    public DialogAmamentacao(@NonNull final Context context, int tema) {
        super(context);
        this.tema = tema;
        initComponentes();
        titulo.setText("Amamentação");
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        layoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(v);
            }
        });
        layoutHoraInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRelogio(v,horaInicio);
            }
        });

        
        layoutHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRelogio(v,horaTermino);
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amamentacao amamentacao = createAtividadeToStorage();
                Singleton.getInstance().getAtividades().add(amamentacao);
                if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(amamentacao))
                    MainActivity.getAdapterListaDeAtividades().getAtividades().add(amamentacao);
                Singleton.getInstance().saveAtividades(context);
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void setDialogToEdit(final Amamentacao amamentacao, final int position, final ArrayList<Atividade> atividades){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date data = amamentacao.getDataInicial();
        Time hInicial = amamentacao.getHoraInicial();
        Time hFinal = amamentacao.getHoraFinal();

        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getAtividades().remove(amamentacao);
                atividades.remove(amamentacao);
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
                Amamentacao edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(amamentacao);
                Singleton.getInstance().getAtividades().remove(amamentacao);
                atividades.remove(amamentacao);
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
        this.anotacoes.setText(amamentacao.getAnotacoes());

    }

    private void mostrarCalendario(View view) {
        final Calendar calendario = Calendar.getInstance();
        DatePickerDialog dialogCalendario  = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(Calendar.YEAR,year);
                calendario.set(Calendar.MONTH,month);
                calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                data.setText(dataFormat.format(calendario.getTime()));
            }
        },calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));

        dialogCalendario.show();
    }

    private void mostrarRelogio(View view, final TextView tvHora){
        TimePickerDialog.OnTimeSetListener  mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {

                Calendar calNow = Calendar.getInstance();
                calNow.set(Calendar.HOUR_OF_DAY, hora);
                calNow.set(Calendar.MINUTE, minuto);
                String hours = DateFormat.getTimeInstance().format(calNow.getTime());
                tvHora.setText(hours.toString());
            }
        };

        //Cria um calendário com a data e hora actual
        Calendar calNow = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                mTimeSetListener,
                calNow.getTime().getHours(),
                calNow.getTime().getMinutes(),
                true);

        dialog.show();
    }

    private void initComponentes(){
        // Inicializa os elementos do dialog
        Calendar initial = Calendar.getInstance();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String init = this.configureTime(initial);
        this.setContentView(tema);
        icone = this.findViewById(R.id.icone_atv);
        icone.setImageResource(R.drawable.icons8_amamentacao_48);
        titulo = this.findViewById(R.id.titulo_atv);
        layoutData = this.findViewById(R.id.layout_data);
        layoutHoraInicial = this.findViewById(R.id.hora_data_inicio_atv);
        layoutHoraFinal = this.findViewById(R.id.hora_data_termino_atv);
        layoutBotoes = this.findViewById(R.id.botoes_atv);
        data = this.findViewById(R.id.tv_data);
        horaInicio = this.findViewById(R.id.hora_inicio_atv);
        horaTermino = this.findViewById(R.id.hora_final_atv);
        btnConfirmar = this.findViewById(R.id.btn_confirmar_atv);
        btnCancelar = this.findViewById(R.id.btn_cancelar_atv);
        anotacoes = this.findViewById(R.id.edt_anotacoes_atv);
        horaInicio.setText(init);
        horaTermino.setText(init);
        data.setText(dataFormat.format(initial.getTime()));
    }

    private Amamentacao createAtividadeToStorage(){
        int horaI,minI,segI,horaF,minF,segF;
        Date date = new Date(data.getText().toString());

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

        Amamentacao amamentacao = new Amamentacao(date,
                anotacoes.getText().toString(),new Time(horaI,minI,segI),
                new Time(horaF,minF,segF));
        return amamentacao;
    }

    @NonNull
    protected String configureTime(Time time){

        int horas,minutos,segundos;
        String tag = " AM";
        horas = time.getHours();
        minutos = time.getMinutes();
        segundos = time.getSeconds();
        if(horas > 12) {
            tag = " PM";
            horas = horas - 12;
        }
        Time init = new Time(horas,minutos,segundos);

        return (init.toString() + tag);
    }

    @NonNull
    protected String configureTime(Calendar calendar){

        int horas,minutos,segundos;
        String tag = " AM";
        horas = calendar.getTime().getHours();
        minutos = calendar.getTime().getMinutes();
        segundos = calendar.getTime().getSeconds();
        if(horas > 12) {
            tag = " PM";
            horas = horas - 12;
        }
        Time init = new Time(horas,minutos,segundos);

        return (init.toString() + tag);
    }
}
