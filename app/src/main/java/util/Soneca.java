package util;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Soneca extends Atividade{

    private Time horaInicio;
    private Time horaTermino;

    public Soneca(Date dataInicial, Date dataFinal, String anotacoes,Time horaInicio,Time horaTermino) {
        super(dataInicial, dataFinal, anotacoes);
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
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
}
