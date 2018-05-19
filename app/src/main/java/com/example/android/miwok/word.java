package com.example.android.miwok;

/**
 * Created by User on 12-12-2017.
 */

public class word {

    private String mdefault;
    private String mmiwok;
    private int image;
    private int check_image=-1;
    private int check_audio=-1;
    private int audioid;

    public word(String a,String b,int aud)
    {
        mdefault=a;
        mmiwok=b;
        audioid=aud;
        check_audio=1;
    }

    public word(String a,String b,int img,int aud)
    {
        mdefault=a;
        mmiwok=b;
        image=img;
        check_image=1;
        audioid=aud;
        check_audio=1;
    }
    public String getMdefault()
    {
        return mdefault;
    }
    public String getMmiwok()
    {
        return mmiwok;
    }
    public int getimage()
    {
        return image;
    }
    public int getAudioid()
    {
        return audioid;
    }
    public boolean hasimage()
    {
        if(check_image==-1)
            return false;
        else
            return true;
    }
    public boolean hasaudio()
    {
        if(check_audio==-1)
            return false;
        else
            return true;
    }
}
