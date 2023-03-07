package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryRespBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.provider.MetaTradeProvider;
import com.wanmi.sbc.bookmeta.vo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:30
 * @Description:
 */
@RestController
@RequestMapping("metaTrade")
public class MetaTradeController {
    @Resource
    private MetaTradeProvider metaTradeProvider;
    @PostMapping("queryMetaTradeTree")
    public BusinessResponse<MetaTradePageQueryRespVO> getMetaTradeTree(@RequestBody MetaTradeQueryByPageReqVO pageRequest) {
        MetaTradePageQueryBO convert = KsBeanUtil.convert(pageRequest, MetaTradePageQueryBO.class);
        MetaTradePageQueryRespBO list = this.metaTradeProvider.getMetaTadeTree(convert);
        MetaTradePageQueryRespVO metaTradeTreeRespVOS = KsBeanUtil.convert(list, MetaTradePageQueryRespVO.class);
        return BusinessResponse.success(metaTradeTreeRespVOS);
    }

    @PostMapping("addMetaTrade")
    public BusinessResponse<Integer> addMetaTrade(@RequestBody MetaTradeAddReq pageRequest) {
        MetaTradeBO metaTradeBO = KsBeanUtil.convert(pageRequest, MetaTradeBO.class);
        int i = this.metaTradeProvider.addMetaTade(metaTradeBO);
        return BusinessResponse.success(i);
    }
    @PostMapping("updateMetaTrade")
    public BusinessResponse<Integer> updateMetaTrade(@RequestBody MetaTradeAddReq pageRequest) {
        MetaTradeBO metaTradeBO = KsBeanUtil.convert(pageRequest, MetaTradeBO.class);
        int i = this.metaTradeProvider.updateMetaTade(metaTradeBO);
        return BusinessResponse.success(i);
    }
    @PostMapping("deleteMetaTrade")
    public BusinessResponse<Integer> deleteMetaTrade(@RequestBody  MetaLabelQueryByPageReqVO pageRequest) {
        int i = this.metaTradeProvider.deleteMetaTade(pageRequest.getId());
        return BusinessResponse.success(i);
    }

}
