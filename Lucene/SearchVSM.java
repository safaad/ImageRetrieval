package Lucene;
import py4j.GatewayServer;

import java.io.IOException;

import java.nio.file.CopyOption;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;


public class SearchVSM {
	String index;
	String field;
	int hitsPerPage;
	IndexReader reader;
	IndexSearcher searcher;
	 Analyzer analyzer;
	public SearchVSM() {
		index = "C:\\Users\\Safaa\\Desktop\\ProjectIR\\LuceneIndexVSM";
		field = "contents";
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hitsPerPage=10;
		searcher = new IndexSearcher(reader);
		analyzer = new StandardAnalyzer();
		searcher.setSimilarity(new ClassicSimilarity());
	}
	public  List<String> search(String queryString) throws Exception {
		List<String> res = null;
		QueryParser parser = new QueryParser(field, analyzer);
		
			if (queryString == null) { 
				System.out.println("NO query ");
				return null;
			}

			String line = queryString;

			if (line == null || line.length() == -1) {
				System.out.println("NO query ");
				return null;
			}

			line = line.trim();
			if (line.length() == 0) {
				System.out.println("NO query ");
				return null;
			}

			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field));

			res = doPagingSearch(searcher, query, hitsPerPage);

		return res;
	}

	@SuppressWarnings("null")
	public List< String> doPagingSearch( IndexSearcher searcher, Query query,
			int hitsPerPage) throws IOException {
        Path FROM;
        Path TO;
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
		// Collect enough docs to show pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		List<String> res = new ArrayList<String>();
		int numTotalHits = Math.toIntExact(results.totalHits.value);
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits
						+ " total matching documents collected.");

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				
				
				Document doc = searcher.doc(hits[i].doc);
				String path = doc.get("path");
				
				System.out.println("doc=" + path + " score=" + hits[i].score);
				res.add(path+"\t"+Float.toString(hits[i].score));
				String imagepath=(path.split(".txt")[0]).split("Docs")[1];
				System.out.println(imagepath);
	            FROM=Paths.get("C:\\Users\\Safaa\\Desktop\\ProjectIR\\train2014"+imagepath+".jpg");
	            TO = Paths.get("C:\\Users\\Safaa\\Desktop\\ProjectIR\\static\\img\\resultsVSM"+imagepath+".jpg");
	            java.nio.file.Files.copy(FROM, TO, options);

			}

		return res;
	}
	  public static void main(String[] args) {
		    Search app = new Search();
		    GatewayServer server = new GatewayServer(app);
		    server.start();
		    // app is now the gateway.entry_point

		  }


}