package index;

import common.DocInfo;
import org.ansj.domain.Term;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index {
    public static class Weight {
        public String word;
        public int docId;
        public int weight;
    }
    
    private ArrayList<DocInfo> forwardIndex = new ArrayList<DocInfo>();
    private HashMap<String, ArrayList<Weight>> invertedIndex = new HashMap<String, ArrayList<Weight>>();
    
    public DocInfo getDocInfo(int docId) {
        return forwardIndex.get(docId);
    }
    
    public ArrayList<Weight> getInverted(String term) {
        return invertedIndex.get(term);
    }
    
    public void build(String inputPath) throws IOException {
        System.out.printf("build start");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputPath)));
        String line ="";
        while ((line = bufferedReader.readLine()) != null) {
            DocInfo docInfo = buildForward(line);
            buildInverted(docInfo);
        }
        System.out.printf("build finish");
    }

    private void buildInverted(DocInfo docInfo) {
        class WordCnt {
            public int titleCount;
            public int contentCount;

            public WordCnt(int titleCount, int contentCount) {
                this.titleCount = titleCount;
                this.contentCount = contentCount;
            }
        }
        HashMap<String, WordCnt> wordCntHashMap = new HashMap<String, WordCnt>();
    }

    private DocInfo buildForward(String line) {
        String[] tokens = line.split("\3");
        if (tokens.length != 3) {
            System.out.printf("文件格式存在问题：" + line);
            return null;
        }
        DocInfo docInfo = new DocInfo();
        docInfo.setDocId(forwardIndex.size());
        docInfo.setTitle(tokens[0]);
        docInfo.setUrl(tokens[1]);
        docInfo.setContent(tokens[2]);
        forwardIndex.add(docInfo);
        return docInfo;
    }
}
