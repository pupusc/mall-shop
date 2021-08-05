#!/bin/bash
# echo "************** 准备编译商城文件 ****************"
path="/Users/fd/Documents/workspace/company/fandeng/server/"

array_proj=(sbc-service-common sbc-service-setting sbc-service-vas ares perseus sbc-service-erp sbc-service-customer sbc-service-account sbc-service-linkedmall sbc-service-goods sbc-service-marketing sbc-service-crm sbc-service-message sbc-service-pay sbc-service-order sbc-service-elastic sbc-platform)



function choise(){
	cd ${path}$1
	echo "\033[43m\n\n@@@@@@@@@@@@@@@ 项目 $1 开始 @@@@@@@@@@@@@@@\n\n\033[0m"
	mvn clean install
	echo "\033[43m\n\n@@@@@@@@@@@@@@@  项目 $1 完成 @@@@@@@@@@@@@@@\n\n\033[0m"
}

function choiseAll() {

	echo "\n\n"
	for p in ${array_proj[@]}
	do
		echo "\033[43m\n\n@@@@@@@@@@@@@@@ 项目 ${p} 开始 @@@@@@@@@@@@@@@\n\n\033[0m"
		cd "${path}${p}"
		mvn clean install
		echo "\033[43m\n\n@@@@@@@@@@@@@@@  项目 ${p} 完成 @@@@@@@@@@@@@@@\n\n\033[0m"
	done

	
}



echo "\033[43m******************************************请输入编译的项目，默认全部编译****************************************** \033[0m"
echo "\033[40;37m【${array_proj[@]}】\033[0m \n\n"
read inp

if [[ ${inp} = "" ]] 
then 
	echo "\033[43m\n\n************** 编译商城文件  开始 ****************\n\n\033[0m"
	choiseAll
	echo "\033[43m\n\n************** 编译商城文件  结束 ****************\n\n\033[0m"
else 
	echo "\n\n************** 编译商城文件  开始 ****************\n\n\033[0m"
	choise ${inp}
	echo "\033[43m\n\n************** 编译商城文件  结束 ****************\n\n\033[0m"
fi





