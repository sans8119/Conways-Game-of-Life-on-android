package com.gameOfLife.algo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gameOfLife.GameOfLifeApplication;
import com.gameOfLife.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

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
    private UniverseInterface univ ;

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

      /**
     *   If something is wrong with the command line we complain here.
     */
    void usage(String s) {
        Log.d(TAG,s) ;
        Log.d(TAG,"Usage:  java Driver <classname> <pattern.rle") ;
        //System.exit(10) ;
    }


     public void readPattern(int[][] data) {
            for (int i = 0; i < data.length; i++){
                for (int j = 0; j < data.length; j++) {
                    if(data[i][j]==1) {
                        univ.setBit(j, i);
                    }
                }
        }
    }


    /**
     *   Read a pattern in RLE format.  This format uses 'b' for a blank,
     *   'o' for a live cell, '$' for a new line, and integer repeat
     *   counts before any of this.  It also contains a leading line
     *   containing the x and y location of the upper right.  This is
     *   not a full-fledged reader but it will read the patterns we
     *   include.  Any ! outside a comment will terminate the pattern.
     */
    public void readPattern() {
        Context mContext= GameOfLifeApplication.getAppContext();
        InputStream ins = mContext.getResources().openRawResource(
                mContext.getResources().getIdentifier("raw/test",
                        "raw", mContext.getPackageName()));
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));//System.in)) ;
        mContext=null;
        String inputLine = null ;
        int x = 0, y = 0 ;              // current location
        int paramArgument = 0 ;         // our parameter argument so far
        try {
        while ((inputLine = br.readLine()) != null) {
            // ignore lines that start with 'x' or '#'
            if (inputLine.startsWith("x") || inputLine.startsWith("#"))
                continue ;
            inputLine = inputLine.trim() ;
            for (int i=0; i<inputLine.length(); i++) {
                char c = inputLine.charAt(i) ;
                int param = (paramArgument == 0 ? 1 : paramArgument) ;
                if (c == 'b') {
                    x += param ;
                    paramArgument = 0 ;
                } else if (c == 'o') {
                    while (param-- > 0) {
                        univ.setBit(x++, y);Log.d(TAG,"x:" + x + ", y:" + y);
                    }
                    paramArgument = 0 ;
                } else if (c == '$') {
                    y += param ;
                    x = 0 ;
                    paramArgument = 0 ;
                } else if ('0' <= c && c <= '9') {
                    paramArgument = 10 * paramArgument + c - '0' ;
                } else if (c == '!') {
                    return ;
                } else {
                    usage("In the input, I saw the character " + c +
                            " which is illegal in the RLE subset I know how to handle.") ;
                }
            }
        }
        }catch (IOException e){e.printStackTrace();}
    }


    /**
     *   Main picks up the class to use for the UniverseImplementation
     *   from the command line, loads and instantiates that class,
     *   parses the RLE from the standard input, setting bits as it
     *   goes, and then runs the Universe forever.
     *
     *   type: user defined pattern or default;
     */
    public void main(int[][] data, int type)  {
        long t2=System.currentTimeMillis();
          //  if (args.length != 1)
         //   usage("Please give me an argument containing the class name to use.") ;
        Log.d(TAG,"Main");
        String classToUse = "MamoizedTreeUniverse";//args[0] ;
        Log.d(TAG,"Instantiating class " + classToUse + "build/intermediates/exploded-aar/com.android.support/appcompat-v7/22.0.0/res") ;
        univ = new MemoizedTreeUniverse();//TreeUniverse();//(UniverseInterface)Class.forName(classToUse).newInstance() ;
        Log.d(TAG, "Reading pattern from standard input.") ;
        if(type==Constants.DEFAULT_PATTERN)
            readPattern();
        else
        readPattern(data) ;
        StringBuffer str=new StringBuffer();
        str.append("\n");
       int[][] grid=((TreeUniverse)univ).root.getBitArray(20);
        Log.d(TAG, "Simulating.");
        int i=0; long diff=-1; boolean flag=true; int time=100;
        while (startStopFlag) {
            i++;
             long t=System.currentTimeMillis();
             Log.d(TAG,"generation count:"+i+"; univ stats: "+univ.stats()) ;
    try {
        univ.runStep();
        str=new StringBuffer();
        WeakReference<StringBuffer> weakCounter = new WeakReference<StringBuffer>(str);
        WeakReference<int[][]> weakCounter2 = new WeakReference<int[][]>(grid);
        int[] intArray=((TreeUniverse)univ).root.getBitArray2(20);
        Message message=new Message();
        Bundle bundle=message.getData();
        bundle.putIntArray("data", intArray);
        message.setData(bundle);
        if(handler!=null)
        handler.sendMessage(message);
        synchronized(this) {
            try {
                Thread.sleep(Constants.DELAY);
            }catch (InterruptedException e){e.printStackTrace();}
        }
    }catch(OutOfMemoryError e){
        e.printStackTrace();
    }finally{ //this is done so that we can handle low memory cases.
        System.gc();
        if(flag){
            flag=false;
            TreeNode root=((TreeUniverse) univ).root;
            univ = new TreeUniverse();
            ((TreeUniverse) univ).root=root;
            Log.d(TAG,root.level+ " ******************************Switching the algo**************************************");
        }
    }
            diff=System.currentTimeMillis()-t;
            Log.d(TAG, "GEN COUNT:"+i+" ,diff="+diff);//+/*", allocated="+allocated+*/", available="+Runtime.getRuntime().totalMemory()+", free="+Runtime.getRuntime().freeMemory()+","+Runtime.getRuntime().totalMemory()/5);

        }

    }

}