package com.wanmi.sbc.goods.api.provider.index;

import com.soybean.common.resp.CommonPageResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.index.NormalModuleReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.api.response.index.NormalModuleResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 1:06 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.goods.name}", contextId = "NormalModuleProvider")
public interface NormalModuleProvider {


    /**
     * 新增
     * @param normalModuleReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/normal-module/add")
    BaseResponse add(@RequestBody @Validated NormalModuleReq normalModuleReq);


    /**
     * 修改
     * @param normalModuleReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/normal-module/update")
    BaseResponse update(@RequestBody @Validated NormalModuleReq normalModuleReq);

    /**
     * 查询
     * @param normalModuleSearchReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/normal-module/list")
    BaseResponse<CommonPageResp<List<NormalModuleResp>>> list(@RequestBody NormalModuleSearchReq normalModuleSearchReq);


    /**
     * 发布
     * @param id
     * @param isOpen
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/normal-module/publish/{id}/{isOpen}")
    BaseResponse publish(@PathVariable("id") Integer id, @PathVariable("isOpen") Boolean isOpen);
}
