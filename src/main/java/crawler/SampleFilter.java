package crawler;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/**
 * Hadoop FileSystem PathFilter for ARC files, allowing users to limit the
 * number of files processed.
 *
 */
public class SampleFilter
    implements PathFilter {

  static int count =         0;
  static int max   = 999999999;

  public boolean accept(Path path) {

    if (!path.getName().startsWith("textData-"))
      return false;

    SampleFilter.count++;

    if (SampleFilter.count > SampleFilter.max)
      return false;

    return true;
  }
}
