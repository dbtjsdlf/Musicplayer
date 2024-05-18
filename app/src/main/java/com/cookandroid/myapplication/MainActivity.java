package com.cookandroid.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listViewMP3;
    Button btnPlay, btnStop;
    TextView tvMP3;
    ProgressBar pbMP3;

    ArrayList<String> mp3List;
    String selectedMP3;

    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    MediaPlayer mPlayer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 PLAYER");

        // SD카드를 사용하겠다는 permission 설정
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mp3List = new ArrayList<>();

        // mp3 파일 불러오기
        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        assert listFiles != null;
        for(File file : listFiles){
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if(extName.equals("mp3"))
                mp3List.add((fileName));
        }

        // 리스트뷰에 mp3 음악 추가하기
        listViewMP3 = findViewById(R.id.listViewMP3);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, mp3List);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);

        listViewMP3.setOnItemClickListener((adapterView, view, i, l) -> selectedMP3 = mp3List.get(i));
        selectedMP3 = mp3List.get(0);


        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        tvMP3 = findViewById(R.id.tvMP3);
        pbMP3 = findViewById(R.id.pbMP3);

        // 재생버튼
        btnPlay.setOnClickListener(view -> {
            try{
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(mp3Path + selectedMP3);
                mPlayer.prepare();
                mPlayer.start();
                btnPlay.setClickable(false);
                btnStop.setClickable(true);
                tvMP3.setText("실행중인 음악:" + selectedMP3);
                pbMP3.setVisibility(View.VISIBLE);
            }catch (IOException ignored){}
        });

        // 정지 버튼
        btnStop.setOnClickListener(view -> {
            mPlayer.start();
            mPlayer.reset();
            btnPlay.setClickable(true);
            btnStop.setClickable(false);
            tvMP3.setText("실행중인 음악: ");
            pbMP3.setVisibility(View.INVISIBLE);

        });

        btnStop.setClickable(false);
    }
}