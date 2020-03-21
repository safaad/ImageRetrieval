package Lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
public class IndexBM25 {
	 private IndexBM25()  {}
		public static void main(String[] args) {
			boolean create = true;
			String docsPath = "C:\\Users\\Safaa\\Desktop\\ProjectIR\\Docs";
			String indexPath = "C:\\Users\\Safaa\\Desktop\\ProjectIR\\LuceneIndexBM25";
			

			final Path docDir = Paths.get(docsPath);
			if (!Files.isReadable(docDir)) {
				System.out.println("Document directory '" + docDir.toAbsolutePath()
						+ "' does not exist or is not readable, please check the path");
				System.exit(1);
			}

			Date start = new Date();
			try {
				System.out.println("Indexing to directory '" + indexPath + "'...");

				Directory dir = FSDirectory.open(Paths.get(indexPath));
				Analyzer analyzer = new StandardAnalyzer();
				
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
				iwc.setSimilarity(new BM25Similarity());
				if (create) {
					// Create a new index in the directory, removing any
					// previously indexed documents:
					iwc.setOpenMode(OpenMode.CREATE);
					
				} else {
					// Add new documents to an existing index:
					iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
				}

				IndexWriter writer = new IndexWriter(dir, iwc);
				indexDocs(writer, docDir);

				//writer.forceMerge();

				writer.close();

				Date end = new Date();
				System.out.println(end.getTime() - start.getTime() + " total milliseconds");

			} catch (IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		}

		static void indexDocs(final IndexWriter writer, Path path) throws IOException {
			if (Files.isDirectory(path)) {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						try {
							indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
						} catch (IOException ignore) {
							// don't index files that can't be read.
						}
						return FileVisitResult.CONTINUE;
					}
				});
			} else {
				indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
			}
		}

		/** Indexes a single document */
		static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
			try (InputStream stream = Files.newInputStream(file)) {
				// make a new, empty document
				Document doc = new Document();

				// Add the path of the file as a field named "path". Use a
				// field that is indexed (i.e. searchable), but don't tokenize
				// the field into separate words and don't index term frequency
				// or positional information:
				Field pathField = new StringField("path", file.toString(), Field.Store.YES);
				
				doc.add(pathField);

				// Add the last modified date of the file a field named "modified".
				// Use a LongPoint that is indexed (i.e. efficiently filterable with
				// PointRangeQuery). This indexes to milli-second resolution, which
				// is often too fine. You could instead create a number based on
				// year/month/day/hour/minutes/seconds, down the resolution you require.
				// For example the long value would mean
				// February , , - PM.
				doc.add(new LongPoint("modified", lastModified));

				// Add the contents of the file to a field named "contents". Specify a Reader,
				// so that the text of the file is tokenized and indexed, but not stored.
				// Note that FileReader expects the file to be in UTF-encoding.
				// If that's not the case searching for special characters will fail.
				doc.add(new TextField("contents",
						new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

				if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					// New index, so we just add the document (no old document can be there):
					System.out.println("adding " + file);
					writer.addDocument(doc);
				} else {
					// Existing index (an old copy of this document may have been indexed) so
					// we use updateDocument instead to replace the old one matching the exact
					// path, if present:
					System.out.println("updating " + file);
					writer.updateDocument(new Term("path", file.toString()), doc);
				}
			}
		}
}
