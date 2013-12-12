#!/bin/bash

hadoop fs -mkdir /user/cberez/classification

# Download EDF
mkdir -p ./edf; rm -rf ./edf/*;
wget --recursive --mirror -np -A.html -i edf.txt -Q1000m -P ./edf
hadoop fs -rmr /user/cberez/classification/edf
hadoop fs -put ./edf /user/cberez/classification
rm -rf edf
# Download football
mkdir -p ./football; rm -rf ./football/*;
wget --recursive --mirror -np -A.html --level=2 -i football.txt -Q1000m -P ./football
hadoop fs -rmr /user/cberez/classification/football
hadoop fs -put ./football /user/cberez/classification
rm -rf football
