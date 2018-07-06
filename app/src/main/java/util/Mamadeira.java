package util;

import java.sql.Time;
import java.util.Date;

public class Mamadeira extends Atividade {

    private double quantidade;
    private Time horaInicial;
    private Time horaFinal;

    public Mamadeira(Date dataInicial, String anotacoes, Time horaInicial, Time horaFinal, double quantidade) {
        super(dataInicial, anotacoes);
        this.quantidade = quantidade;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;

    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
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
