package finestra;

import static finestra.Finestra.AqIp;
import static finestra.Finestra.AqSaldo;
import static finestra.Finestra.nome;
import static finestra.Finestra.serv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static finestra.Finestra.ags;
import static finestra.Finestra.disc;
import static finestra.Finestra.ip1;
import static finestra.Finestra.p;
import static finestra.Finestra.ril;
import static finestra.Finestra.sag;
import static finestra.Finestra.sld;
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

    public ClientAsta(Socket serv) throws IOException {
        in = new InputStreamReader(serv.getInputStream());
        c = new BufferedReader(in);
        out = new PrintWriter(serv.getOutputStream(), true);
        out.println(nome);//invio del nome del client al server       

    }

    public void sendAsta(int rilancio) {
        out.println(rilancio);

    }
    
    //thread che rimane in ascolto di nuove offerte 
    @Override
    public void run() {
        while (true) {

            try {
                String asctm=c.readLine();
                finestra.getTempo().setText(asctm);
                String ascolto = c.readLine();
                sag.setText(ascolto);
                String asc = c.readLine();
                ags.setText(asc);
                if (Integer.parseInt(sld.getText()) < Integer.parseInt(ags.getText())) {
                    AqSaldo.setVisible(false);
                    ril.setVisible(false);
                    disc.setVisible(false);
                    ip1.setEditable(false);
                    AqIp.removeActionListener((ActionListener) this);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientAsta.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
