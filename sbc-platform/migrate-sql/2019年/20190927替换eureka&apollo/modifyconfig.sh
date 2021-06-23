#!/bin/bash


sbc_path="/data/sbc/"

sbc_module="account customer goods order marketing pay setting"

bff_path="/data/s2b/"

bff_module="boss mobile site supplier"

for i in ${sbc_module[@]}
do
	nacos_config="spring.cloud.nacos.config.server-addr=127.0.0.1:8848\nspring.application.name=sbc-service-${i}\nspring.cloud.nacos.config.prefix=sbc-service-${i}\nspring.cloud.nacos.config.file-extension=properties"
    echo -e "${nacos_config}" > ${sbc_path}${i}/conf/application.properties
done

for i in ${bff_module[@]}
do
    nacos_config="spring.cloud.nacos.config.server-addr=127.0.0.1:8848\nspring.application.name=sbc-${i}\nspring.cloud.nacos.config.prefix=sbc-${i}\nspring.cloud.nacos.config.file-extension=properties"
    echo -e "${nacos_config}" > ${bff_path}${i}/conf/application.properties
done
