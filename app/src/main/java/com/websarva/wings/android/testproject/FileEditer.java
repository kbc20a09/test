package com.websarva.wings.android.testproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileEditer extends AppCompatActivity {
    String fileurl = "/data/data/com.websarva.wings.android.testproject/files/";
    String memo = "";
    String path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editer);

        Intent intent = getIntent();
        path = intent.getStringExtra("filepath");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText et_title = findViewById(R.id.et_title);

        et_title.setText(path);
        EditText et_recipe = findViewById(R.id.et_recipe);
        try {
            FileReader fr = new FileReader(fileurl+path+".txt");
            BufferedReader br = new BufferedReader(fr);
            String text;
            while ((text = br.readLine()) != null) {
               memo = memo + text + "\n";
            }
            br.close();
        }catch (IOException e){

        }
        et_recipe.setText(memo);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        File file = new File(fileurl+path+".txt");
        switch (itemId) {

            case android.R.id.home:
                finish();
                break;

            case R.id.menu_keep://ここに保存を行う処理
                EditText et_title = findViewById(R.id.et_title);
                String title = et_title.getText().toString();
                if (title.length() == 0) {
                    Toast.makeText(FileEditer.this, R.string.ts_title, Toast.LENGTH_LONG).show();
                }
                EditText et_recipe = findViewById(R.id.et_recipe);
                String recipe = et_recipe.getText().toString();
                if (recipe.length() == 0) {
                    Toast.makeText(FileEditer.this, R.string.ts_recipe, Toast.LENGTH_LONG).show();
                }

                if (title.length() != 0 && recipe.length() != 0) {
                   //上書き処理呼び出し
                    try {
                        Overwrite(file, title, recipe);
                    } catch (IOException e){

                    }
                }
                break;

            case R.id.menu_delete:
                file.delete();
                MainActivity ma = new MainActivity();
                ma.reload();
                break;

            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        return returnVal;
    }


    //ファイルを上書き保存するメソッド
    public void Overwrite(File file, String title,String recipe)throws IOException{
        file.delete();
        fileurl = fileurl + title + ".txt";
        File newfile = new File(fileurl);
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter fw = new FileWriter(fileurl, true);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            //ファイルに追記する
            pw.println(recipe);
            //ファイルを閉じる
            pw.close();
        } catch (IOException e) {
            Toast.makeText(FileEditer.this, "ファイル保存に失敗しました", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(FileEditer.this, "ファイル保存に成功しました", Toast.LENGTH_LONG).show();
        MainActivity ma = new MainActivity();
        ma.reload();
    }

}