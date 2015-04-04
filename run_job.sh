#!/bin/sh

###########Environment settings#############
# Modify these to match your setup.
export HADOOP_HOME="/home/ubuntu/hadoop-1.0.0" 
#INPUT_URI="s3n://aws-publicdatasets/common-crawl/parse-output/segment/1341690166822/textData-01666"
INPUT_URI="s3n://aws-publicdatasets/common-crawl/parse-output/segment/*/textData-*"
#OUTPUT_URI="mongodb://ec2-54-197-69-75.compute-1.amazonaws.com:27017/mongo_hadoop.crawler_1.out"
OUTPUT_URI="mongodb://ec2-54-197-69-75.compute-1.amazonaws.com:27017/mongo.crawler_indexed.out"
JARNAME="crawler-indexed_1.0.4-1.2.0.jar"

HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#Set the filename of the jar to match the jar you built depending
#on your hadoop version.
declare -a job_args
job_args=("jar" "$HERE/target/$JARNAME")
job_args=(${job_args[@]} "crawler.CrawlerCorpus")
job_args=(${job_args[@]} "-D" "mongo.job.verbose=true")

# INPUT SOURCE -
# To use a mongo collection as input:
#job_args=(${job_args[@]} "-D" "mongo.job.input.format=com.mongodb.hadoop.MongoInputFormat")
#job_args=(${job_args[@]} "-D" "mongo.input.uri=$INPUT_URI")

#Split settings
job_args=(${job_args[@]} "-D" "mongo.input.split_size=8")

# To use a BSON file as input, use these two lines instead:
#job_args=(${job_args[@]} "-D" "mongo.job.input.format=org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat")
job_args=(${job_args[@]} "-D" "mongo.job.input.format=com.mongodb.hadoop.BSONFileInputFormat")
job_args=(${job_args[@]} "-D" 'mapred.input.dir=s3n://aws-publicdatasets/common-crawl/parse-output/segment/1341690166822/textData-01666')

#Set the classes used for Mapper/Reducer
job_args=(${job_args[@]} "-D" "mongo.job.mapper=crawler.CrawlerCorpusMapper")
job_args=(${job_args[@]} "-D" "mongo.job.reducer=crawler.CrawlerCorpusReducer")

#Set the values used for output keys + values.
job_args=(${job_args[@]} "-D" "mongo.job.output.key=org.apache.hadoop.io.LongWritable")
job_args=(${job_args[@]} "-D" "mongo.job.output.value=com.mongodb.hadoop.io.BSONWritable")

job_args=(${job_args[@]} "-D" "mongo.job.mapper.output.key=org.apache.hadoop.io.Text")
job_args=(${job_args[@]} "-D" "mongo.job.mapper.output.value=org.apache.hadoop.io.LongWritable")

# OUTPUT
# To send the output to a mongo collection:
job_args=(${job_args[@]} "-D" "mongo.output.uri=$OUTPUT_URI")
job_args=(${job_args[@]} "-D" "mongo.job.output.format=com.mongodb.hadoop.MongoOutputFormat")

job_args=(${job_args[@]} "-D" "fs.s3.awsSecretAccessKey=****")
job_args=(${job_args[@]} "-D" "fs.s3.awsAccessKeyId=****")

# Alternatively, to write the output to a .BSON file use these two lines instead:
#job_args=(${job_args[@]} "-D" "mapred.output.dir=file:///tmp/yield_historical_out.bson")
#job_args=(${job_args[@]} "-D" "mongo.job.output.format=com.mongodb.hadoop.BSONFileOutputFormat")

echo "${job_args[@]}" "$1" 

$HADOOP_HOME/bin/hadoop "${job_args[@]}" "$1"

