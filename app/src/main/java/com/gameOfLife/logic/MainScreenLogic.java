package com.gameOfLife.logic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.GridLayout;

import com.gameOfLife.R;
import com.gameOfLife.algo.MemoizedTreeUniverse;
import com.gameOfLife.algo.TreeUniverse;
import com.gameOfLife.algo.UniverseInterface;
import com.gameOfLife.ui.MainScreenActivity;
import com.gameOfLife.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainScreenLogic extends GameOfLifeBase {
    public static String TAG = "MainScreenLogic";
    private boolean startStopFlag;

    public MainScreenLogic(Activity activity) {
        super.attachView(activity);
    }

    public boolean isStartStopFlag() {
        return startStopFlag;
    }

    public void setStartStopFlag(boolean startStopFlag) {
        this.startStopFlag = startStopFlag;
    }

    public void startAlgo(final int patterType, final int[][] values) {
        final Activity activity = ((Activity) super.getView());
        new Thread() {
            public void run() {
                try {
                    main(values, patterType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void dettachView() {
        Activity context;
        if (super.isViewPresent()) {
            context = ((Activity) super.getView());
            GridLayout gridLayout = (GridLayout) context.findViewById(R.id.grid_layout);
            gridLayout.removeAllViews();
        }
        super.dettachView();
    }


    public void readPattern(int[][] data, UniverseInterface univ) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (data[i][j] == 1) {
                    univ.setBit(j, i);
                }
            }
        }
    }


    /**
     * Read a pattern in RLE format.  This format uses 'b' for a blank,
     * 'o' for a live cell, '$' for a new line, and integer repeat
     * counts before any of this.  It also contains a leading line
     * containing the x and y location of the upper right.  This is
     * not a full-fledged reader but it will read the patterns we
     * include.  Any ! outside a comment will terminate the pattern.
     */
    public void readPattern(UniverseInterface univ) {
        if (super.isViewPresent()) {
            Context mContext = (Activity) super.getView();
            InputStream ins = mContext.getResources().openRawResource(
                    mContext.getResources().getIdentifier("raw/test",
                            "raw", mContext.getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String inputLine = null;
            int x = 0, y = 0;              // current location
            int paramArgument = 0;         // our parameter argument so far
            try {
                while ((inputLine = br.readLine()) != null) {
                    // ignore lines that start with 'x' or '#'
                    if (inputLine.startsWith("x") || inputLine.startsWith("#"))
                        continue;
                    inputLine = inputLine.trim();
                    for (int i = 0; i < inputLine.length(); i++) {
                        char c = inputLine.charAt(i);
                        int param = (paramArgument == 0 ? 1 : paramArgument);
                        if (c == 'b') {
                            x += param;
                            paramArgument = 0;
                        } else if (c == 'o') {
                            while (param-- > 0) {
                                univ.setBit(x++, y);
                                Log.d(TAG, "x:" + x + ", y:" + y);
                            }
                            paramArgument = 0;
                        } else if (c == '$') {
                            y += param;
                            x = 0;
                            paramArgument = 0;
                        } else if ('0' <= c && c <= '9') {
                            paramArgument = 10 * paramArgument + c - '0';
                        } else if (c == '!') {
                            return;
                        } else {
                            Log.d(TAG, "In the input, I saw the character " + c +
                                    " which is illegal in the RLE subset I know how to handle.");
                            Log.d(TAG, "Usage:  java Driver <classname> <pattern.rle");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Main picks up the class to use for the UniverseImplementation
     * from the command line, loads and instantiates that class,
     * parses the RLE from the standard input, setting bits as it
     * goes, and then runs the Universe forever.
     * <p/>
     * type: user defined pattern or default;
     */
    public void main(int[][] data, int type) {
        long t2 = System.currentTimeMillis();
        MemoizedTreeUniverse univ = new MemoizedTreeUniverse();
        Log.d(TAG, "Reading pattern from standard input.");
        if (type == Constants.DEFAULT_PATTERN)
            readPattern(univ);
        else
            readPattern(data, univ);
        Log.d(TAG, "Simulating.");
        int i = 0;
        long diff = -1;
        boolean flag = true;
        int time = 100;
        while (startStopFlag) {
            i++;
            long t = System.currentTimeMillis();
            Log.d(TAG, "generation count:" + i + "; univ stats: " + univ.stats());
            try {
                univ.runStep();
                int[] intArray = ((TreeUniverse) univ).root.getBitArray2(Constants.GRID_SIZE);
                Message message = new Message();
                Bundle bundle = message.getData();
                bundle.putIntArray("data", intArray);
                message.setData(bundle);
                if (super.isViewPresent()) {
                    Handler handler = ((MainScreenActivity) super.getView()).getHandler();
                    if (handler != null)
                        handler.sendMessage(message);
                }
                synchronized (this) {
                    try {
                        Thread.sleep(Constants.DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                diff = System.currentTimeMillis() - t;
                Log.d(TAG, "GEN COUNT:" + i + " ,diff=" + diff);

            }
        }

    }


}
