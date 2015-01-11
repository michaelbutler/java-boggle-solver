cd vendor
rm -rf commons-cli*
wget http://mirror.olnevhost.net/pub/apache//commons/cli/source/commons-cli-1.2-src.tar.gz
tar -xvf commons-cli-1.2-src.tar.gz
rm -rf commons-cli-1.2-src.tar.gz
mv commons-cli-1.2-src commons-cli
cd commons-cli
mvn clean package

