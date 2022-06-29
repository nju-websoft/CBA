package method;




import java.util.ArrayList;
import java.util.List;


public class TreeHubSub {
    public static int INF=0x3f3f3f3f;

    public class VInfoItem
    {
        public int origin;
        public int hStart;
        public int hEnd;

        public VInfoItem(int origin,int hStart,int hEnd)
        {
            this.origin=origin;
            this.hStart=hStart;
            this.hEnd=hEnd;
        }
    }

    public List<VInfoItem>[] array;

    public TreeHubSub(int nodeNum)
    {
        this.array=new ArrayList[nodeNum];

    }

    public void addterminal(HopLimitHL hl, int terminal)
    {
        int vStart,vEnd;
        vStart=hl.vIndicator[terminal];
        if(terminal==hl.nodeNum-1){vEnd=hl.vStoredNum;}
        else{vEnd=hl.vIndicator[terminal+1];}
        for(int i=vStart;i<vEnd;i++)
        {
            if(array[hl.vOfLabel[i]]==null)
            {
                array[hl.vOfLabel[i]]=new ArrayList<>(0);
            }
            int hStart,hEnd;
            hStart=hl.hubIndicator[i];
            if(i==hl.vStoredNum-1){hEnd=hl.hubStoredNum;}
            else{hEnd=hl.hubIndicator[i+1];}
            array[hl.vOfLabel[i]].add(new VInfoItem(terminal,hStart,hEnd));
        }
    }







}
