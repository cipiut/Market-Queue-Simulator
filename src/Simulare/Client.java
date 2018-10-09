package Simulare;

public class Client {

    private int id=1;//numarul clientului sosit la casa
    private int casa;//casa la care se afla clientul
    private int arrivalTime;
    private int serviceTime;

    public Client(int id,int casa,int arrivalTime,int serviceTime){
        this.casa=casa;
        this.id=id;
        this.arrivalTime=arrivalTime;
        this.serviceTime=serviceTime;
    }

    public int getCasa() {
        return casa;
    }

    public int getId() {
        return id;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}
