#!/bin/zsh

if [ -d "webcat" ]
then
  echo "Stale webcat directory exists... deleting"
  rm -r webcat
fi

if [ -f "webcat.zip" ]
then
  echo "Stale webcat zip exists... deleting"
  rm -r webcat.zip
fi

echo "Creating temporary directory for webcat..."
mkdir webcat
echo "Copying files from src/ into temporary webcat directory..."
cp src/* webcat
echo "Zipping webcat directory into webcat.zip..."
zip -qq -r webcat.zip webcat
echo "Deleting temporary webcat directory..."
rm -r webcat
echo "Webcat submission zip file creation complete! 🚀"
