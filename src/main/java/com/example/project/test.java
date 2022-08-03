package com.example.project;

public class test {
    public static void main (String [] args){
        BTreeGeneric<Integer> btree = new BTreeGeneric<Integer>(3);
        btree.add(1);
        btree.add(2);
        btree.add(3);
        btree.add(4);
        btree.add(5);
        btree.add(6);
        btree.add(7);
        btree.add(8);
        btree.add(9);
        btree.add(10);
        btree.add(11);
        System.out.print(btree);
    }
}
