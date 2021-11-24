package com.websarva.wings.android.testproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class RecipeReader extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    ArrayList<String> recipe = new ArrayList<>();
    String path;
    TextToSpeech tts;
    int count = 0;
    String judg = "あ";
    int REQUEST_CODE = 1000;
    UtteranceProgressListener upl;
    String line = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_reader);

        //TextToSpeechのインスタンス化
        tts = new TextToSpeech(RecipeReader.this, RecipeReader.this);

        //渡されたfileをfilepathに格納
        Intent intent = getIntent();
        path = intent.getStringExtra("filepath");
        File filepath = new File("/data/data/com.websarva.wings.android.testproject/files/" + path + ".txt");

        //取得したファイルの中身を一行ずつ配列に格納
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            String text;
            //recipeにレシピテキストを一行ずつ格納
            while ((text = br.readLine()) != null) {
                recipe.add(text);
                line = line+text+"\n";
            }
            br.close();
        } catch (IOException e) {
        }

        TextView tv_rr = findViewById(R.id.tv_rr);
        tv_rr.setText(line);

        //ttsのリスナー
        upl = new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                startlistening();
            }

            @Override
            public void onError(String utteranceId) {
            }
        };
        Button bt_start = findViewById(R.id.bt_rr);
        bt_start.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        finish();
        return returnVal;
    }
    @Override
    public void onInit(int num) {
        tts.setOnUtteranceProgressListener(upl);
        if (num != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.JAPAN);
        }
    }

    @Override
    public void onClick(View view) {
        //処理のメインのメソッドを呼び出す
        speech();
    }

    public void shutdown() {
        //ttsを終了させる処理を
        tts.shutdown();
    }

    public void speech() {
       if(recipe.size() >= count) {
           if (judg.contains("次")) {
               count++;
           } else {
               if (judg.contains("前")) {
                   count--;
               } else {
                   if (judg.contains("もう一度")) {
                   }
               }
           }
           //ここにメインの処理を
           tts.speak(recipe.get(count), tts.QUEUE_FLUSH, null, "read");
       }
    }


            public void startlistening(){
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString());
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "操作を選んでください\n次・戻る・もう一度");
                // インテント発行
                startActivityForResult(intent, REQUEST_CODE);
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
                judg = candidates.get(0);
                speech();
            }
        }
    }

        }











