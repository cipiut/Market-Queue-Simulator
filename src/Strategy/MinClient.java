package Strategy;

import Simulare.Casa;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MinClient implements Strategy {//strategie pentru punerea la casa cu cei mai putini clienti
    private List<Casa> list = Collections.synchronizedList(new LinkedList<Casa>());

    public MinClient(List<Casa> list) {
        this.list = list;
    }

    @Override

    public int getStrategy() {
        int n=0,min=list.get(0).getList().size();
        for(int i=0;i<list.size();i++){
            int f=list.get(i).getList().size();//compara cati clienti stau la casa si alege minimul
            if(min>f){
                min=f;
                n=i;
            }
        }
        return n;
    }
}