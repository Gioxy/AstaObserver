package finestra;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.net.*;
import java.io.*;

public class Finestra implements ActionListener {

    final static int NUMERO_PORTA = 3333;//porta alla quale il client si connetterà al server 
    static Socket serv;

    static JTextField sld;//textefield con il saldo disponibile 
    static JPanel p = new JPanel();
    static JButton AqIp = new JButton("join");
    static String ipHint = "inserisci ip ";
    static String rilHint = "rilancia";
    static JTextField ip1 = new JTextField(ipHint);
    static String IP;
    static JButton AqSaldo = new JButton("rilancia");
    static JButton disc = new JButton("Exit");
    static JTextField ril = new JTextField(rilHint);
    static JTextField ags;//TextField per l'offerta corrente
    static JTextField sag;//TextField per chi ha effettuato l'offerta corrente
    static int ra;
    static String nome;//nome inserito dall'utente
    static ClientAsta cA;//Thread che gestisce gli in ed out del client 
    JLabel tempo;
    static boolean ext;

    public static void main(String[] args) {
        ext=false;
        
        nome = JOptionPane.showInputDialog("Inserisci il tuo nickname");
        JFrame f = new JFrame("ASTA");                            //finestra
        f.setSize(400, 400);
        f.setLocation(500, 100);
        f.setVisible(true);
        f.setLayout(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        p.setSize(400, 400);
        p.setLayout(null);
        p.setVisible(true);
        p.setBackground(Color.GRAY);
        f.add(p);
        //FocusListener che cancella/rimette il testo del textfield del ip appena l'utente schiccia su quest'ultimo 
        FocusListener ipvisibile = new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals(ipHint)) {
                    tf.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals("")) {
                    tf.setText(ipHint);
                }
            }

        };
        //FocusListener che cancella/rimette il testo del textfield del rilancio appena l'utente schiccia su quest'ultimo 
        FocusListener rilvisibile = new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals(rilHint)) {
                    tf.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals("")) {
                    tf.setText(rilHint);
                }
            }

        };

        ip1.setSize(100, 30);                                    //casella per l' ip
        ip1.setLocation(30, 30);
        ip1.addFocusListener(ipvisibile);
        ip1.setVisible(true);
        p.add(ip1);

        ril.setSize(100, 30);                                    //casella per rilancio
        ril.setLocation(30, 300);
        ril.addFocusListener(rilvisibile);
        ril.setVisible(true);
        p.add(ril);

        AqSaldo.setSize(100, 30);                                //bottone per rilanciare
        AqSaldo.setLocation(150, 300);
        AqSaldo.addActionListener(new Finestra());
        AqSaldo.setVisible(true);
        AqSaldo.setBackground(Color.LIGHT_GRAY);
        p.add(AqSaldo);

        disc.setSize(100, 30);                                   //bottone per disconnettersi (exit)
        disc.setLocation(250, 300);
        disc.addActionListener(new Finestra());
        disc.setVisible(true);
        disc.setBackground(Color.LIGHT_GRAY);
        p.add(disc);

        AqIp.setSize(70, 30);                                    //bottone per acquisizione ip (join)
        AqIp.setLocation(150, 30);
        AqIp.setVisible(true);
        AqIp.setBackground(Color.red);
        AqIp.addActionListener(new Finestra());
        p.add(AqIp);

        Random random = new Random();
        ra = random.nextInt(5000);
        String SaldoStr = String.valueOf(ra);

        JLabel SaldoDisponibile = new JLabel("Saldo Disponibile");
        SaldoDisponibile.setSize(150, 30);
        SaldoDisponibile.setLocation(150, 70);
        SaldoDisponibile.setVisible(true);
        SaldoDisponibile.setForeground(Color.WHITE);
        p.add(SaldoDisponibile);

        sld = new JTextField(SaldoStr);                  //mostro il saldo disponibile
        sld.setSize(100, 30);
        sld.setLocation(30, 70);
        sld.setVisible(true);
        sld.setEditable(false);
        p.add(sld);

        //label per "titolo" offerta corrente 
        JLabel offerta = new JLabel("OFFERTA CORRENTE");
        offerta.setSize(150, 30);
        offerta.setLocation(130, 120);
        offerta.setVisible(true);
        offerta.setForeground(Color.BLACK);
        p.add(offerta);
        //label per offerta corrente
        JLabel soldi = new JLabel("€");
        soldi.setSize(150, 30);
        soldi.setLocation(245, 170);
        soldi.setVisible(true);
        soldi.setForeground(Color.WHITE);
        p.add(soldi);
        //textfield per offerta corrente 
        ags = new JTextField();
        ags.setSize(100, 30);
        ags.setLocation(200, 200);
        ags.setVisible(true);
        ags.setEditable(false);
        p.add(ags);
        //label per nome cliente 
        JLabel cl = new JLabel("Nome cliente");
        cl.setSize(150, 30);
        cl.setLocation(100, 170);
        cl.setVisible(true);
        cl.setForeground(Color.WHITE);
        p.add(cl);
        //textfield per nome del cliente con l'offerta maggiore
        sag = new JTextField();
        sag.setSize(100, 30);
        sag.setLocation(100, 200);
        sag.setVisible(true);
        sag.setEditable(false);
        p.add(sag);
        //label per il tempo
        tempo=new JLabel();
        tempo.setSize(20,20);
        tempo.setLocation(350,100);
        tempo.setVisible(true);
        p.add(tempo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        //bottone per joinare all'asta 
        if (s.equalsIgnoreCase("join")) {
            if (ext == false) {
                AqIp.setBackground(Color.green);
                IP = getText(ip1);
                //procedura per la connessione
                try {
                    ip1.setEditable(false);
                    AqIp.removeActionListener(this);
                    serv = new Socket(IP, NUMERO_PORTA);
                    cA = new ClientAsta(serv);
                    cA.start();
                } catch (NoRouteToHostException ex) {
                    JOptionPane.showMessageDialog(p, "L'ip inserito non è valido");
                    ip1.setEditable(true);
                    AqIp.addActionListener(this);
                    AqIp.setBackground(Color.RED);
                } catch (IOException ex) {
                    //Logger.getLogger(Finestra.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(p, "L'ip inserito non è valido");
                    ip1.setEditable(true);
                    AqIp.addActionListener(this);
                    AqIp.setBackground(Color.RED);
                }
            }

        }
        //bottone per far uscire l'utente (nasconde i bottoni così che l'utente non possa più agire su di essi) 
        if (s.equalsIgnoreCase("exit")) {
            ext = true;
            AqSaldo.setVisible(false);
            ril.setVisible(false);
            disc.setVisible(false);
            ip1.setEditable(false);
            AqIp.removeActionListener(this);
        } else {
            //bottone per rilanciare 
            if (s.equalsIgnoreCase("rilancia") && ext == false) {
                if (Integer.parseInt(ags.getText()) >= Integer.parseInt(ril.getText())) {
                    JOptionPane.showMessageDialog(p, "L'offera inserita risulta minore o uguale all'offerta corrente");
                } else {
                    //controllo per vedere se l'offerta corrente è inferiore al saldo disponibile
                    if (ra >= Integer.parseInt(ril.getText())) {
                        ra = ra - Integer.parseInt(ril.getText());
                        sld.setText(String.valueOf(ra));
                        cA.sendAsta(Integer.parseInt(ril.getText()));
                    } else {
                        JOptionPane.showMessageDialog(p, "L'offerta che hai cercato di effettuare supera il tuo saldo disponibile");
                    }
                }

            }
        }
    }

    private String getText(JTextField ip1) {
        IP = ip1.getText();
        return IP;
    }

    public JLabel getTempo() {
        return tempo;
    }

}
