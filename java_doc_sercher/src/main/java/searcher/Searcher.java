package searcher;

import common.DocInfo;
import index.Index;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;
import java.util.*;

public class Searcher {
    private Index index = new Index();

    public Searcher() throws IOException {
        index.build("D:\\raw_data.txt");
    }

    public List<Result> search(String query) {
        List<Term> terms = ToAnalysis.parse(query).getTerms();
        ArrayList<Index.Weight> allTokenResult = new ArrayList<Index.Weight>();
        for (Term term : terms) {
            String word = term.getName();
            List<Index.Weight> invertedList = index.getInverted(word);
            if (invertedList == null) {
                continue;
            }
            allTokenResult.addAll(invertedList);
        }
        Collections.sort(allTokenResult);
        ArrayList<Result> results = new ArrayList<>();
        for (Index.Weight weight : allTokenResult) {
            DocInfo docInfo = index.getDocInfo(weight.docId);
            Result result = new Result();
            result.setTitle(docInfo.getTitle());
            result.setClickUrl(docInfo.getUrl());
            result.setShowUrl(docInfo.getUrl());
            result.setDesc(GenDesc(docInfo.getContent(), weight.word));
            results.add(result);
        }
        return results;
    }

    private String GenDesc(String content, String word) {
        int firstPos = content.toLowerCase().indexOf(word);
        if (firstPos == -1) {
            return "";
        }
        int descBeg = firstPos < 60 ? 0 : firstPos - 60;
        if (descBeg + 160 > content.length()) {
            return content.substring(descBeg, content.length());
        }
        return content.substring(descBeg, descBeg + 160);
    }

    public static void main(String[] args) throws IOException {
        Searcher searcher = new Searcher();
        List<Result> results = searcher.search("ArrayList");
        for (Result result :results) {
            System.out.println(result);
            System.out.println("===============");
        }
    }
}
