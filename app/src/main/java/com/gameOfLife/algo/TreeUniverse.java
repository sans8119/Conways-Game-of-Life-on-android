package com.gameOfLife.algo;

import android.util.Log;

import java.text.NumberFormat;
/**
 *   This Universe uses a quadtree datastructure to hold our cells.
 */
public class TreeUniverse implements UniverseInterface {
    /**
     *   Set a single bit; can only do this before running, and once
     *   we've started running cannot change.
     */
    public void setBit(int x, int y) {
      /*
       *   We need to make sure the current universe is large enough
       *   to handle these parameters.  A root node at level n supports
       *   coordinates from -2^(n-1) .. 2^(n-1)-1.
       */
        while (true) {
            int maxCoordinate = 1 << (root.level - 1) ; Log.d("Driver","TreeUviverse:setBit:maxCoordinate= " + maxCoordinate + ", root.level" + root.level+", x="+x+", y="+y);
            if (-maxCoordinate <= x && x <= maxCoordinate-1 &&
                    -maxCoordinate <= y && y <= maxCoordinate-1)
                break ;
            root = root.expandUniverse() ;
        }
      /*
       *   Call our recursive routine to set the bit.Tr
       */
        root = root.setBit(x, y) ;
    }
    /**
     *   Run a step.  First, we make sure the root is large enough to
     *   include the entire next generation by checking that all border
     *   nodes in the 4x4 square three levels down are empty.  Then we
     *   simply invoke the next generation method of the node.
     */
    public void runStep() {int coun=0; long t=System.currentTimeMillis();
        while (root.level < 3 ||
                root.nw.population != root.nw.se.se.population ||
                root.ne.population != root.ne.sw.sw.population ||
                root.sw.population != root.sw.ne.ne.population ||
                root.se.population != root.se.nw.nw.population)
            root = root.expandUniverse() ;

        root = root.nextGeneration() ;

        generationCount++ ;

        int z=(int)Math.pow(2,root.level);
        Log.d("111", "generationCount="+generationCount+", level="+root.level+ "; time of finding next generation:"+(System.currentTimeMillis()-t));
       /* if(z>20) z=20;
        for(int j=0;j<z;j++) {
            for (int i = 0; i < z; i++) { Log.d("111", "i=" + i + ",j=" + j + "," + root.getBit(i, j));
                str.append(root.getBit(i,j)).append(",");
            }
            str.append("|");
        }*/

      //  if(generationCount<9) {
            /*int[][] grid=root.getBitArray(20);
            StringBuffer str=new StringBuffer();
            WeakReference<StringBuffer> weakCounter = new WeakReference<StringBuffer>(str);
            str.append("\n");
            for (int j = 0; j < grid.length; j++) {
                for (int i = 0; i < grid.length; i++) {
                    str.append(grid[j][i]).append(" ");
                }
                str.append("\n");
            }
            Log.d("111", generationCount + "----------------------grid----------------------" + root.level);
            Log.d("111", str.toString());
            Log.d("111", "************end**************");
        str=null;*/
            /*if(z>20) z=20;
             str=new StringBuffer();
            for(int j=-z/2;j<z/2;j++) {
                for (int i = -z/2; i < z/2; i++) {// Log.d("111", "i=" + i + ",j=" + j + "," + root.getBit(i, j));
                    str.append(root.getBit(i,j)).append(",");
                }
                str.append("\n");
            }
            Log.d("111", str.toString());*/
        //}
       // Log.d("NG",root.level+" z="+z+"### ");
    }
    /**
     *   Display the current statistics about the current generation.
     *   This should include the generation count and the population.
     */
    public String stats() {
        return "Generation " + nf.format(generationCount) +
                " population " + nf.format(root.population) ;
    }
    /*
     *   The data we use.
     */
    double generationCount = 0 ;
    public TreeNode root = TreeNode.create() ;
    NumberFormat nf = NumberFormat.getIntegerInstance() ;
    {
        nf.setMaximumFractionDigits(0) ;
    }
}