#!/bin/bash


sbc_cache_path="/data/sbc/configcache"

sbc_module="account customer goods order marketing pay setting"

bff_cache_path="/data/s2b/configcache"

bff_module="boss mobile pc supplier"

nacos_config="spring.cloud.nacos.discovery.server-addr = 127.0.0.1:8848\nlogging.level.com.alibaba.nacos = ERROR"

group_name="DEFAULT_GROUP"

mkdir -p ~/download/{account,customer,goods,order,marketing,pay,setting,boss,mobile,pc,supplier}

download_path="/root/download/"

for i in ${sbc_module[@]}
do
	cd ${sbc_cache_path}/wanmi-${i}/config-cache/
	mkdir ${group_name}
	cp wanmi-${i}+default+application.properties ${group_name}/sbc-service-${i}.properties
	sed -i '/eureka/d' ${group_name}/sbc-service-${i}.properties
	echo -e "${nacos_config}" >> ${group_name}/sbc-service-${i}.properties
	zip -q -r ${group_name}.zip ${group_name}
	rm -rf ${group_name}
	mv ${group_name}.zip ${download_path}${i}
done

for i in ${bff_module[@]}
do
        cd ${bff_cache_path}/wanmi-${i}/config-cache/
        mkdir ${group_name}
        cp wanmi-${i}+default+application.properties ${group_name}/sbc-${i}.properties
        sed -i '/eureka/d' ${group_name}/sbc-${i}.properties
        echo -e "${nacos_config}" >> ${group_name}/sbc-${i}.properties
        zip -q -r ${group_name}.zip ${group_name}
        rm -rf ${group_name}
        mv ${group_name}.zip ${download_path}${i}
done
