package method;

import HBLL.*;

import java.io.IOException;

public class Run {
    public static final int INF=0x3f3f3f3f;

    public static void main(String[] args)
    {
        String directory,task,weightingScheme;
        directory=args[0]+"/";
        task=args[1];
        weightingScheme=args[2];
        if(task.equals("PLL"))
        {
            PLL pll=new PLL();
            WeightedGraph ww;
            ww=new WeightedGraph();
            try {
                ww.graphReadUW(directory+"graph.txt");
                System.out.println("graph read finish");
            }catch(IOException e)
            {
                e.printStackTrace();
            }
            pll.generatePLL(ww,directory);
        }
        else if(task.equals("HBLL"))
        {
            if(weightingScheme.equals("UW"))
            {
                WeightedGraph ww=new WeightedGraph();
                if(directory.contains("Dbpedia")||directory.contains("LUBM_5M"))
                {
                    ww.createLabelUW(directory,3);
                }
                else ww.createLabelUW(directory,INF);
            }
            else if(weightingScheme.equals("IW"))
            {
                WeightedGraph ww=new WeightedGraph();
                if(directory.contains("Dbpedia")||directory.contains("LUBM_5M"))
                {
                    ww.createLabelIW(directory,3);
                }
                else ww.createLabelIW(directory,INF);
            }
            else System.out.println("weightingScheme argument error");
        }
        else if(task.equals("CBA")||task.equals("CBA+"))
        {
            if(weightingScheme.equals("UW")||weightingScheme.equals("IW"))
            {
                CBAandCBAplus.run(directory,weightingScheme,task);
            }
            else System.out.println("weightingScheme argument error");
        }
        else System.out.println("task argument error");
    }
}
