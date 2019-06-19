import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import net.sf.json.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GetData {
    private static GetData cur;

    private static String filename;
    private static Analyzer analyzer ;
    private static Directory directory = new RAMDirectory();
    private static Data myData;
    public static GetData getInstance(){
        if (cur == null){
            cur = new GetData();
            myData = new Data();
        }

        return cur;
    }


    public static Analyzer getAnalyzer() {
        return analyzer;
    }

    public static Data jason2Data(String jason){
        String content = "";
        String id = "";
        String title = "";


        JSONObject jsonObject = JSONObject.fromObject(jason);
        title = jsonObject.getString("title");
        id = jsonObject.getString("uid");
        content = jsonObject.getString("content");


        myData = new Data();
        myData.setContent(content);
//        myData.setId(id);
        myData.setTitle(title);

        return myData;
    }

    public static void setFilename(String filename) {
        GetData.filename = filename;
    }


    public static Data listData(String info){
        String[] buff = info.split(",");

        myData = new Data();
        myData.setUrl(buff[0]);
        myData.setPagerank(buff[1]);
        myData.setContent(buff[3]);
        myData.setTitle(buff[2]);
        //System.out.println(buff[3]);
        return myData;
    }


    public static Directory getDirectory(Similarity mySimilarity) throws IOException {

        analyzer = new ComplexAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        
        config.setSimilarity(mySimilarity);
        IndexWriter iwriter = new IndexWriter(directory, config);

        File file = new File(filename);
        BufferedReader reader = null;


        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        String info = "";

        int i = 0;
        boolean flag = true;
        while ((tempString = reader.readLine()) != null) {
            if (tempString.contains("index"))
                flag = false;
            if (i != 3)
            {
                i++;
                info += tempString;
                info += ",";
            }
            else
            {
                info += tempString;
                if (!flag )
                {
                    flag = true;
                }
                else
                {
                    iwriter.addDocument(listData(info).toDoc());
                }
                i = 0;
                info = "";
            }
        }
        reader.close();
        iwriter.close();
        return directory;

    }

}




