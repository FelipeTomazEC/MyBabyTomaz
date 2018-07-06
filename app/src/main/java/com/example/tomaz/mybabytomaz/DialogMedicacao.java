package com.example.tomaz.mybabytomaz;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import util.Atividade;
import util.Medicacao;
import util.Singleton;

public class DialogMedicacao extends Dialog {

    private Button btnConfirmar;
    private Button btnCancelar;
    private ImageView icone;
    private TextView titulo;
    private TextView tvHoraInicio;
    private TextView tvDataInicio;
    private TextView tvDataTermino;
    private TextView tvHoraTermino;
    private Spinner tipoMedicamento;
    private LinearLayout layoutHoraInicial;
    private LinearLayout layoutHoraFinal;
    private LinearLayout layoutDataInicial;
    private LinearLayout layoutDataFinal;
    private LinearLayout layoutBotoes;
    private EditText edtDosagem;
    private EditText edtNomeDoMedicamento;
    private EditText edtOutroTipo;
    private EditText edtAnotacoes;
    private final String[] tiposDeMedicamentos = {"Analgésico", "Anti-inflamatório", "Antialérgico",
            "Antibiótico", "Outro"};
    private ArrayAdapter<String> adapterSpinner;

    public DialogMedicacao(@NonNull Context context) {
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

        //Habilita o campo outro para digitação se escolhida a opção outro
        tipoMedicamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isOther = false;
                if (tiposDeMedicamentos[position].equals("Outro"))
                    isOther = true;
                edtOutroTipo.setEnabled(isOther);
                edtOutroTipo.setClickable(isOther);

                if(!isOther)
                    edtOutroTipo.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Medicacao medicacao = createAtividadeToStorage();

                    //Add a atividade criada no array de atividades e salva
                    Singleton.getInstance().getAtividades().add(medicacao);
                    if(!MainActivity.getAdapterListaDeAtividades().getAtividades().contains(medicacao))
                        MainActivity.getAdapterListaDeAtividades().getAtividades().add(medicacao);
                    Singleton.getInstance().saveAtividades(getContext());
                    MainActivity.updateList();
                    Toast.makeText(v.getContext(), "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show();
                    dismiss();

                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), "Preencha com um número válido !",
                            Toast.LENGTH_SHORT).show();
                    edtDosagem.requestFocus();
                }
            }
        });
    }

    private void initComponentes() {
        // Inicializa os elementos do dialog
        Calendar initial = Calendar.getInstance();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        this.setContentView(R.layout.dialog_medicacao);
        icone = this.findViewById(R.id.icone_atv);
        icone.setImageResource(R.drawable.icons8_comprimidos_64);
        titulo = this.findViewById(R.id.titulo_atv);
        titulo.setText("Medicação");
        layoutDataInicial = this.findViewById(R.id.layout_data);
        layoutDataFinal = this.findViewById(R.id.layout_data_final);
        layoutHoraInicial = this.findViewById(R.id.hora_data_inicio_atv);
        layoutHoraFinal = this.findViewById(R.id.hora_data_final_atv);
        layoutBotoes = this.findViewById(R.id.botoes_atv);
        edtNomeDoMedicamento = this.findViewById(R.id.edt_nome_medicamento);
        tipoMedicamento = this.findViewById(R.id.tipo_de_medicamento);
        edtDosagem = this.findViewById(R.id.edt_dosagem);
        edtOutroTipo = this.findViewById(R.id.edt_outro);
        tvDataInicio = this.findViewById(R.id.tv_data);
        tvDataTermino = this.findViewById(R.id.tv_data_final);
        tvHoraInicio = this.findViewById(R.id.hora_inicio_atv);
        tvHoraTermino = this.findViewById(R.id.hora_final_atv);
        btnConfirmar = this.findViewById(R.id.btn_confirmar_atv);
        btnCancelar = this.findViewById(R.id.btn_cancelar_atv);
        edtAnotacoes = this.findViewById(R.id.edt_anotacoes_atv);
        tvHoraInicio.setText(configureTime(initial));
        tvHoraTermino.setText(configureTime(initial));
        tvDataInicio.setText(dataFormat.format(initial.getTime()));
        tvDataTermino.setText(dataFormat.format(initial.getTime()));
        adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tiposDeMedicamentos);
        tipoMedicamento.setAdapter(adapterSpinner);
    }

    private Medicacao createAtividadeToStorage() throws NumberFormatException {
        int horaI, minI, segI, horaF, minF, segF;
        Date dInicio = new Date(tvDataInicio.getText().toString());
        Date dTermino = new Date(tvDataTermino.getText().toString());
        double dosagem = 0;
        String tipoM;

        dosagem = Double.parseDouble(edtDosagem.getText().toString());
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

        // Seta o tipo do medicamento
        if (tipoMedicamento.getSelectedItem().toString().equals("Outro"))
            tipoM = edtOutroTipo.getText().toString();
        else
            tipoM = tipoMedicamento.getSelectedItem().toString();

        Medicacao medicacao = new Medicacao(dInicio, dTermino, edtAnotacoes.getText().toString(),
                new Time(horaI, minI, segI), new Time(horaF, minF, segF), edtNomeDoMedicamento.getText().toString(),
                dosagem, tipoM);

        return medicacao;
    }

    public void setDialogToEdit(final Medicacao medicacao, final int position,final ArrayList<Atividade> atividades){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy");
        Date dataInicial = medicacao.getDataInicial();
        Date dataFinal = medicacao.getDataFinal();
        Time hInicial = medicacao.getHoraInicio();
        Time hFinal = medicacao.getHoraTermino();
        Double dosagem = medicacao.getDosagem();


        //Cria e adiciona o botão DELETE no dialog
        Button delete = new Button(getContext());
        delete.setText("DELETE");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getAtividades().remove(medicacao);
                atividades.remove(medicacao);
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
                Medicacao edit = createAtividadeToStorage();
                int indexInSingleton = Singleton.getInstance().getAtividades().indexOf(medicacao);
                Singleton.getInstance().getAtividades().remove(medicacao);
                atividades.remove(medicacao);
                atividades.add(position,edit);
                if(!Singleton.getInstance().getAtividades().contains(edit))
                    Singleton.getInstance().getAtividades().add(indexInSingleton,edit);
                Singleton.getInstance().saveAtividades(v.getContext());
                MainActivity.updateList();
                Toast.makeText(v.getContext(), "Atividade editada com sucesso", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        this.edtNomeDoMedicamento.setText(medicacao.getNomeMedicamento());
        this.edtDosagem.setText(String.valueOf(dosagem));
        this.tvDataInicio.setText(dataFormat.format(dataInicial));
        this.tvDataTermino.setText(dataFormat.format(dataFinal));
        this.tvHoraInicio.setText(configureTime(hInicial));
        this.tvHoraTermino.setText(configureTime(hFinal));
        this.edtAnotacoes.setText(medicacao.getAnotacoes());
        // Cuida da seleção do tipo do medicamento
        if(Arrays.asList(tiposDeMedicamentos).contains(medicacao.getTipoDeMedicamento())){
            int index = Arrays.asList(tiposDeMedicamentos).indexOf(medicacao.getTipoDeMedicamento());
            tipoMedicamento.setSelection(index);
        }else{
            tipoMedicamento.setSelection(tiposDeMedicamentos.length-1);
            edtOutroTipo.setEnabled(true);
            edtOutroTipo.setClickable(true);
            edtOutroTipo.setText(medicacao.getTipoDeMedicamento());
        }
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


