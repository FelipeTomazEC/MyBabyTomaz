package com.example.tomaz.mybabytomaz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import util.Amamentacao;
import util.Atividade;
import util.Banho;
import util.ComparadorDeAtividades;
import util.Mamadeira;
import util.Medicacao;
import util.Singleton;
import util.Soneca;
import util.TrocaDeFralda;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvlistaDeAtividades;
    private static AdapterListaDeAtividades adapterListaDeAtividades;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapterBarraDeAtividades;
    private RecyclerView barraDeAtividades;
    private ArrayList<Integer> iconesId;
    private ArrayList<String> textIcones;
    private ImageView ivLimparFiltros;
    private Spinner spinnerFiltroAtividades;
    private LinearLayout layoutFiltroData;
    private TextView tvFiltroData;
    private View navHeaderView;
    private TextView tvNomeBebe;
    private final String[] atividades = {"Todas","Banho", "Amamentação", "Mamadeira", "Soneca", "Medicação", "Troca de fralda"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // Inicializa o Navigation com as informações do bebê
        navHeaderView = navigationView.getHeaderView(0);
        tvNomeBebe = navHeaderView.findViewById(R.id.tv_nome_bebe);
        tvNomeBebe.setText("Nome do Bebê : "+ Singleton.getInstance().getBebe().getNome());

        //Inicializa e configura o conteúdo da barra de atividades"
        iconesId = new ArrayList<>();
        iconesId.add(R.drawable.icons8_amamentacao_48);
        iconesId.add(R.drawable.icons8_mamadeira_48);
        iconesId.add(R.drawable.icons8_bebe_dormindo_50);
        iconesId.add(R.drawable.icons8_comprimidos_64);
        iconesId.add(R.drawable.icons8_fralda_48);
        iconesId.add(R.drawable.icons8_banho_48);
        textIcones = new ArrayList<>();
        textIcones.add("Amamentação");
        textIcones.add("Mamadeira");
        textIcones.add("Soneca");
        textIcones.add("Medicação");
        textIcones.add("Troca de fralda");
        textIcones.add("Banho");



        // Inicializa a barra com as atividades
        barraDeAtividades = (RecyclerView) findViewById(R.id.recycler_barra_de_atividades);
        layoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        barraDeAtividades.setHasFixedSize(true);
        barraDeAtividades.setLayoutManager(layoutManager);
        adapterBarraDeAtividades = new RecyclerAdapter(iconesId, textIcones, this);
        barraDeAtividades.setAdapter(adapterBarraDeAtividades);

        // Inicializa o Imageview reponsável por limpar os filtros
        ivLimparFiltros = findViewById(R.id.iv_limpar_filtros);
        ivLimparFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerFiltroAtividades.setSelection(0);
                tvFiltroData.setText("");
            }
        });

        // Inicializa a lista com as atividades cadastradas
        Singleton.getInstance().loadAtividades(this);
        lvlistaDeAtividades = (ListView) findViewById(R.id.lv_lista_de_atividades);
        adapterListaDeAtividades = new AdapterListaDeAtividades(this.getApplicationContext(), Singleton.getInstance().getAtividades());
        spinnerFiltroAtividades = findViewById(R.id.spinner_filtro_atividade);
        layoutFiltroData = findViewById(R.id.layout_filtro_data);
        tvFiltroData = findViewById(R.id.tv_filtro_data);
        tvFiltroData.setText("");
        lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
        lvlistaDeAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Atividade atv = adapterListaDeAtividades.getAtividades().get(position);

                if (atv instanceof Amamentacao) {
                    DialogAmamentacao dialog = new DialogAmamentacao(MainActivity.this, R.layout.dialog_amamentacao);
                    dialog.setDialogToEdit((Amamentacao) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                } else if (atv instanceof Banho) {
                    DialogBanho dialog = new DialogBanho(MainActivity.this, R.layout.dialog_banho);
                    dialog.setDialogToEdit((Banho) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                } else if (atv instanceof Mamadeira) {
                    DialogMamadeira dialog = new DialogMamadeira(MainActivity.this, R.layout.dialog_mamadeira);
                    dialog.setDialogToEdit((Mamadeira) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                } else if (atv instanceof Medicacao) {
                    DialogMedicacao dialog = new DialogMedicacao(MainActivity.this);
                    dialog.setDialogToEdit((Medicacao) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                } else if (atv instanceof Soneca) {
                    DialogSoneca dialog = new DialogSoneca(MainActivity.this);
                    dialog.setDialogToEdit((Soneca) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                } else if (atv instanceof TrocaDeFralda) {
                    DialogTrocaDeFralda dialog = new DialogTrocaDeFralda(MainActivity.this);
                    dialog.setDialogToEdit((TrocaDeFralda) atv, position,adapterListaDeAtividades.getAtividades());
                    dialog.show();
                }

            }
        });

        // Configura os filtros
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, atividades);
        spinnerFiltroAtividades.setAdapter(adapterSpinner);
        layoutFiltroData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(v);
            }
        });
        tvFiltroData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filtro = spinnerFiltroAtividades.getSelectedItem().toString();
                if (filtro.equals("Amamentação")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.AMAMENTACAO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Mamadeira")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.MAMADEIRA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Soneca")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.SONECA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Medicação")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.MEDICACAO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Troca de fralda")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.TROCA_FRALDA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Banho")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.BANHO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Todas")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(-1));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                }
            }
        });
        spinnerFiltroAtividades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtro = spinnerFiltroAtividades.getSelectedItem().toString();
                if (filtro.equals("Amamentação")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.AMAMENTACAO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Mamadeira")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.MAMADEIRA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Soneca")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.SONECA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Medicação")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.MEDICACAO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Troca de fralda")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.TROCA_FRALDA));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Banho")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(Atividade.BANHO));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                } else if (filtro.equals("Todas")) {
                    adapterListaDeAtividades = new AdapterListaDeAtividades(MainActivity.this, filtrar(-1));
                    lvlistaDeAtividades.setAdapter(adapterListaDeAtividades);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void mostrarCalendario(View view) {
        final Calendar calendario = Calendar.getInstance();
        DatePickerDialog dialogCalendario = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, month);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                tvFiltroData.setText(dataFormat.format(calendario.getTime()));
            }
        }, calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));

        dialogCalendario.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.deleteBaby) {
            Singleton.getInstance().getAtividades().clear();
            Singleton.getInstance().saveAtividades(this);
            Singleton.getInstance().setBebe(null);
            Singleton.getInstance().saveBaby(this);
            Intent it = new Intent(this,CadastroActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static AdapterListaDeAtividades getAdapterListaDeAtividades(){
        return adapterListaDeAtividades;
    }

    public static void updateList() {

        Collections.sort(adapterListaDeAtividades.getAtividades(),new ComparadorDeAtividades());
        adapterListaDeAtividades.notifyDataSetChanged();
    }

    private ArrayList<Atividade> filtrar(int idAtv) {

        Date data = null;
        int dia = 0,mes = 0,ano = 0;

        ArrayList<Atividade> filtrado = new ArrayList<>();
        ArrayList<Atividade> filtrado1 = new ArrayList<>();
        if(!tvFiltroData.getText().toString().equals("")) {
            data = new Date(tvFiltroData.getText().toString());
            dia = data.getDate();
            mes = data.getMonth();
            ano = data.getYear();
        }

        if(data != null){
            for(int i = 0; i < Singleton.getInstance().getAtividades().size(); i++){
                Atividade atv = Singleton.getInstance().getAtividades().get(i);

                if((dia == atv.getDataInicial().getDate() && mes==atv.getDataInicial().getMonth() &&
                        ano == atv.getDataInicial().getYear()) || (dia ==atv.getDataFinal().getDate()&&
                        mes == atv.getDataFinal().getMonth() && ano ==atv.getDataFinal().getYear())) {

                    filtrado1.add(atv);

                    Log.e("DATAS","Data inicial :"+atv.getDataInicial().toString()
                            + "Data final :"+atv.getDataFinal()+"Data filtro :"+data.toString());
                }
            }
        }else
        {
            filtrado1 = Singleton.getInstance().getAtividades();
        }

        switch (idAtv) {
            case Atividade.AMAMENTACAO: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof Amamentacao)
                        filtrado.add(atv);
                }
                break;
            }
            case Atividade.BANHO: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof Banho)
                        filtrado.add(atv);
                }
                break;
            }
            case Atividade.MAMADEIRA: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof Mamadeira)
                        filtrado.add(atv);
                }
                break;
            }
            case Atividade.MEDICACAO: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof Medicacao)
                        filtrado.add(atv);
                }
                break;
            }
            case Atividade.SONECA: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof Soneca)
                        filtrado.add(atv);
                }
                break;
            }
            case Atividade.TROCA_FRALDA: {
                for (Atividade atv : filtrado1) {
                    if (atv instanceof TrocaDeFralda)
                        filtrado.add(atv);
                }
                break;
            }

            default:
                filtrado = filtrado1;
        }




        return filtrado;
    }
}
