package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.provider.GoodsEvaluateAnalyseProvider;
import com.wanmi.sbc.bookmeta.vo.GoodsEvaluateAnalyseReqVO;
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
 * @Date: 2023/03/17/15:42
 * @Description:
 */
@RestController
@RequestMapping("goodsEvaluateAnalyse")
public class GoodsEvaluateAnalyseController {
    @Resource
    GoodsEvaluateAnalyseProvider goodsEvaluateAnalyseProvider;
    @PostMapping("queryByPage")
    public BusinessResponse<List<GoodsEvaluateAnalyseBo>> queryByPage(@RequestBody GoodsEvaluateAnalyseReqVO reqVO) {
        GoodsEvaluateAnalyseBo convert = KsBeanUtil.convert(reqVO, GoodsEvaluateAnalyseBo.class);
        BusinessResponse<List<GoodsEvaluateAnalyseBo>> listBusinessResponse = goodsEvaluateAnalyseProvider.queryByPage(convert);
        return listBusinessResponse;
    }
}
