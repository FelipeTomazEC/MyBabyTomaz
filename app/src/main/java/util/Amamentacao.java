package util;

import java.sql.Time;
import java.util.Date;


public class Amamentacao extends Atividade {

    private Time horaInicial;
    private Time horaFinal;

    public Amamentacao(Date dataInicial,String anotacoes,Time horaInicial,Time horaFinal) {
        super(dataInicial,anotacoes);
        this.horaFinal = horaFinal;
        this.horaInicial = horaInicial;
    }

    public Time getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Time horaInicial) {
        this.horaInicial = horaInicial;
    }

    public Time getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Time horaFinal) {
        this.horaFinal = horaFinal;
    }
}
