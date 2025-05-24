package com.br.venceem; // substitua pelo seu pacote real

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// Classe para manipular o banco de dados SQLite
public class BancoDeDados extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "produtos.db";
    private static final int VERSAO_BANCO = 1;
    private static final String TABELA_PRODUTOS = "produtos";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_VALIDADE = "validade";

    public BancoDeDados(Context contexto) {
        super(contexto, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCriaTabela = "CREATE TABLE " + TABELA_PRODUTOS + " (" +
                COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUNA_NOME + " TEXT NOT NULL, " +
                COLUNA_VALIDADE + " TEXT NOT NULL)";
        db.execSQL(sqlCriaTabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PRODUTOS);
        onCreate(db);
    }

    // Insere um produto na tabela
    public void adicionarProduto(String nome, String validade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, nome);
        valores.put(COLUNA_VALIDADE, validade);
        db.insert(TABELA_PRODUTOS, null, valores);
        db.close();
    }

    // Recupera todos os produtos do banco
    public ArrayList<Produto> obterTodosProdutos() {
        ArrayList<Produto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_PRODUTOS, null);
        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();
                produto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_ID)));
                produto.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_NOME)));
                produto.setValidade(cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_VALIDADE)));
                lista.add(produto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Atualiza dados de um produto existente
    public void atualizarProduto(int id, String nome, String validade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_NOME, nome);
        valores.put(COLUNA_VALIDADE, validade);
        db.update(TABELA_PRODUTOS, valores, COLUNA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Exclui um produto pelo ID
    public void excluirProduto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_PRODUTOS, COLUNA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
