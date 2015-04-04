package crawler;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.*;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import java.util.Random;

public class CrawlerCorpusMapper extends Mapper<Text, Text, Text, Text>{

	// create a counter group for Mapper-specific statistics
	private final String _counterGroup = "Custom Mapper Counters";

private static final Log LOG = LogFactory.getLog(CrawlerCorpusMapper.class);
	@Override
	public void map( Text key, Text value,
			final Context pContext )
	throws IOException, InterruptedException {
	int count = 0;
	count++;
		try {
                	Random generator = new Random();

			// Get the text content as a string.
			String pageText = value.toString();
//			System.out.println("Page Text in Mapper:"+pageText);

			// Get URL for text content as a string.
			String url = key.toString();
//			System.out.println("Mapper Key-URL:"+url);
			// Removes all punctuation.
			pageText = pageText.replaceAll("[^a-zA-Z0-9 ]", "");

			// Normalizes whitespace to single spaces.
			pageText = pageText.replaceAll("\\s+", " ");

			if (pageText == null || pageText == "") {
				System.out.println("Page Text is Null..Skipping Page");
				count++;	
			}	
			
			// Splits by space and outputs to OutputCollector.
			for (String word : pageText.split(" ")) {
 				int v = generator.nextInt() % 10000000;
                            if (v > 1 && v < 100000) {				
				pContext.write(new Text(word.toLowerCase()), new Text(url));
			    }
			}
		}
		catch (Exception ex) {
			System.out.println("Caught Exception"+ ex.toString());
			count++;			
		}
	}

}

