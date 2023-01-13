package com.example;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Test {
    public static void main(String[] args) {
        Integer a = 1;
        Integer m = 1;
        Integer b = 2;
        Integer c = 128;
        Integer d = 128;
        Integer e = 321;
        Integer f = 321;
        Integer h = 321;
        Long g = 3L;
        System.out.println(c == d);
        System.out.println(a == m);
        System.out.println(h == f);
        System.out.println(h.equals(f));
        System.out.println(e.equals(f));
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));

//        Test.

//        Semaphore semaphore=new

        Integer integer=10;

        LinkedBlockingDeque
                linkedBlockingDequ;
        LinkedBlockingQueue linkedBlockingQueue;

        int i=10;
        int x= i++ + ++i+i--;

        System.out.println(x);

        integer=100;
        new  Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10000;i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println(i);
                }
            }
        }).start();


        new  Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10000;i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println(i);
                }
            }
        }).start();


    }
}
