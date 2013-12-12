#!/bin/bash

START_PATH=`pwd`
cd $START_PATH
cd ../..

WORK_DIR=/user/cberez

set -e
set -x

echo "Preparing classification data"
hadoop fs -rm -p ${WORK_DIR}/classification-all
hadoop fs -mkdir ${WORK_DIR}/classification-all
hadoop fs -cp -R ${WORK_DIR}/classification/text/*/*/* ${WORK_DIR}/classification-all

echo "Creating sequence files from classification data"
./bin/mahout seqdirectory \
  -i ${WORK_DIR}/classification-all \
  -o ${WORK_DIR}/classification-seq -ow -xm sequential

echo "Converting sequence files to vectors"
./bin/mahout seq2sparse \
  -i ${WORK_DIR}/classification-seq \
  -o ${WORK_DIR}/classification-vectors  -lnorm -nv  -wt tfidf

# echo "Creating training and holdout set with a random 80-20 split of the generated vector dataset"
# ./bin/mahout split \
#   -i ${WORK_DIR}/classification-vectors/tfidf-vectors \
#   --trainingOutput ${WORK_DIR}/classification-train-vectors \
#   --testOutput ${WORK_DIR}/classification-test-vectors  \
#   --randomSelectionPct 40 --overwrite --sequenceFiles -xm sequential

echo "Training Naive Bayes model"
./bin/mahout trainnb \
  -i ${WORK_DIR}/classification-vectors -el \
  -o ${WORK_DIR}/model \
  -li ${WORK_DIR}/labelindex \
  -ow $c

echo "Self testing on training set"
./bin/mahout testnb \
  -i ${WORK_DIR}/classification-vectors\
  -m ${WORK_DIR}/model \
  -l ${WORK_DIR}/labelindex \
  -ow -o ${WORK_DIR}/classification-testing $c

# echo "Testing on holdout set"
# ./bin/mahout testnb \
#   -i ${WORK_DIR}/classification-test-vectors\
#   -m ${WORK_DIR}/model \
#   -l ${WORK_DIR}/labelindex \
#   -ow -o ${WORK_DIR}/classification-testing $c