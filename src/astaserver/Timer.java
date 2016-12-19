/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astaserver;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gioxy
 */
public class Timer extends Observable implements Runnable{
    
    private int i;

    public Timer() {
        this.i = 20;
    }

    public int getI() {
        return i;
    }

    public int setI() {
        i=20;
        return i;
    }
    
    
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            i--;
        setChanged();
        notifyObservers();
        }
    }
    
}
