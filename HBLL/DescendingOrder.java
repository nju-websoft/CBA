package HBLL;

import java.util.ArrayList;

public class DescendingOrder {
    public void degreeOrder(PLL hpll)
    {
        ArrayList<OrderItem> degreeOrder=new ArrayList<OrderItem>();
        for(int i=0;i<hpll.ww.nodeNum;i++)
        {
            degreeOrder.add(new OrderItem(i,hpll.ww.graph.get(i).size()));
        }
        degreeOrder.sort(new OrderComparator());
        for(int i=0;i<hpll.ww.nodeNum;i++)
        {
            int temp=degreeOrder.get(i).v;
            hpll.originMap[i]=temp;
            hpll.changedMap[temp]=i;
        }
    }

    public void degreeOrder(Pruned_HBLL hpll)
    {
        ArrayList<OrderItem> degreeOrder=new ArrayList<OrderItem>();
        for(int i=0;i<hpll.nodeNum;i++)
        {
            degreeOrder.add(new OrderItem(i,hpll.graph.get(i).size()));
        }
        degreeOrder.sort(new OrderComparator());
        for(int i=0;i<hpll.nodeNum;i++)
        {
            int temp=degreeOrder.get(i).v;
            hpll.originMap[i]=temp;
            hpll.changedMap[temp]=i;
        }
    }

}
