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

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import util.Atividade;
import util.Singleton;
import util.Soneca;

public class DialogSoneca extends Dialog {

    private Button btnConfirmar;
    private Button btnCancelar;
    private ImageView icone;
    private TextView titulo;
    private TextView tvHoraInicio;
    private TextView tvDataInicio;
    private TextView tvDataTermino;
    private TextView tvHoraTermino;
    private LinearLayout layoutHoraInicial;
    private LinearLayout layoutHoraFinal;
    private LinearLayout layoutDataInicial;
    private LinearLayout layoutDataFinal;
    private EditText edtAnotacoes;
    private LinearLayout layoutBotoes;


    public DialogSoneca(@NonNull Context context) {
        super(context);

        initComponentes();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        layoutDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(v, tvDataInicio);
            }
        });

        layoutDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(v, tvDataTermino);
            }
        });

        layoutHoraInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRelogio(v, tvHoraInicio);
            }
        });

        layoutHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRelogio(v, tvHoraTermino);
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Soneca soneca = createAtividadeToStorage();

                    //Add a atividade criada no array de atividades e salva
                    Singleton.getInstance().getAtividades().add(soneca);
                    if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(soneca))
                        MainActivity.getAdapterListaDeAtividades().getAtividades().add(soneca);
                    Singleton.getInstance().saveAtividades(getContext());
                    MainActivity.updateList();
                    Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                    dismiss();
            }
        });
    }

    public void setDialogToEdit(final Soneca soneca, final int position,final ArrayList<Atividade> atividades){

        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date dataInicial = soneca.getDataInicial();
        Date dataFinal = soneca.getDataFinal();
        Time hInicial = soneca.getHoraInicio();
        Time hFinal = soneca.getHoraTermino();

        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getAtividades().remove(soneca);
                atividades.remove(soneca);
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
                Soneca edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(soneca);
                Singleton.getInstance().getAtividades().remove(soneca);
                atividades.remove(soneca);
                atividades.add(position,edit);
                if(!Singleton.getInstance().getAtividades().contains(edit))
                    Singleton.getInstance().getAtividades().add(indexInSingleton,edit);
                Singleton.getInstance().saveAtividades(v.getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade editada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        this.tvDataInicio.setText(dataFormat.format(dataInicial));
        this.tvDataTermino.setText(dataFormat.format(dataFinal));
        this.tvHoraInicio.setText(configureTime(hInicial));
        this.tvHoraTermino.setText(configureTime(hFinal));
        this.edtAnotacoes.setText(soneca.getAnotacoes());
    }

    private void initComponentes() {
        // Inicializa os elementos do dialog
        Calendar initial = Calendar.getInstance();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        String time = configureTime(initial);
        this.setContentView(R.layout.dialog_soneca);
        icone = this.findViewById(R.id.icone_atv);
        icone.setImageResource(R.drawable.icons8_bebe_dormindo_50);
        titulo = this.findViewById(R.id.titulo_atv);
        titulo.setText("Soneca");
        layoutDataInicial = this.findViewById(R.id.layout_data);
        layoutDataFinal = this.findViewById(R.id.layout_data_final);
        layoutHoraInicial = this.findViewById(R.id.hora_data_inicio_atv);
        layoutHoraFinal = this.findViewById(R.id.hora_data_final_atv);
        layoutBotoes = this.findViewById(R.id.botoes_atv);
        tvDataInicio = this.findViewById(R.id.tv_data);
        tvDataTermino = this.findViewById(R.id.tv_data_final);
        tvHoraInicio = this.findViewById(R.id.hora_inicio_atv);
        tvHoraTermino = this.findViewById(R.id.hora_final_atv);
        btnConfirmar = this.findViewById(R.id.btn_confirmar_atv);
        btnCancelar = this.findViewById(R.id.btn_cancelar_atv);
        edtAnotacoes = this.findViewById(R.id.edt_anotacoes_atv);
        tvHoraInicio.setText(time);
        tvHoraTermino.setText(time);
        tvDataInicio.setText(dataFormat.format(initial.getTime()));
        tvDataTermino.setText(dataFormat.format(initial.getTime()));
    }

    private Soneca createAtividadeToStorage(){
        int horaI, minI, segI, horaF, minF, segF;
        Date dInicio = new Date(tvDataInicio.getText().toString());
        Date dTermino = new Date(tvDataTermino.getText().toString());

        String[] hInit = tvHoraInicio.getText().toString().split(":");
        if (hInit[2].contains(" PM")) {
            hInit[2] = hInit[2].replace(" PM", "");
            horaI = Integer.parseInt(hInit[0]) + 12;
        } else {
            hInit[2] = hInit[2].replace(" AM", "");
            horaI = Integer.parseInt(hInit[0]);
        }
        minI = Integer.parseInt(hInit[1]);
        segI = Integer.parseInt(hInit[2]);

        String[] hFinal = tvHoraTermino.getText().toString().split(":");
        if (hFinal[2].contains(" PM")) {
            hFinal[2] = hFinal[2].replace(" PM", "");
            horaF = Integer.parseInt(hFinal[0]) + 12;
        } else {
            hFinal[2] = hFinal[2].replace(" AM", "");
            horaF = Integer.parseInt(hFinal[0]);
        }
        minF = Integer.parseInt(hFinal[1]);
        segF = Integer.parseInt(hFinal[2]);

        Soneca soneca = new Soneca(dInicio, dTermino, edtAnotacoes.getText().toString(),
                new Time(horaI, minI, segI), new Time(horaF, minF, segF));

        return soneca;
    }

    private void mostrarCalendario(View view, final TextView data) {
        final Calendar calendario = Calendar.getInstance();
        DatePickerDialog dialogCalendario = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, month);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                data.setText(dataFormat.format(calendario.getTime()));
            }
        }, calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));

        dialogCalendario.show();
    }

    private void mostrarRelogio(View view, final TextView tvHora) {
        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
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

    @NonNull
    private String configureTime(Time time){

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
    private String configureTime(Calendar calendar){

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
