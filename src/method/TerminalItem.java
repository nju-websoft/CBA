package method;

import java.util.Map;

public class TerminalItem
{
    public int terminal;
    public double dis;
    public int mid;//hub node
    public int hop1;//hop from mid to origin
    public int hop2;//hop from mid to terminal
    public int freshOrigin;//origin in tree
    public int vStart;
    public int vEnd;
    public int originHubLoc;
    public int terminalHubLoc;

    public static final int INF=0x3f3f3f3f;

    public TerminalItem(int terminal)
    {
        this.terminal=terminal;
        this.dis=INF;
        this.mid=-1;
        this.hop1=-1;
        this.hop2=-1;
        this.freshOrigin=-1;
        this.vStart=-1;
        this.vEnd=-1;
        this.originHubLoc=-1;
        this.terminalHubLoc=-1;
    }

    public class InfoItem
    {
        int origin;
        double dis;
        int pre;
        int HubLoc;
        public InfoItem(int origin,double dis,int pre,int HubLoc)
        {
            this.origin=origin;
            this.dis=dis;
            this.pre=pre;
            this.HubLoc=HubLoc;
        }

        public void change(int origin,double dis,int pre,int HubLoc)
        {
            this.origin=origin;
            this.dis=dis;
            this.pre=pre;
            this.HubLoc=HubLoc;
        }

    }

    public void freshdis(HopLimitHL hl, TreeHubSub thb, int hop, AnsTree tree)
    {
        if(vStart==-1)
        {
            vStart=hl.vIndicator[terminal];
            if(terminal==hl.nodeNum-1){vEnd=hl.vStoredNum;}
            else{vEnd=hl.vIndicator[terminal+1];}
        }
        for(int i=vStart;i<vEnd;i++)
        {
            int v=hl.vOfLabel[i];
            if(thb.array[v]==null)
            {
                continue;
            }
            InfoItem[] dynamicHL=new InfoItem[hop+1];
            for(TreeHubSub.VInfoItem vInfoItem:thb.array[v])
            {
                int hStart=vInfoItem.hStart,hEnd=vInfoItem.hEnd;
                int depth=tree.depth(vInfoItem.origin);
                for(int h=hStart;h<hEnd;h++)
                {
                    int dynamicHop=hl.hopOfLabel[h]+depth;
                    if(dynamicHop>hop)
                    {
                        break;
                    }
                    if(dynamicHL[dynamicHop]==null)
                    {
                        dynamicHL[dynamicHop]=new InfoItem(vInfoItem.origin,hl.disOfLabel[h],hl.preOfLabel[h],h);
                    }
                    else if(hl.disOfLabel[h]+1e-6<=dynamicHL[dynamicHop].dis)
                    {
                        dynamicHL[dynamicHop].change(vInfoItem.origin,hl.disOfLabel[h],hl.preOfLabel[h],h);

                    }
                }
            }
            double nearestDis=INF;
            for(int j=0;j<=hop;j++)
            {
                if(dynamicHL[j]!=null)
                {
                    if(dynamicHL[j].dis+1e-6<=nearestDis)
                    {
                        nearestDis=dynamicHL[j].dis;
                    }
                    else
                    {
                        dynamicHL[j]=null;
                    }
                }
            }
            int hStart,hEnd;
            hStart=hl.hubIndicator[i];
            if(i==hl.vStoredNum-1){hEnd=hl.hubStoredNum;}
            else{hEnd=hl.hubIndicator[i+1];}
            int hopiter1=hStart;int hopiter2=hop;
            while(hopiter1<hEnd&&hl.hopOfLabel[hopiter1]<=hop&&hopiter2>=0)
            {
                if(dynamicHL[hopiter2]==null)
                {
                    hopiter2--;
                    continue;
                }
                if(hl.hopOfLabel[hopiter1]+hopiter2<=hop)
                {
                    double dis_=hl.disOfLabel[hopiter1]+dynamicHL[hopiter2].dis;
                    if(dis_+1e-6<=dis)
                    {
                        dis=dis_;
                        hop1=hopiter2;
                        hop2=hl.hopOfLabel[hopiter1];
                        mid=v;
                        freshOrigin=dynamicHL[hopiter2].origin;
                        originHubLoc=dynamicHL[hopiter2].HubLoc;
                        terminalHubLoc=hopiter1;
                    }
                    hopiter1++;
                }
                else
                {
                    hopiter2--;
                }
            }
        }
    }


    public void freshdis(HopLimitHL hl, int hop, AnsTree tree)
    {
        int vStart,vEnd;
        vStart=hl.vIndicator[terminal];
        if(terminal==hl.nodeNum-1){vEnd=hl.vStoredNum;}
        else{vEnd=hl.vIndicator[terminal+1];}
        int originStart,originEnd;
        int vIter,originIter;
        int tempOrigin=tree.root;
        int tempHop=hop;
        originStart=hl.vIndicator[tempOrigin];
        if(tempOrigin==hl.nodeNum-1){originEnd=hl.vStoredNum;}
        else{originEnd=hl.vIndicator[tempOrigin+1];}
        vIter=vStart;originIter=originStart;
        while(vIter<vEnd&&originIter<originEnd)
        {
            if(hl.vOfLabel[originIter]==hl.vOfLabel[vIter])
            {
                int originHStart=hl.hubIndicator[originIter],vHStart=hl.hubIndicator[vIter];
                int originHEnd,vHEnd;
                if(originIter==hl.hubIndicator.length-1) {originHEnd=hl.hubStoredNum;}
                else {originHEnd=hl.hubIndicator[originIter+1];}
                if(vIter==hl.hubIndicator.length-1) {vHEnd=hl.hubStoredNum;}
                else {vHEnd=hl.hubIndicator[vIter+1];}
                int originHIter=originHStart,vHIter=vHEnd-1;
                while(originHIter<originHEnd&&hl.hopOfLabel[originHIter]<=tempHop&&vHIter>=vHStart)
                {
                    if(hl.hopOfLabel[originHIter]+hl.hopOfLabel[vHIter]<=tempHop)
                    {
                        if(dis>=hl.disOfLabel[originHIter]+hl.disOfLabel[vHIter]+1e-6)
                        {
                            dis=hl.disOfLabel[originHIter]+hl.disOfLabel[vHIter];
                            hop1=hl.hopOfLabel[originHIter];
                            hop2=hl.hopOfLabel[vHIter];
                            mid=hl.vOfLabel[originIter];
                            freshOrigin=tempOrigin;
                        }
                        originHIter++;
                    }
                    else
                    {
                        vHIter--;
                    }
                }
                originIter++;vIter++;
            }
            else if(hl.vOfLabel[originIter]<hl.vOfLabel[vIter])
            {
                originIter++;
            }
            else
            {
                vIter++;
            }
        }

        for(Map.Entry<Integer,Integer> entry: tree.preNode.entrySet())
        {
            tempOrigin=entry.getKey();
            int depth=tree.depth(tempOrigin);
            if(depth>hop) continue;
            tempHop=hop-depth;
            originStart=hl.vIndicator[tempOrigin];
            if(tempOrigin==hl.nodeNum-1){originEnd=hl.vStoredNum;}
            else{originEnd=hl.vIndicator[tempOrigin+1];}
            vIter=vStart;originIter=originStart;
            while(vIter<vEnd&&originIter<originEnd)
            {
                if(hl.vOfLabel[originIter]==hl.vOfLabel[vIter])
                {
                    int originHStart=hl.hubIndicator[originIter],vHStart=hl.hubIndicator[vIter];
                    int originHEnd,vHEnd;
                    if(originIter==hl.hubIndicator.length-1) {originHEnd=hl.hubStoredNum;}
                    else {originHEnd=hl.hubIndicator[originIter+1];}
                    if(vIter==hl.hubIndicator.length-1) {vHEnd=hl.hubStoredNum;}
                    else {vHEnd=hl.hubIndicator[vIter+1];}
                    int originHIter=originHStart,vHIter=vHEnd-1;
                    while(originHIter<originHEnd&&hl.hopOfLabel[originHIter]<=tempHop&&vHIter>=vHStart)
                    {
                        if(hl.hopOfLabel[originHIter]+hl.hopOfLabel[vHIter]<=tempHop)
                        {
                            if(dis>=hl.disOfLabel[originHIter]+hl.disOfLabel[vHIter]+1e-6)
                            {
                                dis=hl.disOfLabel[originHIter]+hl.disOfLabel[vHIter];
                                hop1=hl.hopOfLabel[originHIter];
                                hop2=hl.hopOfLabel[vHIter];
                                mid=hl.vOfLabel[originIter];
                                freshOrigin=tempOrigin;
                            }
                            originHIter++;
                        }
                        else
                        {
                            vHIter--;
                        }
                    }
                    originIter++;vIter++;
                }
                else if(hl.vOfLabel[originIter]<hl.vOfLabel[vIter])
                {
                    originIter++;
                }
                else
                {
                    vIter++;
                }
            }
        }
    }
}



