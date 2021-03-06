package com.gameOfLife.algo;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 *   CanonicalTreeNode extends TreeNode to canonicalize the nodes.
 *   We use a HashMap so we can return the canonicalized node.
 */
public class CanonicalTreeNode extends TreeNode {
    /**
     *   We need to provide a hashCode() and an equals() method to be
     *   able to hash these objects.
     */
    public int hashCode() {
        if (level == 0)
            return (int)population ;
        return System.identityHashCode(nw) +
                11 * System.identityHashCode(ne) +
                101 * System.identityHashCode(sw) +
                1007 * System.identityHashCode(se) ;
    }
    public boolean equals(Object o) {
        TreeNode t = (TreeNode)o ;
        if (level != t.level)
            return false ;
        if (level == 0)
            return alive == t.alive ;
        return nw == t.nw && ne == t.ne && sw == t.sw && se == t.se ;
    }

    /**
     *   Given a node, return the canonical one if it exists, or make it
     *   the canonical one.
     */
    TreeNode intern() {
        TreeNode canon = (TreeNode)hashMap.get(this) ;
        if (canon != null)
            return canon ;
        //Log.d("111","CanonicalTreeNode:hashmap size="+hashMap.size());
        hashMap.put(this, this) ;
        return this ;
    }
    /**
     *   Our canonicalization hashset.
     */
    static WeakHashMap hashMap = new WeakHashMap() ;
    /**
     *   Provide constructors.  The rest of the code manages the factory
     *   interface mechanism used by TreeNode.  We use intern() in all
     *   three create() functions to guarantee that all new nodes are
     *   canonicalized.
     */
    CanonicalTreeNode(boolean alive) {
        super(alive) ;
    }
    CanonicalTreeNode(TreeNode nw, TreeNode ne, TreeNode sw, TreeNode se) {
        super(nw, ne, sw, se) ;
    }
    /**
     *   We override the three create functions.
     */
    TreeNode create(boolean living) {
        TreeNode node=new CanonicalTreeNode(living).intern();
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }
    TreeNode create(TreeNode nw, TreeNode ne, TreeNode sw, TreeNode se) {
        TreeNode node=new CanonicalTreeNode(nw, ne, sw, se).intern();
        WeakReference<TreeNode> weakCounter = new WeakReference<TreeNode>(node);
        return node;
    }
    static TreeNode create() {
        return new CanonicalTreeNode(false).emptyTree(3) ;
    }
}