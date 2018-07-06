package com.example.tomaz.mybabytomaz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import util.Amamentacao;
import util.Atividade;
import util.Singleton;
import util.TrocaDeFralda;

public class DialogTrocaDeFralda extends Dialog {

    private Button btnConfirmar;
    private Button btnCancelar;
    private ImageView icone;
    private TextView titulo;
    private TextView horaInicio;
    private TextView data;
    private LinearLayout layoutData;
    private LinearLayout layoutHoraInicial;
    private EditText anotacoes;
    private CheckBox ckbUrina;
    private CheckBox ckbFezes;
    private LinearLayout layoutBotoes;


    public DialogTrocaDeFralda(@NonNull final Context context) {
        super(context);
        initComponentes();
        titulo.setText("Troca de fralda");
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

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TrocaDeFralda trocaDeFralda = createAtividadeToStorage();

                //Adiciona a atividade no array de atividade e salva
                Singleton.getInstance().getAtividades().add(trocaDeFralda);
                if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(trocaDeFralda))
                    MainActivity.getAdapterListaDeAtividades().getAtividades().add(trocaDeFralda);
                Singleton.getInstance().saveAtividades(getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }



    private void initComponentes(){
        // Inicializa os elementos do dialog
        Calendar initial = Calendar.getInstance();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);


        String init = configureTime(initial);
        this.setContentView(R.layout.dialog_troca_de_fralda);
        icone = this.findViewById(R.id.icone_atv);
        icone.setImageResource(R.drawable.icons8_fralda_48);
        titulo = this.findViewById(R.id.titulo_atv);
        layoutData = this.findViewById(R.id.layout_data);
        layoutHoraInicial = this.findViewById(R.id.hora_data_inicio_atv);
        layoutBotoes = this.findViewById(R.id.botoes_atv);
        data = this.findViewById(R.id.tv_data);
        horaInicio = this.findViewById(R.id.hora_inicio_atv);
        btnConfirmar = this.findViewById(R.id.btn_confirmar_atv);
        btnCancelar = this.findViewById(R.id.btn_cancelar_atv);
        anotacoes = this.findViewById(R.id.edt_anotacoes_atv);
        ckbFezes = this.findViewById(R.id.chk_fezes);
        ckbUrina = this.findViewById(R.id.chk_urina);
        horaInicio.setText(init);
        data.setText(dataFormat.format(initial.getTime()));
    }

    public void setDialogToEdit(final TrocaDeFralda trocaDeFralda, final int position, final ArrayList<Atividade> atividades){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date dataInicial = trocaDeFralda.getDataInicial();
        Time hInicial = trocaDeFralda.getHorario();

        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getAtividades().remove(trocaDeFralda);
                atividades.remove(trocaDeFralda);
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
                TrocaDeFralda edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(trocaDeFralda);
                Singleton.getInstance().getAtividades().remove(trocaDeFralda);
                atividades.remove(trocaDeFralda);
                atividades.add(position,edit);
                if(!Singleton.getInstance().getAtividades().contains(edit))
                    Singleton.getInstance().getAtividades().add(indexInSingleton,edit);
                Singleton.getInstance().saveAtividades(v.getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade editada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        this.data.setText(dataFormat.format(dataInicial));
        this.horaInicio.setText(configureTime(hInicial));
        this.ckbUrina.setChecked(trocaDeFralda.isUrina());
        this.ckbFezes.setChecked(trocaDeFralda.isFezes());
        this.anotacoes.setText(trocaDeFralda.getAnotacoes());
    }

    private TrocaDeFralda createAtividadeToStorage(){
        int horaI,minI,segI;
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


        TrocaDeFralda trocaDeFralda = new TrocaDeFralda(date,new Time(horaI,minI,segI),
                ckbUrina.isChecked(),ckbFezes.isChecked(),anotacoes.getText().toString());

        return trocaDeFralda;
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
