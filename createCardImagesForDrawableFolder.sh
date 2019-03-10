#!/bin/bash

# COPY .png card image assets from src dir to dest dir,
# rename each card .png file that contains a digit in it and then copy it to dest dir
# leave all original card .png files in src folder un-mutated
FOLDER_PATH_SRC=$1
FILES_IN_SRC_DIR="$FOLDER_PATH_SRC/*"

FOLDER_PATH_DEST=$2
cp $FILES_IN_SRC_DIR $FOLDER_PATH_DEST
echo "folder path dest"
echo "$FOLDER_PATH_DEST";
# cd $FOLDER_PATH_DEST
for f in $FOLDER_PATH_DEST/*
do
  echo "Processing $f file..."

  # take action on each file. $f store current file name
  # cat $f
  FNAME_WITHOUT_FULL_PATH=${f##*/}
  echo "$FNAME_WITHOUT_FULL_PATH"
  CAPTURED_DIGIT_IN_FILENAME=$(echo "$FNAME_WITHOUT_FULL_PATH" | egrep -o '[[:digit:]]+' | head -n1)
  echo "printing result:"
  # ls $f
  echo "$CAPTURED_DIGIT_IN_FILENAME"
  if [[ $CAPTURED_DIGIT_IN_FILENAME = [0-9]* ]]
  then
    echo "found and replacing digit with number as word"
    case "$CAPTURED_DIGIT_IN_FILENAME" in
      2 )
        DIGIT_AS_WORD="two"
        ;;
      3 )
        DIGIT_AS_WORD="three"
        ;;
      4 )
        DIGIT_AS_WORD="four"
        ;;
      5 )
        DIGIT_AS_WORD="five"
        ;;
      6 )
        DIGIT_AS_WORD="six"
        ;;
      7 )
        DIGIT_AS_WORD="seven"
        ;;
      8 )
        DIGIT_AS_WORD="eight"
        ;;
      9 )
        DIGIT_AS_WORD="nine"
        ;;
      10 )
        DIGIT_AS_WORD="ten"
        ;;
    esac
    mv "$f" "${f/$CAPTURED_DIGIT_IN_FILENAME/$DIGIT_AS_WORD}"
  else
    echo "not replacing"
  fi
done
