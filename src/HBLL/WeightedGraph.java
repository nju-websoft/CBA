package HBLL;

import java.io.*;
import java.util.*;

public class WeightedGraph {
    public int nodeNum=0;
    public int kwNum=0;
    public ArrayList<ArrayList<Edge>> graph;
    public HashMap<String, Integer> nodeId;
    public ArrayList<String> nodeName;
    public ArrayList<ArrayList<Integer>> kwMap;
    public HashMap<String, Integer> kwId;
    public ArrayList<String> kwName;

    public static final int INF=0x3f3f3f3f;

    public WeightedGraph()
    {
        graph=new ArrayList<ArrayList<Edge>>();
        nodeId=new HashMap<String, Integer>();
        nodeName=new ArrayList<>();
        kwMap=new ArrayList<>();
        kwId=new HashMap<String, Integer>();
        kwName=new ArrayList<>();
    }

    public void graphReadUW(String graphfile) throws IOException
    {
        File graphFile=new File(graphfile);
        Scanner inputG=new Scanner(graphFile);

        nodeNum=inputG.nextInt();
        for(int i=0;i<nodeNum;i++)
        {
            graph.add(new ArrayList<Edge>(0));
        }

        int x,y;
        while(inputG.hasNext())
        {
            x=inputG.nextInt();
            y=inputG.nextInt();
            graph.get(x).add(new Edge(y,1.0));
            graph.get(y).add(new Edge(x,1.0));
        }
        inputG.close();
        removeDuplicateEdge();
    }

    public void graphReadIW(String graphfile) throws IOException
    {
        File graphFile=new File(graphfile);
        Scanner inputG=new Scanner(graphFile);

        nodeNum=inputG.nextInt();
        for(int i=0;i<nodeNum;i++)
        {
            graph.add(new ArrayList<Edge>(0));
        }

        int x,y;
        double z;
        while(inputG.hasNext())
        {
            x=inputG.nextInt();
            y=inputG.nextInt();
            z=inputG.nextDouble();
            graph.get(x).add(new Edge(y,z));
            graph.get(y).add(new Edge(x,z));
        }
        inputG.close();
        removeDuplicateEdge();
    }


    public void createLabelUW(String directory, int count)
    {
        try
        {
            graphReadUW(directory+"graph.txt");
            Pruned_HBLL hbll=new Pruned_HBLL();
            hbll.generateHBLL(this,count,directory);
            hbll.writeOut(directory,"UW");
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void createLabelIW(String directory, int count)
    {
        try
        {
            graphReadIW(directory+"Weightgraph.txt");
            Pruned_HBLL hbll=new Pruned_HBLL();
            hbll.generateHBLL(this,count,directory);
            hbll.writeOut(directory,"IW");
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }
    }



    public void readTotal(String directory, String weightingScheme) throws IOException
    {
        if(weightingScheme.equals("UW"))
        {
            graphReadUW(directory+"graph.txt");
        }
        else if(weightingScheme.equals("IW"))
        {
            graphReadIW(directory+"Weightgraph.txt");
        }
        System.out.println("graph read finish");
        if(!directory.contains("lubm"))
        {

            File kwFile=new File(directory+"kwName.txt");
            BufferedReader inputKw=new BufferedReader(new FileReader(kwFile));
            String kws;
            while((kws=inputKw.readLine())!=null)
            {
                if(kws.equals("")) {
                    continue;
                }
                kws=kws.replaceAll(" +"," ");
                String[] kw=kws.split(" ");
                if(kw.length<2)
                {
                    kwName.add(" ");
                    kwId.put(" ", Integer.parseInt(kw[0]));
                }else
                {
                    kwName.add(kw[1].toLowerCase());
                    kwId.put(kw[1].toLowerCase(), Integer.parseInt(kw[0]));
                }
            }
            kwNum=kwName.size();
            inputKw.close();
            System.out.println(kwNum);
            System.out.println("keyword read finish");

            File nodeFile=new File(directory+"nodeName.txt");
            BufferedReader inputN=new BufferedReader(new FileReader(nodeFile));
            String nds;
            while((nds=inputN.readLine())!=null)
            {
                nds=nds.replaceAll(" +"," ");
                String[] node=nds.split(" ");
                nodeName.add(node[1]);
                nodeId.put(node[1], Integer.parseInt(node[0]));
            }
            inputN.close();
            System.out.println(nodeName.size());
            System.out.println("node read finish");

            for(int i=0;i<kwNum;i++)
            {
                kwMap.add(new ArrayList<>());
            }

            File mapFile=new File(directory+"kwMap.txt");
            BufferedReader inputMap=new BufferedReader(new FileReader(mapFile));
            String s;
            while((s=inputMap.readLine())!=null)
            {
                String[] entity=s.split("   ");
                int loc= Integer.parseInt(entity[0]);
                for(int i=1;i<entity.length;i++)
                {
                    kwMap.get(loc).add(Integer.parseInt(entity[i]));
                }
            }
            inputMap.close();
            System.out.println("keywords Mapping read finish");


        }

    }



    public void removeDuplicateEdge()
    {
        for(int i=0;i<graph.size();i++)
        {
            HashMap<Integer, Double> edgeMap=new HashMap<>(0);
            for(Edge e:graph.get(i))
            {
                if(edgeMap.containsKey(e.v))
                {
                    double preWeight=edgeMap.get(e.v);
                    if(preWeight>=e.weight+1e-6)
                    {
                        edgeMap.replace(e.v,preWeight,e.weight);
                    }
                }
                else
                {
                    edgeMap.put(e.v,e.weight);
                }
            }
            ArrayList<Edge> temp=new ArrayList<>(0);
            for(Map.Entry<Integer, Double> entry:edgeMap.entrySet())
            {
                temp.add(new Edge(entry.getKey(),entry.getValue()));
            }
            graph.set(i,temp);
        }
    }










}
