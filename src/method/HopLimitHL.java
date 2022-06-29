package method;

import HBLL.DistOriginPair;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class HopLimitHL {
    public int[] vIndicator;
    public int[] vOfLabel;
    public int[] hubIndicator;
    public int[] hopOfLabel;
    public int[] preOfLabel;
    public double[] disOfLabel;
    static final int INF=0x3f3f3f3f;
    public int nodeNum;
    public int vStoredNum;
    public int hubStoredNum;

    public boolean containsHL=false;

    public int[] HL_vIndicator;
    public int[] HL_vOfLabel;
    public int[] HL_hopOfLabel;
    public int[] HL_preOfLabel;
    public int HL_hubStoredNum;


    public void readLabels(String directoryPath, String weightingScheme) throws IOException
    {
        File fileL=new File(directoryPath+weightingScheme+"HBLL.txt");
        Scanner inputL=new Scanner(fileL);
        nodeNum=inputL.nextInt();
        int vNum=inputL.nextInt();
        int labelNum=inputL.nextInt();
        vIndicator=new int[nodeNum];
        vOfLabel=new int[vNum];
        hubIndicator=new int[vNum];
        hopOfLabel=new int[labelNum];
        preOfLabel=new int[labelNum];
        disOfLabel=new double[labelNum];
        vStoredNum=0;
        hubStoredNum=0;
        while(inputL.hasNext())
        {
            int v_0=inputL.nextInt();
            int num=inputL.nextInt();
            vIndicator[v_0]=vStoredNum;
            for(int i=0;i<num;i++)
            {
                int v=inputL.nextInt();
                int hNum=inputL.nextInt();
                vOfLabel[vStoredNum+i]=v;
                hubIndicator[vStoredNum+i]=hubStoredNum;
                for(int j=0;j<hNum;j++)
                {
                    hopOfLabel[hubStoredNum+j]=inputL.nextInt();
                    preOfLabel[hubStoredNum+j]=inputL.nextInt();
                    disOfLabel[hubStoredNum+j]=inputL.nextDouble();
                }
                hubStoredNum+=hNum;
            }
            vStoredNum+=num;
        }
        inputL.close();
        System.out.println("label read finish");
    }

    public void readHLLabels(String directory) throws IOException
    {
        containsHL=true;

        File f=new File(directory+"PLLlabel.txt");
        Scanner input=new Scanner(f);
        int node=input.nextInt();
        int num=input.nextInt();
        HL_vIndicator=new int[node];
        HL_hubStoredNum=num;
        HL_vOfLabel=new int[num];
        HL_hopOfLabel=new int[num];
        HL_preOfLabel=new int[num];

        num=0;
        while(input.hasNext())
        {
            int u=input.nextInt();
            HL_vIndicator[u]=num;
            int uNum=input.nextInt();
            for(int i=0;i<uNum;i++)
            {
                HL_vOfLabel[num]=input.nextInt();
                HL_hopOfLabel[num]=input.nextInt();
                HL_preOfLabel[num]=input.nextInt();
                num++;
            }
        }
        input.close();
        System.out.println("read HLlabel finish");
    }

    public double getDist(int u,int v,int hop)
    {
        int uStart=vIndicator[u],vStart=vIndicator[v],uEnd,vEnd;
        if(u==vIndicator.length-1)  { uEnd=vStoredNum; }
        else { uEnd=vIndicator[u+1]; }
        if(v==vIndicator.length-1) { vEnd=vStoredNum; }
        else { vEnd=vIndicator[v+1]; }

        double dist=INF;
        int uIter=uStart,vIter=vStart;
        while(uIter<uEnd&&vIter<vEnd)
        {
            if(vOfLabel[uIter]==vOfLabel[vIter])
            {
                int uHStart=hubIndicator[uIter],vHStart=hubIndicator[vIter];
                int uHEnd,vHEnd;
                if(uIter==hubIndicator.length-1) {uHEnd=hubStoredNum;}
                else {uHEnd=hubIndicator[uIter+1];}
                if(vIter==hubIndicator.length-1) {vHEnd=hubStoredNum;}
                else {vHEnd=hubIndicator[vIter+1];}
                int uHIter=uHStart,vHIter=vHEnd-1;
                while(uHIter<uHEnd&&hopOfLabel[uHIter]<=hop&&vHIter>=vHStart)
                {
                    if(hopOfLabel[uHIter]+hopOfLabel[vHIter]<=hop)
                    {
                        if(dist>=disOfLabel[uHIter]+disOfLabel[vHIter]+1e-6)
                        {
                            dist=disOfLabel[uHIter]+disOfLabel[vHIter];
                        }
                        uHIter++;
                    }
                    else
                    {
                        vHIter--;
                    }
                }
                uIter++;vIter++;
            }
            else if(vOfLabel[uIter]<vOfLabel[vIter])
            {
                uIter++;
            }
            else
            {
                vIter++;
            }
        }


        return dist;
    }

    public int getHop(int u,int v)
    {
        if(!containsHL)
        {
            int uStart=vIndicator[u],vStart=vIndicator[v],uEnd,vEnd;
            if(u==vIndicator.length-1)  { uEnd=vOfLabel.length; }
            else { uEnd=vIndicator[u+1]; }
            if(v==vIndicator.length-1) { vEnd=vOfLabel.length; }
            else { vEnd=vIndicator[v+1]; }

            int hop=INF;
            int uIter=uStart,vIter=vStart;
            while(uIter<uEnd&&vIter<vEnd)
            {
                if(vOfLabel[uIter]==vOfLabel[vIter])
                {
                    if(hop>hopOfLabel[hubIndicator[uIter]]+hopOfLabel[hubIndicator[vIter]])
                    {
                        hop=hopOfLabel[hubIndicator[uIter]]+hopOfLabel[hubIndicator[vIter]];
                    }
                    uIter++;vIter++;
                }
                else if(vOfLabel[uIter]<vOfLabel[vIter])
                {
                    uIter++;
                }
                else
                {
                    vIter++;
                }
            }

            return hop;
        }
        else
        {
            int uStart=HL_vIndicator[u],vStart=HL_vIndicator[v],uEnd,vEnd;
            if(u==nodeNum-1){uEnd=HL_hubStoredNum;}
            else{uEnd=HL_vIndicator[u+1];}
            if(v==nodeNum-1){vEnd=HL_hubStoredNum;}
            else{vEnd=HL_vIndicator[v+1];}

            int hop=INF;
            int uIter=uStart,vIter=vStart;
            while(uIter<uEnd&&vIter<vEnd)
            {
                if(HL_vOfLabel[uIter]==HL_vOfLabel[vIter])
                {
                    if(hop>HL_hopOfLabel[uIter]+HL_hopOfLabel[vIter])
                    {
                        hop=HL_hopOfLabel[uIter]+HL_hopOfLabel[vIter];
                    }
                    uIter++;vIter++;
                }
                else if(HL_vOfLabel[uIter]<HL_vOfLabel[vIter])
                {
                    uIter++;
                }
                else
                {
                    vIter++;
                }
            }
            return hop;
        }
    }

    public int getHop(int u, KwdHL kwhl)
    {
        if(!containsHL) {
            int vStart, vEnd;
            vStart = vIndicator[u];
            if (u == nodeNum - 1) {
                vEnd = vStoredNum;
            } else {
                vEnd = vIndicator[u + 1];
            }
            int hop = INF;
            for (int i = vStart; i < vEnd; i++) {
                int v = vOfLabel[i];
                if (kwhl.list[v] == null || kwhl.list[v].size() == 0) {
                    continue;
                }
                int hop1 = hopOfLabel[hubIndicator[i]];
                int hop2 = kwhl.list[v].get(0).hop;
                if (hop > hop1 + hop2) {
                    hop = hop1 + hop2;
                }
            }
            return hop;
        }
        else
        {
            int vStart, vEnd;
            vStart = HL_vIndicator[u];
            if (u == nodeNum - 1) {
                vEnd = HL_hubStoredNum;
            } else {
                vEnd = HL_vIndicator[u + 1];
            }
            int hop = INF;
            for (int i = vStart; i < vEnd; i++) {
                int v = HL_vOfLabel[i];
                if (!kwhl.HL_store[v]) {
                    continue;
                }
                int hop1 = HL_hopOfLabel[i];
                int hop2 = kwhl.HL_hop[v];
                if (hop > hop1 + hop2) {
                    hop = hop1 + hop2;
                }
            }
            return hop;
        }
    }

    public DistOriginPair getDist(int u, int hop, KwdHL kwhl)
    {
        int vStart,vEnd;
        vStart=vIndicator[u];
        if(u==nodeNum-1){vEnd=vStoredNum;}
        else{vEnd=vIndicator[u+1];}
        double dis=INF;
        int origin=-1;
        for(int i=vStart;i<vEnd;i++)
        {
            int v=vOfLabel[i];
            if(kwhl.list[v]==null||kwhl.list[v].size()==0)
            {
                continue;
            }
            int hStart,hEnd;
            hStart=hubIndicator[i];
            if(i==vStoredNum-1){hEnd=hubStoredNum;}
            else {hEnd=hubIndicator[i+1];}
            if(kwhl.list[v].get(0).hop+hopOfLabel[hStart]>hop)
            {
                continue;
            }
            int hIterator=hEnd-1,hopIterator=0;
            while(hopIterator<kwhl.list[v].size()&&kwhl.list[v].get(hopIterator).hop<=hop&&hIterator>=hStart)
            {
                if(kwhl.list[v].get(hopIterator).hop+hopOfLabel[hIterator]<=hop)
                {
                    double dis_=kwhl.list[v].get(hopIterator).dis+disOfLabel[hIterator];
                    if(dis_+1e-6<=dis)
                    {
                        dis=dis_;
                        origin=kwhl.list[v].get(hopIterator).origin;
                    }
                    hopIterator++;
                }
                else
                {
                    hIterator--;
                }
            }
        }
        return new DistOriginPair(dis,origin);
    }

    class getHopBFSItem
    {
        int v;
        int hop;
        public getHopBFSItem(int v,int hop)
        {
            this.v=v;
            this.hop=hop;
        }
    }
}
