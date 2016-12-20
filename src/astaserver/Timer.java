package astaserver;

import java.util.Observable;

/**
 *
 * @author Gioele Salmaso
 */
public class Timer extends Observable implements Runnable{
    
    private int i;

    //inizializza il tempo a 20 secondi 
    public Timer() {
        this.i = 20;
    }

    public int getI() {
        return i;
    }

    //reimposta il tempo nel caso in cui si riceva un'offerta maggiore di quella corrente
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
