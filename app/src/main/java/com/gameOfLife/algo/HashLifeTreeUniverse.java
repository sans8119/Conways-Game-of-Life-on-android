package com.gameOfLife.algo;

import android.util.Log;

/**
 *   This Universe uses the full HashLife algorithm.
 *   Since this algorithm takes large steps, we have to
 *   change how we increment the generation counter, as
 *   well as how we construct the root object.
 */
public class HashLifeTreeUniverse extends TreeUniverse {

    public void runStep() {
        int coun=0;
        while (root.level < 3 ||
                root.nw.population != root.nw.se.se.population ||
                root.ne.population != root.ne.sw.sw.population ||
                root.sw.population != root.sw.ne.ne.population ||
                root.se.population != root.se.nw.nw.population)
            root = root.expandUniverse() ;
        double stepSize = Math.pow(2.0, root.level-2) ;
        Log.d("NG2", "Taking a step of " + nf.format(stepSize)+",  root.level="+root.level) ;
        root = root.nextGeneration();++coun;
        generationCount += stepSize;

        int z=(int)Math.pow(2,root.level);
        Log.d("NG", "generationCount="+generationCount+", counter"+coun + " Next gen:" + z);
        int[][] grid=root.getBitArray(20);
        StringBuffer str=new StringBuffer();
        str.append("\n");
        for(int j=0;j<grid.length;j++) {
            for (int i = 0; i < grid.length; i++) {
                str.append(grid[j][i]).append(" ");
            }
            str.append("\n");
        }
        Log.d("111",generationCount+"----------------------grid----------------------"+root.level);
        Log.d("111",str.toString());
        Log.d("111","************end**************");
 /*       StringBuffer str=new StringBuffer();
        if(z>20) z=20;
        for(int j=0;j<z;j++) {
            for (int i = 0; i < z; i++) { Log.d("111","i="+i+",j="+j+","+root.getBit(i,j));
                str.append(root.getBit(i,j)).append(",");
            }
            str.append("|");
        }
        Log.d("NG",root.level+" z="+z+"### "+str.toString());*/
    }
    {
        root = HashLifeTreeNode.create();
    }
}