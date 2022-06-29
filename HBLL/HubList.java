package HBLL;

import java.util.ArrayList;
import java.util.Collections;

public class HubList {
   public ArrayList<Hub> list;//list sort in descending order

   public HubList()
   {
       list=new ArrayList<Hub>(0);
   }

   public void add(int v,int hop,double dist,int pred)
   {
       if(list.size()!=0&&list.get(list.size()-1).v==v)
       {
           list.get(list.size()-1).add(hop,dist,pred);
       }
       else
       {
            Hub h=new Hub(v);
            h.add(hop,dist,pred);
            list.add(h);
       }
   }

   public int size()
   {
       return list.size();
   }

   public void sort()
   {
       Collections.sort(this.list,Hub.hubComparator);
   }
}
