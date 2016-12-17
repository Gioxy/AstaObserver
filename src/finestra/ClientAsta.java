package finestra;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Gioele Salmaso
 */
public class ClientAsta extends Thread {

    Finestra finestra;
    InputStreamReader in;
    BufferedReader c;
    PrintWriter out;

    public ClientAsta(Socket serv,Finestra finestra) throws IOException {
        this.finestra = finestra;
        in = new InputStreamReader(serv.getInputStream());
        c = new BufferedReader(in);
        out = new PrintWriter(serv.getOutputStream(), true);
        out.println(finestra.getNome());//invio del nome del client al server   

    }

    public void sendAsta(int rilancio) {
        out.println(rilancio);

    }
    
    //thread che rimane in ascolto di nuove offerte 
    @Override
    public void run() {
        while (true) {

            try {
                //String asctm=c.readLine();
                //finestra.getTempo().setText(asctm);
                String ascolto = c.readLine();
                finestra.getSag().setText(ascolto);
                String asc = c.readLine();
                finestra.getAgs().setText(asc);
                if (Integer.parseInt(finestra.getSld().getText()) < Integer.parseInt(finestra.getAgs().getText())) {
                    finestra.getAqSaldo().setVisible(false);
                    finestra.getRil().setVisible(false);
                    finestra.getDisc().setVisible(false);
                    finestra.getIp1().setEditable(false);
                    finestra.getAqIp().removeActionListener((ActionListener) this);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientAsta.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
