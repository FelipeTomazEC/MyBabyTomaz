package util;

import java.util.Comparator;

public class ComparadorDeAtividades implements Comparator<Atividade> {
    @Override
    public int compare(Atividade o1, Atividade o2) {
        if(o1.getDataInicial().after(o2.getDataInicial()))
            return -1;
        else if (o1.getDataInicial().before(o2.getDataInicial()))
            return 1;
        else
            return 0;
    }
}
