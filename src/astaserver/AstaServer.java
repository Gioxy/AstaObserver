package astaserver;

/**
 * @author gioele.salmaso,matteo.scalone
 */
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
//import finestra.Time.tm;

public class AstaServer {

    final int Numero_Porta = 3333;
    Utenti[] utenti;
    int n_utenti;//variabile che contiene il numero di partecipanti all'asta 
    int i = 0;//indice per memorizzare ip e nome client
    int baseAsta;//variabile che indica lo stato della miglior offerta 
    MonitorServ ms;
    ServerSocket server;
    Time tm=new Time();

    public AstaServer(Utenti[] utenti, int n_utenti, int baseAsta, MonitorServ ms) {
        this.utenti = utenti;
        this.n_utenti = n_utenti;
        this.baseAsta = baseAsta;
        this.ms = ms;
        try {
            server = new ServerSocket(Numero_Porta, n_utenti);
        } catch (IOException ex) {
            Logger.getLogger(AstaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void accettazione(){
        try {
            while (true) {
                Socket client = server.accept();
                Cliente clt = new Cliente(client, ms,this);
                ms.addObserver(clt);
                clt.start();
                //fa partire il thread solo una volta,appena si connette il primo client
                if(ms.countObservers()==1)
                    tm.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Utenti[] getUtenti() {
        return utenti;
    }

    public void setUtenti(Utenti[] utenti) {
        this.utenti = utenti;
    }
    
    public void setUtenti(int i, Utenti utente){
        getUtenti()[i] = utente;
    }

    public int getN_utenti() {
        return n_utenti;
    }

    public void setN_utenti(int n_utenti) {
        this.n_utenti = n_utenti;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getBaseAsta() {
        return baseAsta;
    }

    public void setBaseAsta(int baseAsta) {
        this.baseAsta = baseAsta;
    }

    public MonitorServ getMs() {
        return ms;
    }

    public void setMs(MonitorServ ms) {
        this.ms = ms;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Time getTm() {
        return tm;
    }

    public void setTm(Time tm) {
        this.tm = tm;
    }
    
    public static void main(String[] args) {
        Scanner ut = new Scanner(System.in);
        System.out.println("Quanti utenti possono partecipare all'asta?");
        int n_utenti = ut.nextInt();
        Utenti [] utenti = new Utenti[n_utenti];//definisce la grandezza dell'array di utenti
        Random r=new Random();
        int baseAsta =30;//r.nextInt(100);
        MonitorServ ms = new MonitorServ(baseAsta);
        AstaServer server = new AstaServer(utenti,n_utenti,baseAsta,ms);
        server.accettazione();
    }

}

class Cliente extends Thread implements Observer {

    Socket client;
    BufferedReader in;
    PrintWriter out;
    MonitorServ ms;
    String nome ;
    AstaServer as;

    public Cliente(Socket client, MonitorServ ms,AstaServer as) {
        this.client = client;
        this.as=as;
        this.ms = ms;
        nome="";
        try{
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));//
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            
            nome = in.readLine();//stringa che memorizza il nome del client
            System.out.println("Si è collegato l'utente con ip " + client.getInetAddress() + " con il nome " + nome);

            //memorizzazione ip e nome del client all'interno del array di utenti
            as.setUtenti(as.getI(), new Utenti(nome, client.getInetAddress().toString()));
            if(as.getI()<as.getN_utenti())
                as.setI(as.getI()+1);
            System.out.println(Arrays.deepToString(as.getUtenti()));
            //comunicazione base d'asta iniziale ai client
            out.println(ms.getNomeClient());
            out.println(ms.getBaseAsta());

            //lettura rilancio da parte del client e set dell'offerta corrente
            while(true)
            {
                String lettRil = in.readLine();
                ms.setBaseAsta(Integer.parseInt(lettRil),nome);
                System.out.println("Ricevuto: " + lettRil + " da " + ms.getNomeClient());//utenti[i - 1].nome);
                System.out.println("Current asta: " + ms.getBaseAsta());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //metodo che comunica al client l'offerta migliore corrente 
    @Override
    public void update(Observable o, Object o1) {
        System.out.println("Aggiorno");
        //out.println(String.valueOf(ms.sendTime()));
        //uscita dell'informazione contenente il nome del client che ha effettuato l'offerta migliore
        out.println(String.valueOf(ms.getNomeClient()));
        out.println(String.valueOf(ms.getBaseAsta()));
    }
}
