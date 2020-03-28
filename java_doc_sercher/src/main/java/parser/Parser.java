package parser;

import java.io.*;
import java.util.ArrayList;

public class Parser {
    private static final String INPUT_PATH = "D:\\Java\\docs\\api";
    private static final String OUTPUT_PATH = "D:\\raw_data.txt";

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter(OUTPUT_PATH);
        ArrayList<File> fileList = new ArrayList<File>();
        enumFile(INPUT_PATH, fileList);
//        System.out.println(fileList.size());
        for (File f : fileList) {
            System.out.print("converting " + f.getAbsolutePath() + " ..");
            String line = convertLine(f);
            fileWriter.write(line);
        }
        fileWriter.close();
    }

    private static String convertLine(File f) throws IOException {
        String title = convertTitle(f);
        String url = convertUrl(f);
        String content = convertContent(f);
        return title + "\3" + url + "\3" + content + "\n";
    }

    private static String convertUrl(File f) {
//        https://docs.oracle.com/javase/8/docs/api
        String part1 = "https://docs.oracle.com/javase/8/docs/api";
        String part2 = f.getAbsolutePath().substring(INPUT_PATH.length());
        return part1 + part2;
    }

    private static String convertTitle(File f) {
        String name = f.getName();
        return name.substring(0, name.length() - 5);
    }

    private static void enumFile(String inputPath, ArrayList<File> fileList) {
        File root= new File(inputPath);
        File[] files = root.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                enumFile(f.getAbsolutePath(), fileList);
            } else if (f.getAbsolutePath().endsWith(".html")) {
                fileList.add(f);
            }
        }
    }

    private static String convertContent(File f) throws IOException {
        FileReader fileReader = new FileReader(f);
        boolean isContent = true;
        StringBuilder output = new StringBuilder();
        while (true) {
            int ret = fileReader.read();
            if (ret == -1) {
                break;
            }
            char c = (char)ret;
            if (isContent) {
                if (c == '<') {
                    isContent = false;
                    continue;
                }
                if (c == '\n' || c == '\r') {
                    c = ' ';
                }
                output.append(c);
            } else {
                if (c == '>') {
                    isContent = true;
                }
            }
        }
        return output.toString();
    }
}
