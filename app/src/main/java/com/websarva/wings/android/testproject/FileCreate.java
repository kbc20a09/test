package com.websarva.wings.android.testproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileCreate extends AppCompatActivity {

    String fileurl = "/data/data/com.websarva.wings.android.testproject/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_create);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_keep, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        switch (itemId) {

            case android.R.id.home:
                finish();
                break;

            case R.id.menu_keep://ここに保存を行う処理
                EditText et_title = findViewById(R.id.et_title);
                String title = et_title.getText().toString();
                if (title.length() == 0) {
                    Toast.makeText(FileCreate.this, R.string.ts_title, Toast.LENGTH_LONG).show();
                }
                EditText et_recipe = findViewById(R.id.et_recipe);
                String recipe = et_recipe.getText().toString();
                if (recipe.length() == 0) {
                    Toast.makeText(FileCreate.this, R.string.ts_recipe, Toast.LENGTH_LONG).show();
                }
                File newfile = new File(fileurl + title + ".txt");
                if(newfile.exists()){
                    Toast.makeText(FileCreate.this, "同名のレシピが存在します", Toast.LENGTH_LONG).show();
                }

                if (title.length() != 0 && recipe.length() != 0 && !newfile.exists()) {
                    try {
                        filekeep(title, recipe);
                    } catch (IOException e) {
                    }
                }
                break;

            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        return returnVal;
    }


    //ファイルを新規保存するメソッド
    public void filekeep(String title, String recipe) throws IOException {
        fileurl = fileurl + title + ".txt";
        File newfile = new File(fileurl);
        newfile.createNewFile();
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
            Toast.makeText(FileCreate.this, "ファイル保存に失敗しました", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(FileCreate.this, "ファイル保存に成功しました", Toast.LENGTH_LONG).show();
        MainActivity ma = new MainActivity();
        ma.reload();
    }
}


