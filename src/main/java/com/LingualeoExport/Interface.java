package com.LingualeoExport;

import java.io.*;
import java.util.logging.Logger;

public class Interface extends InputReader {
    public static Logger log = Logger.getLogger(Interface.class.getName());

    Interface(String path, String encode){
        readFile(path, encode);
        // TODO допилить ввод имейло-пароля
        Reciever reciever = new Reciever("holypics3@gmail.com", "LoveJava", getWordsMap());
    }

    public void readFile(String path, String encode){
        try{
            InputStream is = new FileInputStream(path);
            readInput(is, encode);
        }catch (FileNotFoundException e){
            log.info("DEBUG: Fail. File not found exception.");
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            log.info("DEBUG: Fail. Unsupported encoding exception.");
            e.printStackTrace();
        }catch (IOException e){
            log.info("DEBUG: Fail. Something gone wrong... :(");
            e.printStackTrace();
        }
    }
}
