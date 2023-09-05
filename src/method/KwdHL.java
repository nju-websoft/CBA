package method;


import java.util.ArrayList;

public class KwdHL {
    public class InfoItem {
        public int hop;
        public double dis;
        public int origin;

        public InfoItem(int hop, double dis, int origin) {
            this.hop = hop;
            this.dis = dis;
            this.origin = origin;
        }
    }

    public ArrayList<InfoItem>[] list;
    public int diambound;
    public int[] HL_hop;
    public int[] HL_origin;
    public boolean[] HL_store;
    public static int INF = 0x3f3f3f3f;

    public KwdHL(HopLimitHL hl, ArrayList<Integer> query, int diambound) {
        this.diambound = diambound;
        list = new ArrayList[hl.nodeNum];
        for (Integer u : query) {
            int vStart, vEnd;
            vStart = hl.vIndicator[u];
            if (u == hl.nodeNum - 1) {
                vEnd = hl.vStoredNum;
            }
            else {
                vEnd = hl.vIndicator[u + 1];
            }
            for (int i = vStart; i < vEnd; i++) {
                int v = hl.vOfLabel[i];
                if (list[v] == null) {
                    list[v] = new ArrayList<InfoItem>(0);
                    int hStart, hEnd;
                    hStart = hl.hubIndicator[i];
                    if (i == hl.vStoredNum - 1) {
                        hEnd = hl.hubStoredNum;
                    }
                    else {
                        hEnd = hl.hubIndicator[i + 1];
                    }
                    for (int j = hStart; j < hEnd; j++) {
                        int hop = hl.hopOfLabel[j];
                        if (hop > diambound) {
                            break;
                        }
                        list[v].add(new InfoItem(hop, hl.disOfLabel[j], u));
                    }
                }
                else {
                    ArrayList<InfoItem> temp = new ArrayList<>(0);
                    int hStart, hEnd;
                    hStart = hl.hubIndicator[i];
                    if (i == hl.vStoredNum - 1) {
                        hEnd = hl.hubStoredNum;
                    }
                    else {
                        hEnd = hl.hubIndicator[i + 1];
                    }
                    int hopiter1, hopiter2;
                    hopiter1 = 0;
                    hopiter2 = hStart;
                    double minDis = INF;
                    while (hopiter1 < list[v].size() && hopiter2 < hEnd) {
                        int hop2 = hl.hopOfLabel[hopiter2];
                        if (hop2 > diambound) {
                            break;
                        }
                        int hop1 = list[v].get(hopiter1).hop;
                        if (hop1 == hop2) {
                            double dis1 = list[v].get(hopiter1).dis;
                            double dis2 = hl.disOfLabel[hopiter2];
                            if (dis1 <= dis2) {
                                if (dis1 < minDis) {
                                    temp.add(list[v].get(hopiter1));
                                    minDis = dis1;
                                }
                            }
                            else {
                                if (dis2 < minDis) {
                                    temp.add(new InfoItem(hop2, dis2, u));
                                    minDis = dis2;
                                }
                            }
                            hopiter1++;
                            hopiter2++;
                        }
                        else if (hop1 < hop2) {
                            double dis1 = list[v].get(hopiter1).dis;
                            if (dis1 < minDis) {
                                temp.add(list[v].get(hopiter1));
                                minDis = dis1;
                            }
                            hopiter1++;
                        }
                        else {
                            double dis2 = hl.disOfLabel[hopiter2];
                            if (dis2 <= minDis) {
                                temp.add(new InfoItem(hop2, dis2, u));
                                minDis = dis2;
                            }
                            hopiter2++;
                        }
                    }//while end
                    if (hopiter1 < list[v].size()) {
                        while (hopiter1 < list[v].size()) {
                            double dis1 = list[v].get(hopiter1).dis;
                            if (dis1 < minDis) {
                                temp.add(list[v].get(hopiter1));
                                minDis = dis1;
                            }
                            hopiter1++;
                        }
                    }
                    else if (hopiter2 < hEnd) {
                        while (hopiter2 < hEnd) {
                            int hop2 = hl.hopOfLabel[hopiter2];
                            double dis2 = hl.disOfLabel[hopiter2];
                            if (dis2 < minDis) {
                                temp.add(new InfoItem(hop2, dis2, u));
                                minDis = dis2;
                            }
                            hopiter2++;
                        }
                    }
                    list[v] = temp;
                }
            }
        }

        if (hl.containsHL) {
            HL_hop = new int[hl.nodeNum];
            HL_origin = new int[hl.nodeNum];
            HL_store = new boolean[hl.nodeNum];

            for (Integer u : query) {
                int uStart = hl.HL_vIndicator[u], uEnd;
                if (u == hl.nodeNum - 1) {
                    uEnd = hl.HL_hubStoredNum;
                }
                else {
                    uEnd = hl.HL_vIndicator[u + 1];
                }
                for (int i = uStart; i < uEnd; i++) {
                    int v = hl.HL_vOfLabel[i];
                    if (!HL_store[v]) {
                        HL_store[v] = true;
                        HL_hop[v] = hl.HL_hopOfLabel[i];
                        HL_origin[v] = u;
                    }
                    else {
                        if (HL_hop[v] > hl.HL_hopOfLabel[i]) {
                            HL_hop[v] = hl.HL_hopOfLabel[i];
                            HL_origin[v] = u;
                        }
                    }
                }
            }
        }
    }

    public KwdHL(KwdHL[] kwdHLs, int radiusBound) {
        this.diambound = kwdHLs[0].diambound;
        int nodeNum = kwdHLs[0].list.length;
        this.list = new ArrayList[nodeNum];
        for (KwdHL kwdHL : kwdHLs) {
            for (int v = 0; v < nodeNum; v++) {
                if (kwdHL.list[v] == null) continue;
                if (list[v] == null) {
                    list[v] = new ArrayList<InfoItem>(0);
                    for (InfoItem infoItem : kwdHL.list[v]) {
                        if (infoItem.hop > radiusBound) break;
                        list[v].add(new InfoItem(infoItem.hop, infoItem.dis, infoItem.origin));
                    }
                }
                else {
                    ArrayList<InfoItem> temp = new ArrayList<>(0);
                    int hopiter1 = 0, hopiter2 = 0;
                    double minDis = INF;
                    while (hopiter1 < list[v].size() || hopiter2 < kwdHL.list[v].size()) {
                        int hop2 = INF;
                        if (hopiter2 < kwdHL.list[v].size()) {
                            hop2 = kwdHL.list[v].get(hopiter2).hop;
                        }
                        if (hop2 > radiusBound) {
                            hopiter2 = kwdHL.list[v].size();
                            hop2 = INF;
                        }
                        int hop1 = INF;
                        if (hopiter1 < list[v].size()) {
                            hop1 = list[v].get(hopiter1).hop;
                        }
                        if (hop1 == INF && hop2 == INF) {
                            break;
                        }
                        if (hop1 == hop2) {
                            double dis1 = list[v].get(hopiter1).dis;
                            double dis2 = kwdHL.list[v].get(hopiter2).dis;
                            if (dis1 <= dis2) {
                                if (dis1 < minDis) {
                                    temp.add(list[v].get(hopiter1));
                                    minDis = dis1;
                                }
                            }
                            else {
                                if (dis2 < minDis) {
                                    temp.add(new InfoItem(hop2, dis2, kwdHL.list[v].get(hopiter2).origin));
                                    minDis = dis2;
                                }
                            }
                            hopiter1++;
                            hopiter2++;
                        }
                        else if (hop1 < hop2) {
                            double dis1 = list[v].get(hopiter1).dis;
                            if (dis1 < minDis) {
                                temp.add(list[v].get(hopiter1));
                                minDis = dis1;
                            }
                            hopiter1++;
                        }
                        else {
                            double dis2 = kwdHL.list[v].get(hopiter2).dis;
                            if (dis2 < minDis) {
                                temp.add(new InfoItem(hop2, dis2, kwdHL.list[v].get(hopiter2).origin));
                                minDis = dis2;
                            }
                            hopiter2++;
                        }
                    }
                    list[v] = temp;
                }
            }
        }
    }


}
