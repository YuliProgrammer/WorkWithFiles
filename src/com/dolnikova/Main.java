package com.dolnikova;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String userDir = System.getProperty("user.dir");
        File file = new File(userDir);
        System.out.println(file.getAbsolutePath());

        ArrayList<File> files = new ArrayList<>();
        listOfAllFiles(userDir, files);

        filesNameToString(files);
        System.out.println("___________________________");

        String src = "D:\\myProgramming\\YuliaProject\\JAVA\\srcJdk";
        ArrayList<File> javaFiles = new ArrayList<>();
        countFilesName(javaFiles, src);
        System.out.println("___________________________");

        int numOfFunctionalIn;
        ArrayList<File> javaFilesWithFunctionalIn = new ArrayList<>();
        numOfFunctionalIn = searchInFile(javaFiles, javaFilesWithFunctionalIn);
        System.out.println("count of java files with \" @FunctionalInterface \" files = " + numOfFunctionalIn);
        System.out.println();
        filesNameToString(javaFilesWithFunctionalIn);
        System.out.println("___________________________");

        String fromDir = "C:\\Program Files\\Java\\jdk1.8.0_172\\src.zip";
        String toDir = "D:\\myProgramming\\YuliaProject\\JAVA\\Задание 17";
        copyFileToDirectory(fromDir, toDir);

        String zipFile = "D:\\myProgramming\\YuliaProject\\JAVA\\Задание 17\\src.zip";
        String toDir2 = "D:\\myProgramming\\YuliaProject\\JAVA\\Задание 17\\src";
        unzipFile(zipFile, toDir2);

        moveFileToDirectory("C:\\Прога_Шк", "D:\\Прога_Шк");
    }

    // 1 - получить список всех файлов папки проекта ( рекурсивно )
    public static void listOfAllFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listOfAllFiles(file.getAbsolutePath(), files);
            }
        }
    }

    // 2 - Подсчитать сколько файлов с расширением java внутри файла
    public static void countFilesName(ArrayList<File> javaFiles, String src) {

        ArrayList<File> files = new ArrayList<>();
        listOfAllFiles(src, files);
        int count = 0;

        for (File file : files) {
            if (file.getName().contains(".java")) {
                count++;
                javaFiles.add(file);
            }
        }
        System.out.println("count of java files = " + count);
    }

    // 2.2* Подсчитать сколько Java файлов содержат внутри строку @FunctionalInterface и вывести их имена.
    public static int searchInFile(ArrayList<File> javaFiles, ArrayList<File> javaFilesWithFunctionalIn) {

        int num = 0;
        for (File file : javaFiles) {
            try {

                FileReader fileIn = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileIn);
                String line;
                while ((line = reader.readLine()) != null) {
                    if ((line.contains("@FunctionalInterface"))) {
                        num++;
                        javaFilesWithFunctionalIn.add(file);
                        continue;
                    }
                }

            } catch (Exception e) {
                System.out.println(" Error: " + e);
            }
        }

        return num;
    }


    // 2.3* переместить файл src.zip и распаковать программно

    // создать папку по имени
    public static void madeDir(String name){
        File dir = new File(name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // переместить файл в папку
    public static void moveFileToDirectory(String from, String to) {

        copyDirectoryToDirectory(from, to);

        try {
            File dir = new File(from);
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // скопировать файл в папку
    public static void copyFileToDirectory(String from, String to) {

        madeDir(to);

        try {
            File fromDir = new File(from);
            File toDir = new File(to);
            FileUtils.copyFileToDirectory(fromDir, toDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // скопировать папку в папку
    public static void copyDirectoryToDirectory(String from, String to) {

        madeDir(to);

        try {
            File fromDir = new File(from);
            File toDir = new File(to);
            FileUtils.copyDirectoryToDirectory(fromDir, toDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // разархивировать zip-файл
    public static void unzipFile(String zipPath, String toDirectory) {

        madeDir(toDirectory);

        FileInputStream fileInputStream;
        byte[] buffer = new byte[1024];

        try {
            fileInputStream = new FileInputStream(zipPath);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(toDirectory + File.separator + fileName);

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }

                fileOutputStream.close();
                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
            fileInputStream.close();

            System.out.println("Unzip completed.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void filesNameToString(ArrayList<File> files) {
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).getName());
        }
    }

}
