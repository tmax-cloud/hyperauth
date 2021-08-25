#!/bin/bash

# destination file
output=./CHANGELOG.md
timestamp=`date`
version=$1
prev_version=$2

if [ -z $version ]; then
version=4.0.0.x
fi

if [ -z $prev_version ]; then
prev_version=4.0.0.0
fi

backup="./CHANGELOG_$prev_version.md"
if [ -f $output ]; then
echo "!!!changelog file exist"
mv $output $backup
fi

echo "workdir: `pwd`"
echo "!!!make changelog for $version"
echo "!!!prev version: $prev_version"

# gen file
echo "# HyperAuthServer changelog!!" > $output
echo "All notable changes to this project will be documented in this file." >> $output

# make commit log to changelog
echo -e "\n<!--------------------- Below Hyperauth SPI log --------------------->" >> $output

echo -e "\n### Added" >> $output
git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" --grep="^\[feat\].*" -i >> $output
#git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" >> $output

echo -e "\n### Changed" >> $output
git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" --grep="^\[mod\].*" -i >> $output

echo -e "\n### Fixed" >> $output
git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" --grep="^\[ims\]\[[0-9]*\].*" -i >> $output

echo -e "\n### CRD yaml" >> $output
git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" --grep="^\[crd\].*" -i >> $output

echo -e "\n### Etc" >> $output
git log v$prev_version..HEAD --no-merges --oneline --format="  - %s by %cN" --grep="^\[etc\].*" -i >> $output

echo -e "\n<!--------------------- v$version end --------------------->" >> $output

if [ -f $backup ]; then
echo "!!!add previous changelog"
sed '1,2d' $backup >> $output
rm $backup
fi

echo "!!!done"
