#!/bin/bash

# Download EDF
mkdir -p ./edf; rm -rf ./edf/*;
wget --recursive --mirror -np -A.html -i ./edf.txt -Q1000m -P ./edf
hadoop fs -rmr /user/big/twitter/edf
hadoop fs -put ./edf /user/big/twitter
rm -rf edf
# Download football
mkdir -p ./football; rm -rf ./football/*;
wget --recursive --mirror -np -A.html -i ./football.txt -Q1000m -P ./football
hadoop fs -rmr /user/big/twitter/football
hadoop fs -put ./edf /user/big/twitter
rm -rf football
