package com.wanmi.sbc.goods.provider.impl.index;

import com.soybean.common.resp.CommonPageResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.NormalModuleCategoryEnum;
import com.wanmi.sbc.goods.api.provider.index.NormalModuleProvider;
import com.wanmi.sbc.goods.api.request.index.NormalModuleReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuSearchReq;
import com.wanmi.sbc.goods.api.response.index.NormalModuleResp;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.goods.index.service.NormalModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 12:49 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@RestController
public class NormalModuleController implements NormalModuleProvider {


    @Autowired
    private NormalModuleService normalModuleService;


    @Override
    public BaseResponse add(NormalModuleReq normalModuleReq) {
        normalModuleReq.setModelCategory(NormalModuleCategoryEnum.BACK_POINT.getCode());
        normalModuleService.add(normalModuleReq);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse update(NormalModuleReq normalModuleReq) {
        normalModuleReq.setModelCategory(NormalModuleCategoryEnum.BACK_POINT.getCode());
        normalModuleService.update(normalModuleReq);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse<CommonPageResp<List<NormalModuleResp>>> list(NormalModuleSearchReq normalModuleSearchReq) {

        return BaseResponse.success(normalModuleService.list(normalModuleSearchReq));
    }


    @Override
    public BaseResponse publish(Integer id, Boolean isOpen) {
        normalModuleService.publish(id, isOpen);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse<List<NormalModuleSkuResp>> listNormalModuleSku(NormalModuleSkuSearchReq normalModuleSkuSearchReq) {
        return BaseResponse.success(normalModuleService.listNormalModuleSku(normalModuleSkuSearchReq));
    }
}
