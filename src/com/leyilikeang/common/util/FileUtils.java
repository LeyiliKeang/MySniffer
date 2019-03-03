package com.leyilikeang.common.util;

import com.leyilikeang.common.example.sqlite.SelectRecords;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

import java.io.*;
import java.util.*;

/**
 * @author likang
 * @date 2019/2/27 20:57
 */
public class FileUtils {

    public static String tempFile = "files/temp.cap";

    public static String openFile;

    public ArrayList<String> readRecent() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("files/recent.txt")));
        String path;
        ArrayList<String> arrayList = new ArrayList<String>();
        while ((path = bufferedReader.readLine()) != null) {
            arrayList.add(path);
        }
        bufferedReader.close();
        return arrayList;
    }

    public void writeRecent(String path, boolean clearRecent) throws IOException {
        BufferedWriter bufferedWriter;
        if (clearRecent) {
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("files/recent.txt", false)));
            bufferedWriter.close();
            return;
        }
        ArrayList<String> arrayList = readRecent();
        if (arrayList.contains(path)) {
            arrayList.remove(path);
            arrayList.add(path);
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("files/recent.txt", false)));
            for (String str : arrayList) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } else {
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("files/recent.txt", true)));
            bufferedWriter.write(path);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
    }

    public void deleteRecent(String path) throws IOException {
        ArrayList<String> arrayList = readRecent();
        if (arrayList.contains(path)) {
            arrayList.remove(path);
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("files/recent.txt", false)));
            for (String str : arrayList) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
    }

    public static void main(String[] args) throws IOException {
        FileUtils fileUtils = new FileUtils();
//        fileUtils.readRecent();
        ArrayList<String> arrayList = fileUtils.readRecent();
        for (String str : arrayList) {
            System.out.println(str);
        }
        System.out.println();
        fileUtils.writeRecent("什么鬼啊1", true);
//        fileUtils.writeRecent("什么鬼啊1", true);

//        System.out.println();
//        fileUtils.writeRecent();
//        fileUtils.readRecent();
//        System.out.println();
//        fileUtils.writeRecent();
//        fileUtils.readRecent();
    }
}