package com.soybean.elastic.api.provider.spu;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.elastic.name}", contextId = "SpuProvider")
public interface EsSpuProvider {



}
