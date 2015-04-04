package crawler;
// Mongo

import org.bson.*;
import com.mongodb.hadoop.util.*;

// Hadoop
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;

/**
 * The treasury yield xml config object.
 */
public class CrawlerCorpus extends MongoTool {

    static{
        Configuration.addDefaultResource( "src/examples/hadoop-local.xml" );
        Configuration.addDefaultResource( "src/examples/mongo-defaults.xml" );
        Configuration.addDefaultResource( "mongo-crawler.xml" );
    }

    public static void main( final String[] pArgs ) throws Exception{
        System.exit( ToolRunner.run( new CrawlerCorpus(), pArgs ) );
    }
}

