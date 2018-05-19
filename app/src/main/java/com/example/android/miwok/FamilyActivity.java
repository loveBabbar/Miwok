package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_family);
        mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);// initialised

        final ArrayList<word> family= new ArrayList<word>();
        family.add(new word("father","apa",R.drawable.family_father,R.raw.family_father));
        family.add(new word("mother","ata",R.drawable.family_mother,R.raw.family_mother));
        family.add(new word("son","angsi",R.drawable.family_son,R.raw.family_son));
        family.add(new word("daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
        family.add(new word("older brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        family.add(new word("younger brother","chaaliti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        family.add(new word("older sister","tete",R.drawable.family_older_sister,R.raw.family_older_sister));
        family.add(new word("younger sister","koliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        family.add(new word("grandmother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        family.add(new word("grandfather","paapa",R.drawable.family_grandfather,R.raw.family_grandfather));

        wordAdapter adapter=new wordAdapter(this,family,R.color.category_family);
        ListView l2=(ListView) findViewById(R.id.family_list);
        l2.setAdapter(adapter);

        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word mword = family.get(position);
                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, mword.getAudioid());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompletionListener);// we can also define inline but it will make an object for releasing every single time
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
