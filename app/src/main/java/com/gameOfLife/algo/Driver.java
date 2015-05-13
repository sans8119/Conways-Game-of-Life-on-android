package com.gameOfLife.algo;

import android.os.Handler;

/**
 *   Invoke one of the Life implementations against a particular
 *   input pattern.  The input pattern is read from standard input.
 *
 *   Usage:  java Driver <classname> <pattern.rle
 *
 *   Warning:  runs forever; it may put your machine into swap
 *   or generate huge output files, so be prepared to stop it with
 *   a control-C.
 */
public class Driver {

    private String TAG="Driver";
    private Handler handler;
   // private UniverseInterface univ ;

    public boolean isStartStopFlag() {
        return startStopFlag;
    }

    private boolean startStopFlag;

    public void setStartStopFlag(boolean startStopFlag) {
        this.startStopFlag = startStopFlag;
    }

    public Driver(){
        startStopFlag=true;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }



}