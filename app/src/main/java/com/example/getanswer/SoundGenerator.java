package com.example.getanswer;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

public class SoundGenerator {

    private final String[] typesOfAnswer = new String[] {"yes", "no", "nothing", "yes"};
    Context ctx;

    public SoundGenerator(Context c) {
        this.ctx = c;
    }

    public String generateSound(String question) throws IOException {
        AssetManager am = ctx.getAssets();
        Random random = new Random(System.currentTimeMillis() / 1000);
        int k1 = Math.abs(random.nextInt());
        String path = "answer/" + typesOfAnswer[k1 % 4] + "/";
        String[] sounds = am.list(path);
        int k2 = Math.abs(random.nextInt());
        String soundPath = path + sounds[k2 % sounds.length];
        return soundPath;
    }

    public String playSound(String soundPath) throws IOException {
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        SoundPool sp = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(abs).build();
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0) {
                    soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            }
        });
        AssetManager am = ctx.getAssets();
        int id = sp.load(am.openFd(soundPath), 1);
        String answer = soundPath.split("/")[1];
        return answer;
    }

    public void generateGreeting() throws IOException {
        AssetManager am = ctx.getAssets();
        Random random = new Random(System.currentTimeMillis() / 1000);
        String path = "hi/";
        String[] sounds = am.list(path);
        String soundPath = path + sounds[Math.abs(random.nextInt()) % sounds.length];
        playSound(soundPath);
    }

    public void generateFarewell() throws IOException {
        AssetManager am = ctx.getAssets();
        Random random = new Random(System.currentTimeMillis() / 1000);
        String path = "bye/";
        String[] sounds = am.list(path);
        String soundPath = path + sounds[Math.abs(random.nextInt()) % sounds.length];
        playSound(soundPath);
    }
}
