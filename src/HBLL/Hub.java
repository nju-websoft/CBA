package HBLL;

import java.util.ArrayList;
import java.util.Comparator;

public class Hub {
    public class InfoItem
    {
        public int hop;
        public double dis;
        public int pred;
        public InfoItem(int hop,double dis,int pred)
        {
            this.hop=hop;
            this.dis=dis;
            this.pred=pred;
        }
    }

    public int v;
    public ArrayList<InfoItem> info;

    public Hub(int v)
    {
        this.v=v;
        this.info=new ArrayList<InfoItem>(0);
    }

    public void add(int hop,double dis,int pred)
    {
        InfoItem i=new InfoItem(hop,dis,pred);
        this.info.add(i);
    }

    public static Comparator<Hub> hubComparator=new Comparator<Hub>()
    {
        @Override
        public int compare(Hub h1,Hub h2)
        {
            return h1.v<h2.v?-1:(h1.v==h2.v?0:1);
        }
    };
}
