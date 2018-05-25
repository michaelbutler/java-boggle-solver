#!/bin/bash

if ! [ -x "$(command -v mvn)" ]; then
  echo 'Error: maven (mvn) is not installed. Try installing Maven first and be sure it is in your $PATH' >&2
  exit 1
fi

BASEDIR=$(pwd)

cd vendor
rm -rf commons-cli*
wget https://archive.apache.org/dist/commons/cli/source/commons-cli-1.2-src.tar.gz
tar -xvf commons-cli-1.2-src.tar.gz
rm -rf commons-cli-1.2-src.tar.gz
mv commons-cli-1.2-src commons-cli
cd commons-cli
mvn clean package

cd $BASEDIR
mkdir -p classes

# Compile boggle source code
javac -Xlint:unchecked \
    -d "classes" \
    -classpath ./vendor/commons-cli/target/commons-cli-1.2.jar \
    src/net/butlerpc/boggle/*.java

echo "====================================================="
echo "Packages built. Now able to run using ./run.sh [ARGS]"
echo "====================================================="
