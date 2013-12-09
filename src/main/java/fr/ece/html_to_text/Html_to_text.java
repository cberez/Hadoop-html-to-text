package fr.ece.html_to_text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.w3c.tidy.Tidy;

public class Html_to_text {

	/**
	 * @param args : arg[0] = input file, arg[1] = output file
	 */   
	 public static void main(String[] args) throws Exception {
		    
		Configuration conf = new Configuration();
		Job job = new Job(conf, "html_to_text");
	    
		job.setOutputKeyClass(Text.class);
	 	job.setOutputValueClass(IntWritable.class);
    
 		job.setMapperClass(Map.class);
    
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		    
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    
		job.waitForCompletion(true);
	 }

	
	 public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		 
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	       
	    	System.out.println("Starting JTidy");
	        Tidy tidy = new Tidy();
	        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
	        tidy.setQuiet(true);
	        tidy.setShowWarnings(false);
	        tidy.parseDOM(new ByteArrayInputStream(value.getBytes()), System.out);
	    	
	    	
	    	/* 
	    	 * TODO : Le taff du Map
	    	 *  - Convertir le html en texte avec JTidy
	    	 *  - Retirer les mots useless avec Lucene
	    	 */
	    	
	    	/* String line = value.toString();
	        StringTokenizer tokenizer = new StringTokenizer(line);
	        while (tokenizer.hasMoreTokens()) {
	            word.set(tokenizer.nextToken());
	            context.write(word, one);
	        } */
    	}
	} 
}
