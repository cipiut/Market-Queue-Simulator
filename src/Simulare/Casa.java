package Simulare;
import GUI.GUI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Casa implements Runnable {
    public int name;

    private List<Client> list = Collections.synchronizedList(new LinkedList<Client>());
    Thread thread ;//pentru fiecare casa exista un thread

    public Casa(int name) {
        thread= new Thread(this);
        thread.setDaemon(true);//fiecare thread se opreste cand opresc programu
        this.name = name;
    }

    public void add(Client c) {
        list.add(c);
    }

    public List<Client> getList() {
        return list;
    }

    public int getWaitTime(){//wait time pentru fiecare casa
        int rez=0;
        for(Client t:list)rez+=t.getServiceTime();
        return rez;
    }

    public Client remove() {//functie pentru stergerea din casa sub forma de queue
        Client c=list.get(0);
        list.remove(list.get(0));
        return c;
    }

    public synchronized void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (Panou.simTime>0) {
            if (list.size() > 0) {
                try {
                    Thread.sleep(list.get(0).getServiceTime() * 1000/ GUI.SPEED +1);//timpu de servire in
                    // functie de Speed selectat de utilizator
                } catch (InterruptedException e) {
                }
                if(Panou.curentTime>Panou.simTime)break;
                synchronized (list){
                    Client c=this.remove();//se sterge din casa primul client servit
                    javafx.application.Platform.runLater( () -> GUI.err.appendText(Panou.curentTime + ": Client " + c.getId() +
                            " iese de la casa " +c.getCasa()+ "\n"));// se adauga in TextArea stergerea din casa
                }

            }
        }
    }
}