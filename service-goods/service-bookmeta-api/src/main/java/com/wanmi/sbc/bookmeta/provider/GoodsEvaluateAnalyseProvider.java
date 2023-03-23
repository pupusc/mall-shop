package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/16:34
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsEvaluateAnalyseProvider")
public interface GoodsEvaluateAnalyseProvider {
   @PostMapping("/goods/${application.goods.version}/GoodsEvaluateAnalyse/queryByPage")
   BusinessResponse<List<GoodsEvaluateAnalyseBo>> queryByPage(@RequestBody @NotNull GoodsEvaluateAnalyseBo GoodsEvaluateAnalyseBo);

   @PostMapping("/goods/${application.goods.version}/GoodsEvaluateAnalyse/importGoodsEvaluateAnalyse")
   List<GoodsEvaluateAnalyseBo> exportGoodsEvaluateAnalyse();

}
