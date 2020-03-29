package index;

import common.DocInfo;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index {
    public static class Weight implements Comparable<Weight>{
        public String word;
        public int docId;
        public int weight;

        public int compareTo(Weight o) {
            if (o.weight > this.weight) {
                return 1;
            } else if (o.weight < this.weight) {
                return -1;
            } else {
                return 0;
            }
        }
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
        System.out.println("build start");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputPath)));
        String line ="";
        while ((line = bufferedReader.readLine()) != null) {
            DocInfo docInfo = buildForward(line);
            buildInverted(docInfo);
        }
        System.out.println("build finish");
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
        List<Term> titleTerms = ToAnalysis.parse(docInfo.getTitle()).getTerms();
        for (Term term : titleTerms) {
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);
            if (wordCnt == null) {
                wordCntHashMap.put(word, new WordCnt(1, 0));
            } else {
                wordCnt.titleCount++;
            }
        }
        List<Term> contentTerms = ToAnalysis.parse(docInfo.getContent()).getTerms();
        for (Term term : contentTerms) {
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);
            if (wordCnt == null) {
                wordCntHashMap.put(word, new WordCnt(0, 1));
            } else {
                wordCnt.contentCount++;
            }
        }
        for (HashMap.Entry<String, WordCnt> entry : wordCntHashMap.entrySet()) {
            Weight weight = new Weight();
            weight.word = entry.getKey();
            weight.docId = docInfo.getDocId();
            WordCnt wordCnt = entry.getValue();
            weight.weight = wordCnt.titleCount * 10 + wordCnt.contentCount;
            ArrayList<Weight> invertedList = invertedIndex.get(entry.getKey());
            if (invertedList == null) {
                invertedList = new ArrayList<Weight>();
                invertedIndex.put(entry.getKey(), invertedList);
            }
            invertedList.add(weight);
        }
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

    public static void main(String[] args) throws IOException {
        Index index = new Index();
        index.build("D:\\raw_data.txt");
        List<Index.Weight> invertedList = index.getInverted("arraylist");
        for (Index.Weight weight : invertedList) {
            System.out.println(weight.docId);
            System.out.println(weight.word);
            System.out.println(weight.weight);

            DocInfo docInfo = index.getDocInfo(weight.docId);
            System.out.println(docInfo.getTitle());
            System.out.println("==================");
        }
    }
}
