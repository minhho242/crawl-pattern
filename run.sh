#!/bin/sh

lastSongId=`mysql -uroot -proot -e "select max(id) as '' from songs" vietchord`

echo $lastSongId

java -DthreadNo=10 -jar crawl-pattern-1.0-SNAPSHOT.jar

outputfile=/Users/minhho242/Documents/temp/newsong.sql
mysqldump -uroot -proot --databases vietchord --tables songs --add-drop-database=FALSE --add-drop-table=FALSE --no-create-info=TRUE --comments=FALSE --lock-tables=FALSE --where="id > $lastSongId" > ${outputfile}

mysql -uminhho242 -pP@ssw0rd -htimshop.net vietchord < ${outputfile}