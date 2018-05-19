package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);



        mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);// initialised

        final ArrayList<word> words= new ArrayList<word>();
        words.add(new word("one","lutti",R.drawable.number_one,R.raw.number_one));
        words.add(new word("two","lutti1",R.drawable.number_two,R.raw.number_two));
        words.add(new word("three","lutti2",R.drawable.number_three,R.raw.number_three));
        words.add(new word("four","lutti3",R.drawable.number_four,R.raw.number_four));
        words.add(new word("five","lutti4",R.drawable.number_five,R.raw.number_five));
        words.add(new word("six","lutti5",R.drawable.number_six,R.raw.number_six));
        words.add(new word("seven","lutti6",R.drawable.number_seven,R.raw.number_seven));
        words.add(new word("eight","lutti7",R.drawable.number_eight,R.raw.number_eight));
        words.add(new word("nine","lutti8",R.drawable.number_nine,R.raw.number_nine));
        words.add(new word("ten","lutti9",R.drawable.number_ten,R.raw.number_ten));


        /* Log.v("numbersActivity"," word at index 0 is : "+ words.get(0));
        int index=0;
        while(index<words.size())
        {
            TextView t=new TextView(this);
            t.setText(words.get(index));
            rootview.addView(t);
            index++;
        }*/

        wordAdapter Adapter = new wordAdapter(this, words,R.color.category_numbers);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word mword = words.get(position);// array lise pe jo list item h vo dega, to isliye array list pe call karenge
                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(  mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                mediaPlayer = MediaPlayer.create(NumbersActivity.this, mword.getAudioid());
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
