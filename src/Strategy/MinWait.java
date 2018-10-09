package Strategy;

import Simulare.Casa;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MinWait implements Strategy {//strategie pentru punerea la casa cu cel mai mic wait time
    private List<Casa> list = Collections.synchronizedList(new LinkedList<Casa>());

    public MinWait(List<Casa> list) {
        this.list = list;
    }

    @Override
    public int getStrategy() {
        int n=0,min=list.get(0).getWaitTime();
        for(int i=0;i<list.size();i++){
            int f=list.get(i).getWaitTime();//compara waiting time-ul alege minimul
            if(min>f){
                min=f;
                n=i;
            }
        }
        return n;
    }
}