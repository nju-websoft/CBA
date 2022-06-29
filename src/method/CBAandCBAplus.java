package method;

import HBLL.*;

import java.io.*;
import java.util.*;

public class CBAandCBAplus {
    public static ArrayList<ArrayList<String>> query;
    public static ArrayList<Integer> kwList;
    public static ArrayList<ArrayList<Integer>> queryList;
    public static ArrayList<ArrayList<ArrayList<Integer>>> queryListList;

    public static void generateQueryList(int locOfQuery, WeightedGraph ww)
    {
        queryList=new ArrayList<>();
        kwList=new ArrayList<>();

        for(String kw:query.get(locOfQuery))
        {
            HashSet<Integer> temp_=new HashSet<Integer>(0);
            int kwId=ww.kwId.get(kw);
            kwList.add(kwId);
            for(Integer node:ww.kwMap.get(kwId))
            {
                temp_.add(node);
            }
            ArrayList<Integer> temp=new ArrayList<>(temp_);
            queryList.add(temp);
        }
    }

    public static void readQuery(String directory, WeightedGraph ww) throws IOException
    {
        query=new ArrayList<>();
        File file=new File(directory+"query.txt");
        BufferedReader inputK=new BufferedReader(new FileReader(file));
        String s;
        while((s=inputK.readLine())!=null)
        {
            String[] temp=s.split("\\s+");
            ArrayList<String> oneQuery=new ArrayList<String>(0);
            for(String kw:temp)
            {
                if(ww.kwId.containsKey(kw))
                {
                    oneQuery.add(kw);
                }
                else
                {
                    System.out.println(query.size()+" "+kw);

                }
            }
            query.add(oneQuery);

        }
        System.out.println("query read finish");
    }




    public static void readQueryList(String directory) throws IOException
    {
        queryListList=new ArrayList<ArrayList<ArrayList<Integer>>>(0);
        query=new ArrayList<>(0);
        File file=new File(directory+"queryList.txt");
        BufferedReader input=new BufferedReader(new FileReader(file));
        String s;
        while((s=input.readLine())!=null)
        {
            ArrayList<ArrayList<Integer>> temp1=new ArrayList<>(0);
            String[] a=s.split(",");
            for(int i=0;i<a.length;i++)
            {
                String[] b=a[i].split("\\s+");
                ArrayList<Integer> temp2=new ArrayList<>(0);
                for(int j=0;j<b.length;j++)
                {
                    temp2.add(Integer.valueOf(b[j]));
                }
                temp1.add(temp2);
            }
            queryListList.add(temp1);
            query.add(new ArrayList<>(0));
        }

    }

    static void run(String directory, String weightingScheme, String method)
    {
        WeightedGraph ww =new WeightedGraph();
        HopLimitHL hl=new HopLimitHL();
        try{
            ww.readTotal(directory,weightingScheme);
            hl.readLabels(directory,weightingScheme);
            if(directory.contains("Dbpedia")||directory.contains("LUBM-5M"))
            {
                hl.readHLLabels(directory);
            }
            if(directory.contains("LUBM"))  readQueryList(directory);
            else readQuery(directory,ww);

        }catch (IOException e)
        {
            e.printStackTrace();
        }

        File weightFile=new File(directory+weightingScheme);
        weightFile.mkdir();
        File result=new File(directory+weightingScheme+"/"+method+"result");
        result.mkdir();

        long[][] totaltime=new long[query.size()][3];
        double[][] ansWeight=new double[query.size()][3];


        for(int locOfQuery=0;locOfQuery<query.size();locOfQuery++)
        {
            for(int diameter=2;diameter<=6;diameter+=2)
            {

                File dir=new File(directory+weightingScheme+"/"+method+"result/"+locOfQuery);
                dir.mkdir();

                long startTime = System.currentTimeMillis();
                AnsTree ans=null;
                if(directory.contains("LUBM")) queryList=queryListList.get(locOfQuery);
                else generateQueryList(locOfQuery, ww);
                if (queryList.size() <= 1)
                {
                    long endTime = System.currentTimeMillis();
                    ArrayList<Integer> temp=new ArrayList<>(0);
                    temp.add(queryList.get(0).get(0));
                    ans = new AnsTree(queryList.get(0).get(0), 1, temp);
                    totaltime[locOfQuery][diameter / 2 - 1] = endTime - startTime;
                }
                else {
                    KwdHL[] kwdhl = new KwdHL[queryList.size()];
                    for (int i = 0; i < queryList.size(); i++) {
                        kwdhl[i] = new KwdHL(hl, queryList.get(i), diameter);
                    }
                    CertificateSearch search = new CertificateSearch(ww);
                    CertificateQueue CQ=null;
                    if (method.equals("CBA")) CQ = search.BFSearch(ww, hl, kwdhl, queryList, diameter);
                    else if (method.equals("CBA+"))
                        CQ = search.BestFirstSearchPri(ww, hl, kwdhl,  queryList, diameter);

                    if(CQ!=null)
                    {
                        ans = GreedyGST.greedyTreeEven(ww, CQ, hl, diameter);
                    }

                }
                long endTime = System.currentTimeMillis();
                totaltime[locOfQuery][diameter / 2 - 1] = endTime - startTime;
                if(ansWeight!=null)
                {
                    ansWeight[locOfQuery][diameter/2-1]=ans.weight;
                }else
                {
                    ansWeight[locOfQuery][diameter/2-1]=-1;
                }


                try
                {
                    if(diameter==2)
                    {
                        File queryFile = new File(directory + weightingScheme + "/" + method + "result/" + locOfQuery + "/queryList.txt");
                        queryFile.createNewFile();
                        Writer queryOut = new FileWriter(queryFile);
                        queryOut.write("[ ");
                        for (ArrayList<Integer> q : queryList) {
                            queryOut.write("[");
                            for (Integer i : q) {
                                queryOut.write(i + ", ");
                            }
                            queryOut.write("], ");
                        }
                        queryOut.write("]");
                        queryOut.flush();
                        queryOut.close();

                        System.out.println("------------------query "+locOfQuery+"------------------");

                    }

                    File f=new File(directory+weightingScheme+"/"+method+"result/"+locOfQuery+"/diameter_"+diameter+"_result.txt");
                    f.createNewFile();
                    Writer fOut=new FileWriter(f);
                    System.out.println("diameter="+diameter+"   time: "+totaltime[locOfQuery][diameter/2-1]+" ms");
                    if(ans!=null)
                    {
                        fOut.write("keyword number: "+ans.kwNum+"\n");
                        fOut.write("keyword node: ");
                        for(Integer i:ans.kwNode)
                        {
                            fOut.write(i+", ");
                        }
                        fOut.write("\n");
                        fOut.write("node: ");
                        fOut.write(ans.root+", ");
                        for(Integer terminate:ans.preNode.keySet())
                        {
                            fOut.write(terminate+", ");
                        }
                        fOut.write("\n");
                        fOut.write("edge: ");
                        for(Map.Entry<Integer, Integer> entry:ans.preNode.entrySet())
                        {
                            fOut.write("["+entry.getKey()+","+entry.getValue()+"], ");
                        }

                        fOut.write("\nweight: "+ans.weight);
                    }
                    fOut.close();
                    fOut.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        try
        {
            for(int searchId=0;searchId<5;searchId++)
            {
                File total_time=new File(directory+weightingScheme+"/"+method+"result/"+method+"time.txt");
                total_time.createNewFile();
                Writer total_out=new FileWriter(total_time);
                for(int locOfQuery=0;locOfQuery<query.size();locOfQuery++)
                {
                    total_out.write(totaltime[locOfQuery][0]+"    "+totaltime[locOfQuery][1]+"    "+totaltime[locOfQuery][2]+"\n");

                }
                total_out.flush();
                total_out.close();
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(method+" finish");
    }
}
