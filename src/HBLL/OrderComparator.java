package HBLL;

import java.util.Comparator;

public class OrderComparator implements Comparator<OrderItem> {
    @Override
    public int compare(OrderItem o1,OrderItem o2)
    {
        return o1.pri>o2.pri ?-1:(o1.pri==o2.pri?0:1);
    }//larger priority sorts forward
}
