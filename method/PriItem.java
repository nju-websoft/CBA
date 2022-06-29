package method;
import java.util.Comparator;

public class PriItem {
    public int start;
    public int v;
    public double pri;
    public int hop;
    public int startKwd;
    public double weight;

    public PriItem(int start, int v, double pri, int hop, int startKwd, double weight)
    {
        this.start=start;
        this.v=v;
        this.pri=pri;
        this.hop=hop;
        this.startKwd=startKwd;
        this.weight=weight;
    }

    public static Comparator<PriItem> priItemComparator=new Comparator<PriItem>()
    {
        @Override
        public int compare(PriItem pi1,PriItem pi2)
        {
            if(pi1.pri>pi2.pri) return -1;
            else if(pi1.pri==pi2.pri) return 0;
            else return 1;
        }
    };

    public static Comparator<PriItem> priAndWeightComparator=new Comparator<PriItem>()
    {
        @Override
        public int compare(PriItem pi1,PriItem pi2)
        {
            if(pi1.pri>pi2.pri) return -1;
            else if(pi1.pri<pi2.pri) return 1;
            else
            {
                if(pi1.weight<pi2.weight) return -1;
                else if(pi1.weight>pi2.weight) return 1;
                else return 0;
            }
        }
    };


}
