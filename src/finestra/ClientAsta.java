package finestra;

/**
 *
 * @author Gioele Salmaso
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ClientAsta extends Thread {

    Finestra finestra;
    InputStreamReader in;
    BufferedReader c;
    PrintWriter out;

    public ClientAsta(Socket serv, Finestra finestra) throws IOException {
        this.finestra = finestra;
        in = new InputStreamReader(serv.getInputStream());
        c = new BufferedReader(in);
        out = new PrintWriter(serv.getOutputStream(), true);
        out.println(finestra.getNome());
        /**
         * invio del nome del client al server
         */

    }

    public void sendAsta(int rilancio) {
        out.println(rilancio);

    }

    /**
     * thread che rimane in ascolto di nuove offerte
     */
    @Override
    public void run() {
        while (true) {

            try {

                String ascolto = c.readLine();
                finestra.getSag().setText(ascolto);
                String asc = c.readLine();
                finestra.getAgs().setText(asc);
                String l = c.readLine();
                finestra.getTempo().setText(l);
                if (l.equals("0 s")) {
                    if (finestra.getSag().getText().equalsIgnoreCase("server")) {
                        JOptionPane.showMessageDialog(finestra.getP(), "L'ASTA NON È STATA AGGIUDICATA A NESSUN UTENTE");
                        in.close();
                        out.close();
                        c.close();
                        finestra.getAqSaldo().setVisible(false);
                        finestra.getRil().setVisible(false);
                        finestra.getDisc().setVisible(false);
                        finestra.getIp1().setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(finestra.getP(), "HA VINTO L'ASTA L'UTENTE: " + finestra.getSag().getText() + " ,CON L'OFFERTA DI: " + finestra.getAgs().getText());
                        in.close();
                        c.close();
                        out.close();
                        finestra.getAqSaldo().setVisible(false);
                        finestra.getRil().setVisible(false);
                        finestra.getDisc().setVisible(false);
                        finestra.getIp1().setEditable(false);
                    }
                }
                /**
                 * controllo se il saldo disponibile è inferiore all'offerta
                 * corrente; se lo è, viene nascosto il tasto per rilanciare e
                 * il textfield per inserire la somma da rilanciare; (fase
                 * "spettatore")
                 */
                if (Integer.parseInt(finestra.getSld().getText()) < Integer.parseInt(finestra.getAgs().getText())) {
                    finestra.getAqSaldo().setVisible(false);
                    finestra.getRil().setVisible(false);
                    finestra.getDisc().setVisible(false);
                    finestra.getIp1().setEditable(false);
                    finestra.getAqIp().removeActionListener((ActionListener) this);
                }
            } catch (IOException ex) {
            }

        }

    }

}
