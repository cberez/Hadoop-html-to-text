#!/bin/bash

mvn package

hadoop fs -mkdir /user/cberez/classification/text

hadoop jar ./../target/ece-html-to-text-for-mahout-0.0.1.jar fr.ece.html_to_text.Html_to_text -libjars ./../resources/jsoup-1.7.3.jar ./../resources/lucene-analyzers-common-4.2.1.jar /user/cberez/classification/crawler/energy/*/*.html /user/cberez/classification/text/edf/
hadoop jar ./../target/ece-html-to-text-for-mahout-0.0.1.jar fr.ece.html_to_text.Html_to_text -libjars ./../resources/jsoup-1.7.3.jar ./../resources/lucene-analyzers-common-4.2.1.jar /user/cberez/classification/crawler/football/*/*.html /user/cberez/classification/text/football/