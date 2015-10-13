package com.LingualeoExport;

import java.util.Scanner;

/**
 * Created by Никита on 06.09.15.
 */
public class Main{
    public static void main(String[] args) {
        System.out.println("Hello. This is pre-alpha test, so it's contain some bugs and kostil's.");
        System.out.println("Укажите путь к файлу в формате: C:/YourFileName.srt");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        System.out.println("Введите кодировку файла:");
        String encode = sc.nextLine();
        new Interface(path, encode);
    }
}
