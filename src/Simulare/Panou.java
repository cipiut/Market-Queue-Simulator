package Simulare;
import GUI.GUI;
import Strategy.*;

import java.util.*;

public class Panou implements Runnable{

    private int peek=0;//Peek time
    private int[] wait;//wait Time fiecare clasa
    private int[] clienti;//total clienti la fiecare casa
    private int nrCase;//numarul total de case
    public static int simTime;// timpul simulari
    private int serviceTimeMin;
    private int serviceTimeMax;
    private int arriveTimeMin;
    private int arriveTimeMax;
    private Thread thread;//thread-u pentru generator si simulare
    private List<Casa> list = Collections.synchronizedList(new LinkedList<Casa>());
    private Strategy strategy;//strategia pentru generator

    private Timer timer=new Timer();

    public static int curentTime=0;
    TimerTask t=new TimerTask() {
        @Override
        public void run() {
            curentTime++;

            if(curentTime>simTime){
                timer.cancel();
            }
        }
    };

    public Panou(int nrCase, int serviceTimeMin, int serviceTimeMax, int arriveTimeMin, int arriveTimeMax, int simTime, String choice) {
        this.nrCase=nrCase;
        this.setStrategy(choice);
        this.simTime=simTime;
        makeCasa(nrCase);
        this.serviceTimeMin=serviceTimeMin;
        this.serviceTimeMax=serviceTimeMax;
        this.arriveTimeMax=arriveTimeMax;
        this.arriveTimeMin=arriveTimeMin;
        thread= new Thread(this);
        thread.setDaemon(true);
    }

    public void setStrategy(String s) {
        if(s.equals("Min Clienti"))strategy=new MinClient(list);
        else if(s.equals("Min Wait"))strategy=new MinWait(list);
        else strategy=new RandomStrategy(nrCase);
    }

    private void makeCasa(int n){//se initializeaza toate casele si clienti
        wait = new int[nrCase];
        clienti = new int[nrCase];
        for(int i=0;i<n;i++)list.add(new Casa(i));
    }

    public synchronized void start(){//pornim thread-urile si timerele
        thread.start();
        timer.scheduleAtFixedRate(t,1000/ GUI.SPEED,1000/ GUI.SPEED);
        for(int i=0;i<list.size();i++){
            list.get(i).start();
        }
    }

    public int totalClienti(){//numarul total de clienti din simulare
        int n=0;
        for(int i=0;i<nrCase;i++){
            n+=clienti[i];
        }
        return n;
    }

    public int peekClienti(){//functie care imi numara totalu de clienti la un anumit moment
        int n=0;
        for(int i=0;i<list.size();i++){
            n+=list.get(i).getList().size();
        }
        return n;
    }

    private double avgSimulationWaitTime(){//timpu mediu de asteptare pentru simulare
        double k=0;
        for(int i=0;i<nrCase;i++){
            k+=wait[i];
        }
        return (k/totalClienti());
    }

    private String avgWaitTimeCasa(){//timpu mediu de asteptare pentru casa
        String[] rez=new String[nrCase];
        String r="\n";
        for(int i=0;i<nrCase;i++){
            rez[i]="Casa "+(i+1)+" average time: "+String.valueOf((double)wait[i]/clienti[i])+"\n";
        }
        for(int i=0;i<nrCase;i++){
            r+=rez[i];
        }
        return r;
    }

    public List<Casa> getList() {
        return list;
    }

    @Override
    public void run() {//aici porneste thread-ul si generatorul
        int n,serv,id=0,max=0,aux;
        Random r = new Random();
        while (simTime>curentTime) {
            n = arriveTimeMin + r.nextInt(arriveTimeMax-arriveTimeMin+1);//genereaza random arrive time
            try {
                Thread.sleep(n * 1000/ GUI.SPEED +1);//timpu de asteptare in functie de Speed selectat de utilizator
            } catch (InterruptedException e) {}
            serv = serviceTimeMin + r.nextInt(serviceTimeMax-serviceTimeMin+1);//genereaza random service time
            int indiceCase=strategy.getStrategy();id++;//se selecteaza strategia data de utilizator
            int s=id,n1=n,serv1=serv;
            if(curentTime>simTime)break;
            list.get(indiceCase).add(new Client(id,indiceCase+1,n,serv));//adaugare in lista
            wait[indiceCase]+=serv;//pentru calculul timpului mediu se aduna toate serviceTime-urile generate
            clienti[indiceCase]++;//pentru calculul timpului mediu se aduna toti clienti
            if(max<(aux=peekClienti())){//la fiecare unitate de timp se face update la peekTime
                peek=curentTime;
                max=aux;
            }
            if(simTime>=curentTime)javafx.application.Platform.runLater( () -> GUI.err.appendText(curentTime+": Client "
                    +s+" intra la casa "+(indiceCase+1)+" SERVICE TIME: "+ serv1+" ARRIVE TIME: "+n1+
                    " WAIT TIME: "+list.get(indiceCase).getWaitTime()+"\n"));//se scrie in TextArea adaugarea
        }
        //se afiseaza Statistica
        javafx.application.Platform.runLater( () -> GUI.err.appendText("STOP SIMULARE\n\nStatistica\nPeek Time: " +peek));
        javafx.application.Platform.runLater( () -> GUI.err.appendText("\nAverage Simulation Waiting Time: "
                +avgSimulationWaitTime()));
        javafx.application.Platform.runLater( () -> GUI.err.appendText("\nAverage Waiting Time: " +avgWaitTimeCasa()));
        simTime=0;//pentru oprirea sigura a thread-urilor
    }
}