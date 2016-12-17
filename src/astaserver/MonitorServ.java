package astaserver;

import java.util.Observable;

/**
 *
 * @author Gioele Salmaso
 */
public class MonitorServ extends Observable {

    private int baseAsta;
    private String nomeClient;
    private Time tm;

    public MonitorServ(int baseAsta) {
        this.baseAsta = baseAsta;
        nomeClient = "Server";
    }

    synchronized public int getBaseAsta() {
        return baseAsta;
    }

    synchronized public String getNomeClient() {
        return nomeClient;
    }

    synchronized public void setNomeClient(String nomeClient) {
        this.nomeClient = nomeClient;
    }
    
    synchronized public int sendTime()
    {
        setChanged();
        notifyObservers();
        return tm.getI();     
    }

    //metodo che verifica se la l'offerta ricevuta è maggiore di quella precedente
    //se lo è, l'offerta corrente viene aggiornata 
    synchronized public void setBaseAsta(int baseAsta, String nomeCL) {
        if (getBaseAsta() < baseAsta) {
            this.baseAsta = baseAsta;
            tm.setI();
            setNomeClient(nomeCL);
            setChanged();
            notifyObservers();
        }
    }

}
