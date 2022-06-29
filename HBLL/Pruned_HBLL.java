package HBLL;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Pruned_HBLL {
    public int nodeNum;
    public ArrayList<ArrayList<Edge>> graph;
    int []originMap;
    int []changedMap;
    public HubList[] label;
    public int layer=0;
    double[] dist;
    int[] hubSize;
    int[] hubTime;
    List<Integer> changedDistList;
    int[] frntrLocation;
    List<Ehop> hubq;
    List<Ehop> frontier;
    List<Hub.InfoItem>[] dynamicQuery;
    boolean dynamicEmpty;
    long runTime;
    long labelSize;

    static final double INF=0x3f3f3f3f;
    DescendingOrder order;

    public void writeOut(String directoryPath, String weightingScheme) throws IOException
    {
        File labelFile=new File(directoryPath+weightingScheme+"HBLL.txt");
        labelFile.createNewFile();
        PrintWriter inputL=new PrintWriter(labelFile);
        inputL.println(nodeNum);
        int vNum=0,labelNum=0;
        for(HubList hl:label)
        {
            vNum+=hl.list.size();
            for(Hub hub:hl.list)
            {
                labelNum+=hub.info.size();
            }
        }
        inputL.println(vNum);
        inputL.println(labelNum);
        for(int i=0;i<label.length;i++)
        {
            HubList hl=label[i];
            inputL.println(i+"\t"+hl.list.size());
            for(int j=0;j<hl.size();j++)
            {
                Hub hub=hl.list.get(j);
                inputL.println(hub.v+"\t"+hub.info.size());
                for(Hub.InfoItem infoitem:hub.info)
                {
                    inputL.println(infoitem.hop+"\t"+infoitem.pred+"\t"+infoitem.dis);
                }
            }
        }
        inputL.close();
    }

    public void pruneBFS(int i,int hopLimit)//i is the order of vertex
    {
        int count=0;
        if(label[originMap[i]].size()==0)
        {
            dynamicEmpty=true;
        }
        else
        {
            dynamicEmpty=false;
            for(int j=0;j<label[originMap[i]].list.size();j++)
            {
                dynamicQuery[label[originMap[i]].list.get(j).v]=label[originMap[i]].list.get(j).info;
            }
        }

        dist[originMap[i]]=0.0;
        changedDistList.add(originMap[i]);
        hubq.add(new Ehop(originMap[i],0,0,originMap[i]));
        layer=0;
        while(!hubq.isEmpty()||!frontier.isEmpty())
        {
            if(hubq.isEmpty())
            {
                //use dist to store the distance under the layer so that it can be used to be compared to the distance in the new visit
                for(Ehop ehop:frontier)
                {
                    //frntrlocation back to -1
                    frntrLocation[ehop.v]=-1;

                    //first change dist, use list to record changed vertex
                    if(dist[ehop.v]==INF)
                    {
                        changedDistList.add(ehop.v);
                    }
                    dist[ehop.v]=ehop.dis;
                }
                hubq.clear();
                hubq=frontier;
                frontier=new ArrayList<>();
                layer++;
                if(layer==hopLimit)
                {
                    while(!hubq.isEmpty())
                    {
                        Ehop h=hubq.remove(0);
                        if(h.dis+1e-6<=getDist_dynamic(h.v,h.hop))
                        {
                            //since distances need not to be compared, dist need not to be changed
                            label[h.v].add(originMap[i], h.hop, h.dis, h.pred);
                            count++;
                        }
                    }
                }
                continue;
            }
            Ehop h=hubq.remove(0);
            if(h.dis+1e-6<=getDist_dynamic(h.v,h.hop))
            {
                label[h.v].add(originMap[i],h.hop,h.dis,h.pred);
                count++;
                for(Edge e:graph.get(h.v))
                {
                    int w=e.v;
                    //w has not been visited, dis equals to the past dis
                    if(frntrLocation[w]==-1)
                    {
                        if(dist[w]>=dist[h.v]+e.weight+1e-6)
                        {
                            //record w's location in frontier
                            frntrLocation[w]=frontier.size();

                            //add Ehop to frontier; use Ehop to record new dist
                            frontier.add(new Ehop(w,h.hop+1,dist[h.v]+e.weight,h.v));
                        }
                    }
                    else
                    {
                        if(frontier.get(frntrLocation[w]).dis>=dist[h.v]+e.weight+1e-6)
                        {
                            frontier.get(frntrLocation[w]).changeData(dist[h.v]+e.weight,h.v);
                        }
                    }
                }
            }
        }
        hubSize[i]=count;

        for(int j=0;j<changedDistList.size();j++)
        {
            dist[changedDistList.get(j)]=INF;
        }
        changedDistList.clear();

        if(!dynamicEmpty)
        {
            for(int j=0;j<label[originMap[i]].list.size();j++)
            {
                dynamicQuery[label[originMap[i]].list.get(j).v]=null;
            }
        }



    }

    public double getDist_dynamic(int v,int hop)
    {
        if(dynamicEmpty)
        {
            return INF;
        }
        HubList h=label[v];
        if(h.size()==0)
        {
            return INF;
        }
        int iter=0;
        double dist=INF;
        while(iter!=h.size())
        {
            if(dynamicQuery[h.list.get(iter).v]==null)
            {
                iter++;
                continue;
            }
            int hopiter1=0; int hopiter2=dynamicQuery[h.list.get(iter).v].size()-1;
            while(hopiter1!=h.list.get(iter).info.size()&&hopiter2!=-1)
            {
                if(h.list.get(iter).info.get(hopiter1).hop+dynamicQuery[h.list.get(iter).v].get(hopiter2).hop<=hop)
                {
                    double dist_=h.list.get(iter).info.get(hopiter1).dis+dynamicQuery[h.list.get(iter).v].get(hopiter2).dis;
                    if(dist_+1e-6<=dist)
                    {
                        dist=dist_;
                    }
                    hopiter1++;
                }
                else
                {
                    hopiter2--;
                }
            }
            iter++;
        }

        return dist;
    }



    public void generateHBLL(WeightedGraph ww, int hopLimit, String directoryPath)
    {
        long startTime= System.currentTimeMillis();
        graph=ww.graph;
        nodeNum=ww.nodeNum;
        originMap=new int[nodeNum];
        changedMap=new int[nodeNum];
        //create descending order
        order=new DescendingOrder();
        order.degreeOrder(this);
        label=new HubList[nodeNum];
        for(int i=0;i<nodeNum;i++)
        {
            HubList hl=new HubList();
            label[i]=hl;
        }
        dist=new double[nodeNum];
        frntrLocation=new int[nodeNum];
        for(int i=0;i<nodeNum;i++)
        {
            dist[i]=INF;
            frntrLocation[i]=-1;
        }
        hubq=new ArrayList<Ehop>();
        frontier=new ArrayList<>();
        changedDistList=new ArrayList<Integer>();
        dynamicQuery=new ArrayList[nodeNum];
        int breakcount=100;
        hubSize=new int[nodeNum];
        hubTime=new int[nodeNum/breakcount+1];
        System.out.println("there is "+nodeNum+" node to pruneBFS");
        long pruneTime= System.currentTimeMillis();
        for(int i=0;i<nodeNum;i++)
        {

            pruneBFS(i,hopLimit);

            if(i<breakcount-1){
                System.out.println(i+"---------"+hubSize[i]);
            }



            if(i%breakcount==breakcount-1||i==nodeNum-1)
            {
                long temp=pruneTime;
                pruneTime= System.currentTimeMillis();
                hubTime[i/breakcount]=((int)(pruneTime-temp));
                int tempi=0,bound=(i/breakcount)*breakcount;
                for(int j=i;j>=bound;j--)
                {
                    tempi+=hubSize[j];
                }
                System.out.println("--------"+(i+1)+" node pruned--------"+(pruneTime-temp)+"ms"+"---------"+tempi);
            }
        }

        for(int i=0;i<nodeNum;i++) {
            label[i].sort();
        }


        long endTime= System.currentTimeMillis();

        runTime=endTime-startTime;
        System.out.println("time is "+runTime+"ms");
        labelSize=labelsize();
        System.out.println("space is "+labelSize);
        System.out.println("HBLL finished");


    }


    public long labelsize()
    {
        long s=0;
        for(int i=0;i<nodeNum;i++)
        {
            s+=hubSize[i];
        }
        return s;
    }



}
