package com.websarva.wings.android.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//ファイル一覧を格納する入れ物
private File[] filename;
File fileurl= new File("/data/data/com.websarva.wings.android.testproject/files");
String filepath;
List<String> filelist;
ListView lv_file;

MakeDialog dialog = new MakeDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ファイルパスのセット

        lv_file = findViewById(R.id.lv_List);
        filelist = new ArrayList<>();
        filename = fileurl.listFiles();

        //初回起動時filesがなかった場合新規に作成する
        if(!fileurl.exists()){
               fileurl.mkdir();
        }


        //ファイルの一覧名をString型に変換する処理
        for(int i = 0;i < filename.length;i++) {
            String name = filename[i].getName();
            name = name.substring(0,name.lastIndexOf('.'));
            filelist.add(name);
        }

        //アダプターのセット
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1, filelist);
        lv_file.setAdapter(adapter);

        lv_file.setOnItemClickListener(new ListItemClickListener());
    }

    //FileCreate終了時にリストを再読み込みする
    public void reload(){
        //ファイルの一覧名をString型に変換する処理
        filename = fileurl.listFiles();
        filelist = new ArrayList<>();
        for(int i = 0;i < filename.length;i++) {
            String name = filename[i].getName();
            name = name.substring(0,name.lastIndexOf('.'));
            filelist.add(name);
        }

        //アダプターのセット
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1, filelist);
        lv_file.setAdapter(adapter);

        lv_file.setOnItemClickListener(new ListItemClickListener());
    }


    //リストタップしたリスナー
     private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            filepath = (String)parent.getItemAtPosition(position);
            Bundle args = new Bundle();
            args.putString("filepath",filepath);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(),"MakeDialog");
        }
    }

    //インフレータの設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option,menu);
        return true;
    }


    //＋ボタン押した処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();

        if(itemId == R.id.menu_plus){

            Intent intent_fc = new Intent(MainActivity.this,FileCreate.class);
            startActivity(intent_fc);
        } else {
            returnVal = super.onOptionsItemSelected(item);
       }
        return returnVal;
    }

}