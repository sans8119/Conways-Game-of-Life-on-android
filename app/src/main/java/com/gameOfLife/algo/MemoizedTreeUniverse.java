package com.gameOfLife.algo;

import java.lang.ref.WeakReference;

/**
 *   This Universe uses a memoized canonicalized quadtree datastructure
 *   to hold our cells.  The only difference is how we initialize root.
 */
public class MemoizedTreeUniverse extends TreeUniverse {
    {
        root = MemoizedTreeNode.create() ;
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(root);

    }
}