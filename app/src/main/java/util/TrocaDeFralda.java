package util;

import java.sql.Time;
import java.util.Date;

public class TrocaDeFralda extends Atividade {

    private boolean urina;
    private boolean fezes;
    private Time horario;

    public TrocaDeFralda(Date dataInicial, Time horario, boolean urina, boolean fezes, String anotacoes) {
        super(dataInicial, anotacoes);
        this.fezes = fezes;
        this.urina = urina;
        this.horario = horario;
    }

    public boolean isUrina() {
        return urina;
    }

    public void setUrina(boolean urina) {
        this.urina = urina;
    }

    public boolean isFezes() {
        return fezes;
    }

    public void setFezes(boolean fezes) {
        this.fezes = fezes;
    }

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }
}
