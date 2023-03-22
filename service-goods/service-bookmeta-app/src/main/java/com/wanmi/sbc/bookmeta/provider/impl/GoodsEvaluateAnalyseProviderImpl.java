package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.entity.GoodSearchKey;
import com.wanmi.sbc.bookmeta.entity.GoodsEvaluateAnalyse;
import com.wanmi.sbc.bookmeta.mapper.GoodsEvaluateAnalyseMapper;
import com.wanmi.sbc.bookmeta.provider.GoodsEvaluateAnalyseProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/16:35
 * @Description:
 */
@Validated
@Slf4j
@RestController
public class GoodsEvaluateAnalyseProviderImpl implements GoodsEvaluateAnalyseProvider {
    @Resource
    GoodsEvaluateAnalyseMapper goodsEvaluateAnalyseMapper;
    @Override
    public BusinessResponse<List<GoodsEvaluateAnalyseBo>> queryByPage(GoodsEvaluateAnalyseBo bo) {
        Page page = bo.getPage();
        List<GoodsEvaluateAnalyseBo> list = new ArrayList<>();
        try {
            page.setTotalCount((int) goodsEvaluateAnalyseMapper.getCount(bo.getName()));
            if (page.getTotalCount() <= 0) {
                return BusinessResponse.success(Collections.EMPTY_LIST, page);
            }
            List<GoodsEvaluateAnalyse> allGoodsSearchKey = goodsEvaluateAnalyseMapper.getByPage(bo.getName(), page.getOffset(), page.getPageSize());
            list = KsBeanUtil.convertList(allGoodsSearchKey, GoodsEvaluateAnalyseBo.class);
        }catch (Exception e){
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "queryByPage",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }
        return BusinessResponse.success(list,page);
    }
}
