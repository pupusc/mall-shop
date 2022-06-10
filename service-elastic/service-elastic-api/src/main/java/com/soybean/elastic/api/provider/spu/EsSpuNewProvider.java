package com.soybean.elastic.api.provider.spu;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.elastic.name}", contextId = "SpuProvider")
public interface EsSpuNewProvider {

    /**
     * 包含关键词的搜索
     * @param req
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/spu/listKeyWorldEsSpu")
    BaseResponse<CommonPageResp<List<EsSpuNewResp>>> listKeyWorldEsSpu(@Validated @RequestBody EsKeyWordSpuNewQueryProviderReq req);

}
