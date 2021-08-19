package com.wanmi.sbc.elastic.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardSaveRequest;
import com.wanmi.sbc.elastic.standard.mapper.EsStandardMapper;
import com.wanmi.sbc.elastic.standard.service.EsStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsStandardController implements EsStandardProvider {

    @Autowired
    private EsStandardService esStandardService;

    @Autowired
    private EsStandardMapper esStandardMapper;

    @Override
    public BaseResponse init(@RequestBody EsStandardInitRequest request) {
        esStandardService.init(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse save(@RequestBody EsStandardSaveRequest request) {
        esStandardService.save(esStandardMapper.dtoToEs(request));
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIds(@RequestBody @Valid EsStandardDeleteByIdsRequest request) {
        esStandardService.deleteByIds(request.getGoodsIds());
        return BaseResponse.SUCCESSFUL();
    }
}
