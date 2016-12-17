/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astaserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gioxy
 */
public class Time extends Thread {

    private int i;

    public Time() {
        this.i = 0;
    }
    

    public int getI() {
        return i;
    }

    public void setI() {
        i = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Time.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
    }

}


