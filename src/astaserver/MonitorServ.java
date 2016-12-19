package astaserver;

/**
 *
 * @author Gioele Salmaso
 */
import java.util.Observable;

public class MonitorServ extends Observable {

    private int baseAsta;
    private String nomeClient;

    public MonitorServ(int baseAsta) {
        this.baseAsta = baseAsta;
        nomeClient = "Server";
    }

    //inizio metodi sincronizzati getter and setter 
    synchronized public int getBaseAsta() {
        return baseAsta;
    }

    synchronized public String getNomeClient() {
        return nomeClient;
    }

    synchronized public void setNomeClient(String nomeClient) {
        this.nomeClient = nomeClient;
    }

    /**
     * metodo che verifica se la l'offerta ricevuta è maggiore di quella
     * precedente; se lo è, l'offerta corrente viene aggiornata
     *
     * @param baseAsta
     * @param nomeCL
     */
    synchronized public void setBaseAsta(int baseAsta, String nomeCL) {
        if (getBaseAsta() < baseAsta) {
            this.baseAsta = baseAsta;
            setNomeClient(nomeCL);
            setChanged();
            notifyObservers();
        }
    }

}
