package crawler;
// Mongo

import org.bson.*;
import com.mongodb.hadoop.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// Commons
//import org.apache.commons.logging.*;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
// Hadoop
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BasicBSONObject;
import com.mongodb.hadoop.io.BSONWritable;
// Java
import java.io.*;
import java.util.*;
import com.mongodb.BasicDBObjectBuilder;
	
public class CrawlerCorpusReducer extends Reducer<Text, Text, Text, BSONWritable> {
	@Override
	public void reduce( final Text pKey, 
			    final Iterable<Text> pValues,
		            final Context pContext )
		            throws IOException, InterruptedException{
		        
		     if(pKey != null && pKey.getLength() > 0){
			StringBuilder toReturn = new StringBuilder();
		        boolean first = true;
		        for ( final Text value : pValues ){
		         if(!first){
		        	 toReturn.append(", ");
		         }
		         first = false;
		         toReturn.append(value);
		        }

			BasicBSONObject output = new BasicBSONObject();
		        output.put("URL", toReturn.toString());
		        pContext.write(pKey , new BSONWritable(output));
		    } 
	}

}	

	  
