import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.*;

public class Search {

    static MultiFieldQueryParser parser;
    static IndexSearcher isearcher;
    static Search cur;
    public static Search getInstance() throws IOException, ParseException {

        if (cur == null) {
            cur = new Search();
        }
        return cur;
    }

    public static ArrayList<Data>  demo(String str)throws IOException, ParseException
    {

        Query query;

        System.out.println("q:  "+str);
            if (str== null) {return  null;}
                System.out.println("q:  "+str);
                query = parser.parse(str);
                System.out.println("parser:  "+str);


                ScoreDoc[] hits = isearcher.search(query, 50).scoreDocs;

                ArrayList<Data> final_list= new ArrayList<Data>();
//
                for (int i = 0; i < hits.length; i++) {
                    Document hitDoc = isearcher.doc(hits[i].doc);

                    Data d = new Data(hitDoc.get("url"),
                            hitDoc.get("pagerank"),
                            hitDoc.get("title"),
                            hitDoc.get("content"),
                            hits[i].score * (16 + Math.log(Float.parseFloat(hitDoc.get("pagerank"))) / Math.log(Math.E)));


                    final_list.add(d);

                    System.out.println(hits[i].score + "  " + d.getTitle());

                }

                Collections.sort(final_list, new Comparator<Data>() {
                    public int compare(Data o1, Data o2) {
                        return !(o2.getScore() >= o1.getScore()) ? -1:(o1.getScore()==o2.getScore()? 0:1);
                    }
                });


            System.out.println(">>>>");

            return final_list;

    }

    public Search() throws IOException, ParseException {

        BM25Similarity mySimilarity = new BM25Similarity();

        GetData.setFilename("Data/page_final.txt");
        Directory directory = GetData.getDirectory(mySimilarity);

        DirectoryReader ireader = DirectoryReader.open(directory);
        isearcher = new IndexSearcher(ireader);
        isearcher.setSimilarity(mySimilarity);

        String[] fields = {
                 "title"
                ,"content"
                };
        Map<String,Float> boosts = new HashMap<String, Float>();

        boosts.put("title",0.30f);
        boosts.put("content",0.60f);

        parser = new MultiFieldQueryParser(fields,GetData.getAnalyzer(),boosts);

    }
}