package com.wanmi.sbc.elastic.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsStandardProvider")
public interface EsStandardProvider {

    /**
     * 初始化商品库
     *
     * @param request 初始化请求结构 {@link EsStandardInitRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/standard/init")
    BaseResponse init(@RequestBody EsStandardInitRequest request);

    /**
     * 合并保存商品库
     *
     * @param request 初始化请求结构 {@link EsStandardSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/standard/save")
    BaseResponse save(@RequestBody EsStandardSaveRequest request);

    /**
     * 合并保存商品库
     *
     * @param request 初始化请求结构 {@link EsStandardSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/standard/delete-by-ids")
    BaseResponse deleteByIds(@RequestBody @Valid EsStandardDeleteByIdsRequest request);
}
