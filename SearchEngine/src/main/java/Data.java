import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;


public class Data {
    protected String url;
    protected String pagerank;
    protected String title;
    protected String content;
    protected double score;


    public Data() {

    }

    public Data(String url,String pagerank,String title,String content,double score) {
       this.url = url;
       this.pagerank = pagerank;
       this.title = title;
       this.content = content;
       this.score = score;


    }
    public Document toDoc(){
        Document doc = new Document();
        doc.add(new Field("url",url, TextField.TYPE_STORED));
        doc.add(new Field("pagerank",pagerank, TextField.TYPE_STORED));
        doc.add(new Field("title",title, TextField.TYPE_STORED));
        doc.add(new Field("content",content, TextField.TYPE_STORED));

        return doc;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPagerank(String pagerank) {
        this.pagerank = pagerank;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public String getPagerank() {
        return pagerank;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void show() {
        System.out.println(">");
        System.out.println(url);
        System.out.println(pagerank);
        System.out.println(content);
        System.out.println(title);

    }
}
