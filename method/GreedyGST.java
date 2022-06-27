package method;

import HBLL.*;

import java.util.ArrayList;

public class GreedyGST {
    public static final double INF=0x3f3f3f3f;

    public static AnsTree greedyTreeEven(WeightedGraph ww,CertificateQueue min_maxweightQueue,HopLimitHL hl,int diambound)
    {
        if(min_maxweightQueue==null) return null;

        AnsTree opt=new AnsTree(min_maxweightQueue.certificate,min_maxweightQueue.kwnum,min_maxweightQueue.queue);
        ArrayList<TerminalItem> opt_tq=new ArrayList<>(0);
        TreeHubSub opt_th=new TreeHubSub(ww.nodeNum);
        opt_th.addterminal(hl,min_maxweightQueue.certificate);
        for(int i=0;i<min_maxweightQueue.queue.size();i++)
        {
            opt_tq.add(new TerminalItem(min_maxweightQueue.queue.get(i)));
        }
        while(!opt_tq.isEmpty())
        {
            double mindis=INF;
            int loc=-1;
            for(int i=0;i<opt_tq.size();i++)
            {
                opt_tq.get(i).freshdis(hl,opt_th,diambound/2,opt);
                if(mindis>opt_tq.get(i).dis)
                {
                    mindis=opt_tq.get(i).dis;
                    loc=i;
                }
            }
            opt.addTotalPath(opt_th,hl,opt_tq.get(loc),ww);
            opt_tq.remove(loc);
        }
        return opt;
    }

}
