#!/bin/sh

#lastShopId=`mysql -uroot -proot -e "select max(shop_id) as '' from shops" timshop`
#
#echo $lastShopId
mvn clean package
java -DthreadNo=10 -jar ./target/crawl-pattern-1.0-SNAPSHOT.jar
#outputfile=/Users/minhho242/Documents/temp/newcomp.sql
#outputFileInServer=public_html/scripts/newcomp.sql
#mysqldump -uroot -proot --databases timshop --tables shops --add-drop-database=FALSE --add-drop-table=FALSE --no-create-info=TRUE --comments=FALSE --lock-tables=FALSE --where="shop_id > $lastShopId" > $outputfile
#
#scp $outputfile minhho242@timshop.net:./public_html/scripts
#
#ssh minhho242@timshop.net mysql -uminhho242 -pP@ssw0rd timshop \< ${outputFileInServer}
#ssh minhho242@timshop.net mysql -uminhho242 -pP@ssw0rd peda \< ${outputFileInServer}
#
#curl http://www.timshop.net/default/index/clean-all-cache
#curl http://www.peda.vn/default/index/clean-all-cache