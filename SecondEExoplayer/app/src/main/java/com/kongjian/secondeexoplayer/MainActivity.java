package com.kongjian.secondeexoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlayerImplInternal;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.ProgressiveMediaExtractor;
import com.google.android.exoplayer2.source.ProgressiveMediaPeriod;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Assertions;

public class MainActivity extends AppCompatActivity {

    private static final String strVideo = Environment.getExternalStorageDirectory().getPath() + "/video2.mp4";
    private static final String aacPath = Environment.getExternalStorageDirectory().getPath() + "/video2AllAAC.aac";

    private PlayerView playerView;

    private AudioPlayer mPlayer;

    private Button button;

    public byte[] outData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        playerView = findViewById(R.id.exo_player);
        button = findViewById(R.id.button);
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        //renderer.audioSink
        MediaItem mediaItem = MediaItem.fromUri(strVideo);
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DefaultAudioSink)((MediaCodecAudioRenderer) player.player.renderers[1]).audioSink).isTransform = !((DefaultAudioSink)((MediaCodecAudioRenderer) player.player.renderers[1]).audioSink).isTransform;
                ExoPlayerImplInternal.isBefore = !ExoPlayerImplInternal.isBefore;
            }
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if(!isPlaying){
                    ((ProgressiveMediaPeriod.SampleStreamImpl)(player.player.renderers[1]).getStream()).writeData(aacPath);
                    //outData = ((DefaultAudioSink)((MediaCodecAudioRenderer) player.player.renderers[1]).audioSink).getOutData();
//                    Log.d("TAG", "onIsPlayingChanged: " + outData);
//                    ((DefaultAudioSink)((MediaCodecAudioRenderer) player.player.renderers[1]).audioSink).audioTrack.write(outData,0,outData.length);

//                    int sampleRate = ((DefaultAudioSink)((MediaCodecAudioRenderer) player.player.renderers[1]).audioSink).audioTrack.getSampleRate();
//
//                    mPlayer = new AudioPlayer(sampleRate, AudioFormat
//                            .CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
//                    mPlayer.init();
////                    int type = 0;
////                    StringBuilder sb = new StringBuilder();
////                    for (byte outDatum : outData) {
////                        if(type > 400){
////                            type = 0;
////                            Log.d("TAG", "abc onIsPlayingChanged: " + sb.toString());
////                            sb = new StringBuilder();
////                        }
////                        sb.append(outDatum + ",");
////                        type++;
////                        //Log.d("TAG", outDatum + " ");
////                    }
//                    mPlayer.play(outData, 0, outData.length);
                }
            }
        });
        player.play();
        //player.seekTo(10000);

    }

    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (getBaseContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (getBaseContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("cbs","isGranted == "+isGranted);
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }

    }

}