package util;

import android.content.Context;
import android.support.v7.util.AdapterListUpdateCallback;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class Singleton {

    private static Singleton singleton = null;

    // Elementos singleton
    private Bebe bebe;
    private ArrayList<Atividade> atividades;

    private Singleton (){
        atividades = new ArrayList<>();
        bebe = null;
    }

    public static Singleton getInstance(){
        if(singleton == null){
            singleton = new Singleton();
            return singleton;
        }
        else
            return singleton;
    }

    public Bebe getBebe() {
        return bebe;
    }

    public void setBebe(Bebe bebe) {
        this.bebe = bebe;
    }

    public ArrayList<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(ArrayList<Atividade> atividades) {
        this.atividades = atividades;
    }

    public void saveAtividades(Context context) {
            FileOutputStream fos;
            try {
                fos = context.openFileOutput("atividades.txt",
                        Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(atividades);
                oos.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public void saveBaby(Context context){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput("bebe.txt",
                    Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(bebe);
            Log.e("CSI456", "Salvou o bebe");
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBaby(Context context){
        FileInputStream fis;
        try {
            fis = context.openFileInput("bebe.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            bebe = (Bebe) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadAtividades(Context context) {
        FileInputStream fis;
        try {
            fis = context.openFileInput("atividades.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            atividades = (ArrayList<Atividade>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
