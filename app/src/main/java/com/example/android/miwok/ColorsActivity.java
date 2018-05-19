package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;// obect of audiomanager type
    private  AudioManager.OnAudioFocusChangeListener  mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        releaseMediaPlayer();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }  else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mediaPlayer.start();
                    }
                }
            };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override

        public void onCompletion(MediaPlayer mediaPlayer) {

            // Now that the sound file has finished playing, release the media player resources.

            releaseMediaPlayer();

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);// initialised

        final ArrayList<word> colors= new ArrayList<word>();
        colors.add(new word("red","wetteti",R.drawable.color_red,R.raw.color_red));
        colors.add(new word("green","chokoki",R.drawable.color_green,R.raw.color_green));
        colors.add(new word("brown","takakki",R.drawable.color_brown,R.raw.color_brown));
        colors.add(new word("gray","toppopi",R.drawable.color_gray,R.raw.color_gray));
        colors.add(new word("black","kululi",R.drawable.color_black,R.raw.color_black));
        colors.add(new word("white","keleli",R.drawable.color_white,R.raw.color_white));
        colors.add(new word("dusty yellow","topissa",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        colors.add(new word("mustard yellow","chiwitta",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        wordAdapter adapter=new wordAdapter(this,colors,R.color.category_colors);

        ListView l1=(ListView) findViewById(R.id.color_list);
        l1.setAdapter(adapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word mword =colors.get(position);
           releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(  mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, mword.getAudioid());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }
    private void releaseMediaPlayer() {

        // If the media player is not null, then it may be currently playing a sound.

        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
