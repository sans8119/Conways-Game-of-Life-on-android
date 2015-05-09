package com.gameOfLife.algo;

import android.util.Log;

import java.lang.ref.WeakReference;

/**
 *   This class contains the tree maintenance functions for quadtrees.
 */
public class TreeNodeBase {
    /**
     *   Construct a leaf cell.
     */
    TreeNodeBase(boolean living) {
        nw = ne = sw = se = null ;
        level = 0 ;
        alive = living ;
        population = alive ? 1 : 0 ;
    }
    /**
     *   Construct a node given four children.
     */
    TreeNodeBase(TreeNode nw_, TreeNode ne_, TreeNode sw_, TreeNode se_) {
        nw = nw_ ;
        ne = ne_ ;
        sw = sw_ ;
        se = se_ ;
        level = nw_.level + 1 ;
        population = nw.population + ne.population +
                sw.population + se.population ;
        alive = population > 0 ;
    }
    /**
     *   Factory methods to allow us to "override" the constructors.
     *   These two calls are the only places that the constructors
     *   should ever be called.  The first two are nonstatic member
     *   functions only so they can be overriden; they do not actually use
     *   the self class at all.  The third provides a mechanism for us
     *   to bootstrap the root.
     */
    TreeNode create(boolean living) {
        TreeNode node=new TreeNode(living) ;
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }
    TreeNode create(TreeNode nw, TreeNode ne, TreeNode sw, TreeNode se) {
        TreeNode node=new TreeNode(nw, ne, sw, se);
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }
    static TreeNode create() {
        TreeNode node=new TreeNode(false).emptyTree(3);
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }
    /**
     *   Set a bit in this node in its relative coordinate system;
     *   returns a whole new node since our nodes are immutable.
     *
     *   In the recursive call, we simply adjust the coordinate system
     *   and call down a level.
     */
    TreeNode setBit(int x, int y) {
        if (level == 0)
            return new TreeNode(true);
        // distance from center of this node to center of subnode is
        // one fourth the size of this node.
        int offset = 1 << (level - 2);
        if (x < 0)
            if (y < 0) {
                Log.d("Driver","1x="+x+", offset="+offset+",y="+y);
                return create(nw.setBit(x + offset, y + offset), ne, sw, se);
            } else {  Log.d("Driver","2x="+x+", offset="+offset+",y="+y);
                return create(nw, ne, sw.setBit(x + offset, y - offset), se);
            }
        else if (y < 0) { Log.d("Driver","3x="+x+", offset="+offset+",y="+y);
            return create(nw, ne.setBit(x - offset, y + offset), sw, se);
        } else {  Log.d("Driver","4x="+x+", offset="+offset+",y="+y);
            return create(nw, ne, sw, se.setBit(x - offset, y - offset));
        }
    }
    /**
     *   If we ever really need to get a bit one at a time, we can
     *   use this subroutine.  For convenience it returns 0/1 rather
     *   than false/true.
     */
    int getBit(int x, int y) {
        if (level == 0)
            return alive ? 1 : 0 ;
        int offset = 1 << (level - 2) ;
        if (x < 0)
            if (y < 0)
                return nw.getBit(x+offset, y+offset) ;
            else
                return sw.getBit(x+offset, y-offset) ;
        else
        if (y < 0)
            return ne.getBit(x-offset, y+offset) ;
        else
            return se.getBit(x-offset, y-offset) ;
    }
    /**
     *   Build an empty tree at the given level.
     */
    TreeNode emptyTree(int lev) {
        if (lev == 0)
            return create(false) ;
        TreeNode n = emptyTree(lev-1) ;
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(n);
        return create(n, n, n, n) ;
    }
    /**
     *   Expand the universe; return a new node up one level with the
     *   current node in the center.  Requires us to disassemble the
     *   current node.
     */
    TreeNode expandUniverse() {
        TreeNode border = emptyTree(level-1) ;
        TreeNode node=create(create(border, border, border, nw),
                create(border, border, ne, border),
                create(border, sw, border, border),
                create(se, border, border, border));
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }

    /*
     *   Our data; the class is immutable so all of these are final.
     */
    final TreeNode nw, ne, sw, se ; // our children
    final int level ;           // distance to root
    final boolean alive ;       // if leaf node, are we alive or dead?
    final double population ;   // we cache the population here
}