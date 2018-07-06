package util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Banho extends Atividade {

    private Time horaInicio;
    private Time horaTermino;
    private String [] produtosUtilizados;

    public Banho(Date dataInicial, String anotacoes,Time horaInicio,Time horaTermino, String [] produtosUtilizados) {
        super(dataInicial, anotacoes);
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
        this.produtosUtilizados = produtosUtilizados;
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

    public String[] getProdutosUtilizados() {
        return produtosUtilizados;
    }

    public void setProdutosUtilizados(String[] produtosUtilizados) {
        this.produtosUtilizados = produtosUtilizados;
    }
}
