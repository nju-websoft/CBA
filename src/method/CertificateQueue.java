package method;

import java.util.ArrayList;

public class CertificateQueue {
    public int certificate;
    public double weightMin;
    public double weightMax;
    public ArrayList<Integer> queue;
    double minimum_pathWeight;
    double maximum_pathWeight;
    public int kwnum;

    static final double INF=0x3f3f3f3f;

    public CertificateQueue(int c)
    {
        certificate=c;
        weightMin=0;
        weightMax=0;
        minimum_pathWeight=INF;
        maximum_pathWeight=0;
        queue=new ArrayList<Integer>();
        kwnum=0;
    }

    public void addKwVertex(int v, double weight)
    {
        queue.add(v);
        kwnum++;
        weightMax+=weight;
        if(minimum_pathWeight>weight){minimum_pathWeight=weight;}
        if(maximum_pathWeight<weight){maximum_pathWeight=weight;}
    }

    public void calculateWeightMin()
    {
        if(kwnum<=1)
        {
            weightMin=maximum_pathWeight;
        }
        else
        {
            weightMin=minimum_pathWeight+maximum_pathWeight;
        }
    }



    public boolean isEmpty()
    {
        return (queue.size()==0);
    }


}
