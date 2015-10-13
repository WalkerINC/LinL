package com.LingualeoExport;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class InputReader {

    public static Logger log = Logger.getLogger(InputReader.class.getName());
    StringBuilder stringBuilder = new StringBuilder();
    public static Map<String, Integer> wordsMap = new HashMap<String, Integer>();

    private boolean putWordsIntoMap(String s){
        s = s.toLowerCase();
        if(wordsMap.containsKey(s)){
            wordsMap.put(s, wordsMap.get(s)+1);
            return true;
        }else{
            wordsMap.put(s, 1);
            return false;
        }
    }

    protected void readInput(InputStream inputStream, String encode) throws IOException{
        //TODO возможность выбирать кодировку.
        Reader inputReader = new InputStreamReader(inputStream, encode);
        int temp = inputReader.read();
        while(temp != -1){
            if(correctSymbol(Character.toLowerCase((char)temp))){
                stringBuilder.append((char)temp);
            }else{
                if (stringBuilder.length()!=0) {
                    log.info(stringBuilder.toString());
                    putWordsIntoMap(stringBuilder.toString());
                }
                stringBuilder.delete(0, stringBuilder.length());
            }
            temp = inputReader.read();
        }
    }

    //Условие допустимости символа. Соответствие словарю.
    private boolean correctSymbol(char c){
        if(c>='a' && c<='z'){
            return true;
        }
        return false;
    }

    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }
}
