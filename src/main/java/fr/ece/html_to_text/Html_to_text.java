package fr.ece.html_to_text;

import java.io.IOException;
        


import java.io.PrintWriter;
import java.io.StringReader;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

public class Html_to_text {

	/**
	 * @param args : arg[0] = input file, arg[1] = output file
	 */   
	public static void main(String[] args) throws Exception {
		    
		Configuration conf = new Configuration();
		Job job = new Job(conf, "html_to_text");
	    
		job.setOutputKeyClass(Text.class);
		//On a pas besoin de la clé de sortie, donc on met null
	 	job.setOutputValueClass(NullWritable.class);
 		job.setMapperClass(Map.class);
 		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
	public static class Map extends Mapper<LongWritable, Text, Text, NullWritable> {
		 
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    	String text = Jsoup.parse(value.toString()).select("body").text();
	    	FrenchAnalyzer analyzer = new FrenchAnalyzer(Version.LUCENE_42);
	    	Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_42,new StringReader(text));
	    	final StandardFilter standardFilter = new StandardFilter(Version.LUCENE_42, tokenizer);	
	    	final StopFilter stopFilter = new StopFilter(Version.LUCENE_42, standardFilter, analyzer.getStopwordSet());
	    	final CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
	    	stopFilter.reset();
	    	//On ecrit si et seulement si il y a quelque chose à ecrire
	    	if(stopFilter.incrementToken()){
	    		text = charTermAttribute.toString().toString();
	    		while(stopFilter.incrementToken()) {
	    			text +=' '+charTermAttribute.toString().toString();
	    		}
	    		context.write(new Text(text), NullWritable.get());
	    	}
	    	stopFilter.close();
	    	analyzer.close();
    	}
	} 
}
