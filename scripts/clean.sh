#!/bin/bash

mvn package

hadoop fs -mkdir classification/text

hadoop jar ./target/ece-html-to-text-for-mahout-0.0.1.jar fr.ece.html_to_text.Html_to_text -libjars ./resources/jsoup-1.7.3.jar ./resources/lucene-analyzers-common-4.2.1.jar classification/crawler/energy/*/*.html classification/text/edf/
hadoop jar ./target/ece-html-to-text-for-mahout-0.0.1.jar fr.ece.html_to_text.Html_to_text -libjars ./resources/jsoup-1.7.3.jar ./resources/lucene-analyzers-common-4.2.1.jar classification/crawler/football/*/*.html classification/text/football/