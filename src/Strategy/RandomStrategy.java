package Strategy;

import java.util.Random;

public class RandomStrategy implements Strategy{//strategia random pentru generator
    int nrCase;

    public RandomStrategy(int nrCase) {
        this.nrCase = nrCase;
    }

    @Override
    public int getStrategy() {
        Random r =new Random();
        return r.nextInt(nrCase);//se genereaza random urmatoarea pozitie din casa
    }
}