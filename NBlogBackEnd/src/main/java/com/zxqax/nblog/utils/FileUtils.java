package com.zxqax.nblog.utils;

public final class FileUtils {
    public static String regName(String fileName){
        fileName=fileName.trim();
        //  不支持的文件|文件夹命名      / \ : * " < > | ？
        fileName=fileName.replaceAll(" ","");
        fileName=fileName.replaceAll("/","_");
        fileName=fileName.replaceAll("\\\\","_");
        fileName=fileName.replaceAll(":","_");
        fileName=fileName.replaceAll("\\*","_");
        fileName=fileName.replaceAll("\"","_");
        fileName=fileName.replaceAll("：","_");
        fileName=fileName.replaceAll("<","_");
        fileName=fileName.replaceAll(">","_");
        fileName=fileName.replaceAll("\\|","_");
        fileName=fileName.replaceAll("\\?","_");
        fileName=fileName.replaceAll("[.]","_");
        return fileName;
    }
}
