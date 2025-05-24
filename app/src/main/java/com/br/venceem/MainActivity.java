package com.br.venceem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BancoDeDados bancoDados;
    private RecyclerView listaRecycler;
    private AdaptadorProduto adaptador;
    private EditText textoEntradaNome, textoEntradaValidade;
    private ArrayList<Produto> listaProdutos;
    private SharedPreferences preferencias;
    private static final String PREFS_NAME = "app_preferences";
    private static final String CHAVE_TEMA = "app_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencias = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int temaSalvo = preferencias.getInt(CHAVE_TEMA, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(temaSalvo);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bancoDados = new BancoDeDados(this);

        textoEntradaNome = findViewById(R.id.editTextNome);
        textoEntradaValidade = findViewById(R.id.editTextValidade);
        listaRecycler = findViewById(R.id.recyclerViewProdutos);
        listaRecycler.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.buttonAdd).setOnClickListener(v -> adicionarProduto());

        textoEntradaValidade.setFocusable(false);
        textoEntradaValidade.setClickable(true);
        textoEntradaValidade.setOnClickListener(v -> mostrarDialogoSeletorData());

        carregarProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.menu_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_theme) {
            mostrarDialogoSelecaoTema();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoSelecaoTema() {
        final String[] temas = {"Claro", "Escuro"};
        int temaAtual = preferencias.getInt(CHAVE_TEMA, AppCompatDelegate.MODE_NIGHT_NO);
        int itemChecado = (temaAtual == AppCompatDelegate.MODE_NIGHT_YES) ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Selecione o Tema")
                .setSingleChoiceItems(temas, itemChecado, (dialog, qual) -> {
                    SharedPreferences.Editor editor = preferencias.edit();
                    if (qual == 0) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putInt(CHAVE_TEMA, AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putInt(CHAVE_TEMA, AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    editor.apply();
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoSeletorData() {
        final Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog seletorData = new DatePickerDialog(this, (DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) -> {
            String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", diaSelecionado, mesSelecionado + 1, anoSelecionado);
            textoEntradaValidade.setText(dataFormatada);
        }, ano, mes, dia);

        seletorData.show();
    }

    private void adicionarProduto() {
        String nome = textoEntradaNome.getText().toString().trim();
        String validadeStr = textoEntradaValidade.getText().toString().trim();

        if (nome.isEmpty() || validadeStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha nome e validade.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dataValida(validadeStr)) {
            Toast.makeText(this, "Data inválida! Use formato dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        String validadeBD = converterDataParaBD(validadeStr);

        bancoDados.adicionarProduto(nome, validadeBD);
        Toast.makeText(this, "Produto adicionado!", Toast.LENGTH_SHORT).show();

        textoEntradaNome.setText("");
        textoEntradaValidade.setText("");

        carregarProdutos();
    }

    private void carregarProdutos() {
        listaProdutos = bancoDados.obterTodosProdutos();
        adaptador = new AdaptadorProduto(listaProdutos);
        listaRecycler.setAdapter(adaptador);
    }

    private boolean dataValida(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String converterDataParaBD(String data) {
        try {
            SimpleDateFormat sdfBrasil = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d = sdfBrasil.parse(data);
            SimpleDateFormat sdfBD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdfBD.format(d);
        } catch (ParseException e) {
            return null;
        }
    }

    private String converterDataParaExibicao(String data) {
        try {
            SimpleDateFormat sdfBD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = sdfBD.parse(data);
            SimpleDateFormat sdfBrasil = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfBrasil.format(d);
        } catch (ParseException e) {
            return data;
        }
    }

    private long obterDiasRestantes(String validadeBD) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dataExpiracao = sdf.parse(validadeBD);
            Date hoje = new Date();

            long diff = dataExpiracao.getTime() - hoje.getTime();
            return diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            return 0;
        }
    }

    private void mostrarDialogoOpcoes(Produto produto) {
        String[] opcoes = {"Editar", "Excluir"};
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(produto.obterNome())
                .setItems(opcoes, (dialog, qual) -> {
                    if (qual == 0) {
                        mostrarDialogoEdicao(produto);
                    } else if (qual == 1) {
                        bancoDados.excluirProduto(produto.obterId());
                        Toast.makeText(MainActivity.this, "Produto excluído", Toast.LENGTH_SHORT).show();
                        carregarProdutos();
                    }
                })
                .show();
    }

    private void mostrarDialogoEdicao(Produto produto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Produto");

        View viewInflado = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);

        final EditText textoNomeDialog = viewInflado.findViewById(R.id.editTextNomeDialog);
        final EditText textoValidadeDialog = viewInflado.findViewById(R.id.editTextValidadeDialog);

        textoNomeDialog.setText(produto.obterNome());
        textoValidadeDialog.setText(converterDataParaExibicao(produto.obterValidade()));

        // Configura DatePicker no diálogo
        textoValidadeDialog.setFocusable(false);
        textoValidadeDialog.setClickable(true);
        textoValidadeDialog.setOnClickListener(v -> {
            final Calendar calendario = Calendar.getInstance();
            int ano = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog seletorData = new DatePickerDialog(MainActivity.this, (view, anoSelecionado, mesSelecionado, diaSelecionado) -> {
                String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", diaSelecionado, mesSelecionado + 1, anoSelecionado);
                textoValidadeDialog.setText(dataFormatada);
            }, ano, mes, dia);

            seletorData.show();
        });

        builder.setView(viewInflado);

        builder.setPositiveButton("Salvar", (dialog, qual) -> {
            String novoNome = textoNomeDialog.getText().toString().trim();
            String novaValidade = textoValidadeDialog.getText().toString().trim();

            if (novoNome.isEmpty() || novaValidade.isEmpty()) {
                Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!dataValida(novaValidade)) {
                Toast.makeText(MainActivity.this, "Data inválida! Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            String validadeBD = converterDataParaBD(novaValidade);
            bancoDados.atualizarProduto(produto.obterId(), novoNome, validadeBD);
            Toast.makeText(MainActivity.this, "Produto atualizado!", Toast.LENGTH_SHORT).show();
            carregarProdutos();
        });

        builder.setNegativeButton("Cancelar", (dialog, qual) -> dialog.cancel());

        builder.show();
    }

    private class AdaptadorProduto extends RecyclerView.Adapter<AdaptadorProduto.VisualizadorProduto> {

        private final ArrayList<Produto> produtos;

        AdaptadorProduto(ArrayList<Produto> lista) {
            this.produtos = lista;
        }

        @Override
        public VisualizadorProduto onCreateViewHolder(ViewGroup pai, int tipo) {
            View view = LayoutInflater.from(pai.getContext()).inflate(R.layout.product_item, pai, false);
            return new VisualizadorProduto(view);
        }

        @Override
        public void onBindViewHolder(VisualizadorProduto holder, int posicao) {
            Produto produto = produtos.get(posicao);
            holder.vincular(produto);
        }

        @Override
        public int getItemCount() {
            return produtos.size();
        }

        class VisualizadorProduto extends RecyclerView.ViewHolder {
            TextView textoNome, textoValidade, textoDiasRestantes;

            private final Handler handlerCliqueLongo = new Handler(Looper.getMainLooper());
            private boolean cliqueLongoDisparado = false;

            VisualizadorProduto(View itemView) {
                super(itemView);
                textoNome = itemView.findViewById(R.id.textNome);
                textoValidade = itemView.findViewById(R.id.textValidade);
                textoDiasRestantes = itemView.findViewById(R.id.textDiasRestantes);

                itemView.setOnTouchListener((v, evento) -> {
                    switch (evento.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            cliqueLongoDisparado = false;
                            handlerCliqueLongo.postDelayed(() -> {
                                cliqueLongoDisparado = true;
                                int posicao = getAdapterPosition();
                                if (posicao != RecyclerView.NO_POSITION) {
                                    Produto produto = produtos.get(posicao);
                                    mostrarDialogoOpcoes(produto);
                                }
                            }, 500); // 3 segundos
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            handlerCliqueLongo.removeCallbacksAndMessages(null);
                            break;
                    }
                    return true;
                });
            }

            void vincular(Produto produto) {
                textoNome.setText(produto.obterNome());
                textoValidade.setText("Validade: " + converterDataParaExibicao(produto.obterValidade()));

                long diasRestantes = obterDiasRestantes(produto.obterValidade());
                if (diasRestantes > 0) {
                    textoDiasRestantes.setText(diasRestantes + " dias restantes");
                } else if (diasRestantes == 0) {
                    textoDiasRestantes.setText("Vence hoje!");
                } else {
                    textoDiasRestantes.setText("Vencido");
                }
            }
        }
    }
}
