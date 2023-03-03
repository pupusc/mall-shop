package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.provider.MetaTradeProvider;
import com.wanmi.sbc.bookmeta.vo.MetaLabelQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaTradeAddReq;
import com.wanmi.sbc.bookmeta.vo.MetaTradeTreeRespVO;
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
    public BusinessResponse<List<MetaTradeTreeRespVO>> getMetaTradeTree(@RequestBody MetaLabelQueryByPageReqVO pageRequest) {
        List<MetaTradeBO> list = this.metaTradeProvider.getMetaTadeTree(pageRequest.getParentId());
        List<MetaTradeTreeRespVO> metaTradeTreeRespVOS = KsBeanUtil.convertList(list, MetaTradeTreeRespVO.class);
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
