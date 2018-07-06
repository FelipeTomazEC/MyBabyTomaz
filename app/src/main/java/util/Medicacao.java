package util;

import java.sql.Time;
import java.util.Date;

public class Medicacao extends Atividade {

    private Time horaInicio;
    private Time horaTermino;
    private String nomeMedicamento;
    private Double dosagem;
    private String tipoDeMedicamento;



    public Medicacao(Date dataInicial, Date dataFinal, String anotacoes,
                     Time horaInicio, Time horaTermino,String nomeMedicamento,
                     Double dosagem,String tipoDeMedicamento) {

        super(dataInicial, dataFinal, anotacoes);
        this.dosagem = dosagem;
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
        this.tipoDeMedicamento = tipoDeMedicamento;
        this.nomeMedicamento = nomeMedicamento;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(Time horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public Double getDosagem() {
        return dosagem;
    }

    public void setDosagem(Double dosagem) {
        this.dosagem = dosagem;
    }

    public String getTipoDeMedicamento() {
        return tipoDeMedicamento;
    }

    public void setTipoDeMedicamento(String tipoDeMedicamento) {
        this.tipoDeMedicamento = tipoDeMedicamento;
    }
}
