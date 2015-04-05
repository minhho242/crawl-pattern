#!/usr/bin/env bash

shopId=$1
ssh minhho242@timshop.net mysql -uminhho242 -pP@ssw0rd -e \'delete from shops where shop_id = ${shopId}\' timshop
curl http://www.timshop.net/default/index/clean-cache?sid=${shopId}
