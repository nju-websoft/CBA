package HBLL;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PLL {
    public class PLLHub {
        public int v;
        public int hop;
        public int pre;
        public PLLHub(int v, int hop, int pre)
        {
            this.v=v;
            this.hop=hop;
            this.pre=pre;
        }
    }

    public static Comparator<PLLHub> hubComparator=new Comparator<PLLHub>()
    {
        @Override
        public int compare(PLLHub h1,PLLHub h2)
        {
            if(h1.v<h2.v) return-1;
            else if(h1.v>h2.v) return 1;
            else return 0;
        }
    };

    public ArrayList<PLLHub>[] HubLabel;
    public WeightedGraph ww;
    public int[] originMap;
    public int[] changedMap;
    DescendingOrder order;
    public static final int INF=0x3f3f3f3f;

    boolean[] visit;
    int[] dist;
    int[] pre;
    ArrayList<Integer> visitStored;

    int[] dynamicHop;
    ArrayList<Integer> changedHopNodes;
    void pruneBfs(int i)
    {
        int v0=originMap[i];
        LinkedList<Integer> Q=new LinkedList<Integer>();
        visit[v0]=true;visitStored.add(v0);
        pre[v0]=v0;
        dist[v0]=0;
        Q.add(v0);
        for(PLLHub pllhub:HubLabel[v0])
        {
            int v_=pllhub.v;
            dynamicHop[v_]=pllhub.hop;
            changedHopNodes.add(v_);
        }
        while(Q.size()!=0)
        {
            int h=Q.poll();
            HubLabel[h].add(new PLLHub(v0,dist[h],pre[h]));
            for(Edge e:ww.graph.get(h))
            {
                int v_=e.v;
                if(!visit[v_])
                {
                    if(dist[h]+1<queryDynamic(v_)) {
                        dist[v_]=dist[h]+1;
                        pre[v_]=h;
                        Q.add(v_);
                    }
                    visit[v_]=true;
                    visitStored.add(v_);
                }
            }
        }
       for(Integer v_:visitStored)
       {
           visit[v_]=false;
           dist[v_]=INF;
           pre[v_]=-1;
       }
       for(Integer v_:changedHopNodes)
       {
           dynamicHop[v_]=INF;
       }
       changedHopNodes.clear();
       visitStored.clear();
    }

    int queryBFS(int u,int v)
    {
        boolean[] visit=new boolean[ww.nodeNum];
        visit[u]=true;
        LinkedList<PLLHub> Q=new LinkedList<PLLHub>();
        Q.add(new PLLHub(u,0,0));
        while(Q.size()!=0)
        {
            PLLHub h=Q.poll();
            if(h.v==v) return h.hop;
            for(Edge e:ww.graph.get(h.v))
            {
                int v_=e.v;
                if(!visit[v_])
                {
                    Q.add(new PLLHub(v_,h.hop+1,0));
                    visit[v_]=true;
                }
            }
        }
        return INF;
    }



    int queryDynamic(int v)
    {
        if(HubLabel[v].size()==0)
        {
            return INF;
        }
        int dis=INF;
        for(PLLHub h:HubLabel[v])
        {
            if(dynamicHop[h.v]==INF) continue;
            if(dis>dynamicHop[h.v]+h.hop)
            {
                dis=dynamicHop[h.v]+h.hop;
            }
        }
        return dis;
    }

    void writeOut(String directory)
    {
        try
        {
            File f=new File(directory+"PLLlabel.txt");
            f.createNewFile();
            PrintWriter out=new PrintWriter(f);

            out.println(ww.nodeNum);
            long hubNum=0;
            for(int i=0;i<ww.nodeNum;i++)
            {
                hubNum+=HubLabel[i].size();
            }
            out.println(hubNum);
            for(int i=0;i<ww.nodeNum;i++)
            {

                out.println(i+"   "+HubLabel[i].size());
                for(int j=0;j<HubLabel[i].size();j++)
                {
                    out.println(HubLabel[i].get(j).v+"  "+HubLabel[i].get(j).hop+"  "+HubLabel[i].get(j).pre);
                }


            }


            out.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void generatePLL(WeightedGraph ww, String directory)
    {
        this.ww=ww;
        HubLabel=new ArrayList[ww.nodeNum];
        for(int i=0;i<ww.nodeNum;i++)
        {
            HubLabel[i]=new ArrayList<>(0);
        }
        originMap=new int[ww.nodeNum];
        changedMap=new int[ww.nodeNum];
        order=new DescendingOrder();
        order.degreeOrder(this);

        visit=new boolean[ww.nodeNum];
        visitStored=new ArrayList<>(0);

        dist=new int[ww.nodeNum];
        pre=new int[ww.nodeNum];
        dynamicHop=new int[ww.nodeNum];
        changedHopNodes=new ArrayList<>();
        for(int i=0;i<ww.nodeNum;i++)
        {
            dist[i]=INF;
            pre[i]=-1;
            dynamicHop[i]=INF;
        }
        for(int i=0;i<ww.nodeNum;i++)
        {
            pruneBfs(i);
            System.out.println(i);
        }
        for(int i=0;i<ww.nodeNum;i++)
        {
            Collections.sort(HubLabel[i],hubComparator);
        }
        writeOut(directory);
    }

}
