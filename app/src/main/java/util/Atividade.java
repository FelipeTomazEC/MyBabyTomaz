package util;

import java.io.Serializable;
import java.util.Date;

public class Atividade implements Serializable {
    public static final int AMAMENTACAO = 0;
    public static final int MAMADEIRA = 1;
    public static final int PESO_ALTURA = 2;
    public static final int SONECA = 2;
    public static final int MEDICACAO = 3;
    public static final int TROCA_FRALDA = 4;
    public static final int BANHO = 5;
    private Date dataInicial;
    private Date dataFinal;
    private String anotacoes;

    public Atividade(Date dataInicial, Date dataFinal, String anotacoes) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.anotacoes = anotacoes;
    }

    public Atividade(Date dataInicial, String anotacoes) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataInicial;
        this.anotacoes = anotacoes;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}
