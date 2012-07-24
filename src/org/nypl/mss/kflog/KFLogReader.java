/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mss.kflog;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KFLogReader {
    private Pattern mfm = Pattern.compile("(MFM: OK.{1}|MFM: <.*>)");
    private Map<String, String> sectors = new TreeMap<String, String>();
    private Map<String, Integer> stats = new HashMap<String, Integer>();
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        KFLogReader k = new KFLogReader(args[0]);
    }

    private KFLogReader(String path) throws FileNotFoundException, IOException {
        File f = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        while((line = br.readLine()) != null){
            Matcher m = mfm.matcher(line);
            if(m.find()){
                sectors.put(line.substring(0,4), line.substring(m.start() + 5, m.end()));
            }
        }
        
        Pattern side = Pattern.compile("1$");
        for(Map.Entry e : sectors.entrySet()){
            Matcher m = side.matcher(e.getKey().toString());
            addStat(e.getValue().toString());
            if(m.find())
                System.out.print("\t\t" + e.getKey() + "\t" + e.getValue());
            else{
               if(e.getValue().toString().length() > 7)
                   System.out.print("\n" + e.getKey() + "\t" + e.getValue());
               else
                   System.out.print("\n" + e.getKey() + "\t" + e.getValue() + "\t");
            }
        }
        
        System.out.println("\n\nStats");
        for(Map.Entry e: stats.entrySet()){
            System.out.println(e.getValue() + "\t" + e.getKey());
        }
    }

    private void addStat(String stat) {
        if(stats.containsKey(stat)){
            int count = stats.get(stat);
            count++;
            stats.put(stat, count);
        } else {
            stats.put(stat, 1);
        }
    }
}
