package com.example.getanswer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.JetPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    TextView question, main_text;
    Button b;
    ImageView pda;
    int pdaId;
    SoundGenerator generator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        question = findViewById(R.id.question);
        main_text = findViewById(R.id.main_text);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Graffiti1CTT.ttf");
        main_text.setTypeface(tf);
        pda = findViewById(R.id.pda);
        generator = new SoundGenerator(this);
        //this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sound = generator.generateSound(String.valueOf(question.getText()));
                    String pdaType = generator.playSound(sound);
                    updatePda(pdaType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    generator.generateGreeting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        MyThread thread = new MyThread(task);
        thread.start();
        super.onResume();
    }


    public class MyThread extends Thread {
        Runnable target;
        public MyThread(Runnable task) {
            this.target = task;
        }

        @Override
        public void run() {
            if(target != null) {
                target.run();
            }
        }
    }

    public void updatePda(String type) {
        if(type.equals("yes")) {
            pdaId = R.drawable.yes;
        } else if (type.equals("no")) {
            pdaId = R.drawable.no;
        } else {
            pdaId = R.drawable.nothing;
        }
        pda.setImageResource(pdaId);
    }


}