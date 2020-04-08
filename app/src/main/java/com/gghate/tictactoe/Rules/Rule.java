package com.gghate.tictactoe.Rules;

import android.util.Log;

import java.util.Arrays;

public class Rule {

    public  static final String[] winingPaths={"123","456","789","147","258","369","159","357"};

    public static boolean winCheck(String path)
    {
        boolean result=false;
        for(int i=0;i<winingPaths.length;i++)
        {
            if(path.contains(winingPaths[i]))
            {result=true;
            break;}
        }
        return result;
    }
    public static boolean bruteForceCheck(String path)
    {
        for(int i=0;i<winingPaths.length;i++)
        {int count=0;
            for(int j=0;j<winingPaths[i].length();j++)
            {

                for(int k=0;k<path.length();k++)
                {
                    if(winingPaths[i].charAt(j)==path.charAt(k))
                    {
                        count=count+1;
                        Log.d("Count check"+winingPaths[i],"path : "+path+" Count : "+count);

                    }
                    if(count==3)
                        return true;
                }
            }
        }
        return false;
    }
    public static String sortString(String inputString)
    {
        // convert input string to char array
        char tempArray[] = inputString.toCharArray();

        // sort tempArray
        Arrays.sort(tempArray);

        // return new sorted string
        return new String(tempArray);
    }
}
