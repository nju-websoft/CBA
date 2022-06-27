package HBLL;
import java.util.Comparator;

public class Ehop {
    public int v;
    public int hop;
    public double dis;
    public int pred;

    Ehop(int v, int hop, double dis, int pred)
    {
        this.v=v;
        this.hop=hop;
        this.dis=dis;
        this.pred=pred;
    }

    public void changeData(double dis,int pred)
    {
        this.dis=dis;
        this.pred=pred;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public static Comparator<Ehop> ehopComparator = new Comparator<Ehop>()
    {
        @Override
        public int compare(Ehop h1, Ehop h2)
        {
            if(h1.v!=h2.v) return h1.v-h2.v;
            return h1.hop-h2.hop;
        }
    };
}
