package com.wanmi.sbc.evaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioQueryProvider;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioSaveProvider;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioModifyRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioByIdResponse;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioModifyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 评价系数
 *
 * @author liutao
 * @date 2019/3/1 10:17 AM
 */
@RestController
@RequestMapping("/evaluate/ratio")
@Api(description = "评价系数API", tags = "EvaluateRatioController")
public class EvaluateRatioController {

    @Autowired
    private EvaluateRatioQueryProvider evaluateRatioQueryProvider;

    @Autowired
    private EvaluateRatioSaveProvider evaluateRatioSaveProvider;

    /**
     * 查询评价系数
     *
     * @return
     */
    @ApiOperation(value = "查询评价系数")
    @RequestMapping(value = "/getEvaluateInfo", method = RequestMethod.GET)
    public BaseResponse<EvaluateRatioByIdResponse> getEvaluateInfo() {
        return evaluateRatioQueryProvider.findOne();
    }

    /**
     * 更新评价系数
     *
     * @param evaluateRatioModifyRequest
     * @return
     */
    @ApiOperation(value = "更新评价系数")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public BaseResponse<EvaluateRatioModifyResponse> updateEvaluateRatio(@RequestBody EvaluateRatioModifyRequest evaluateRatioModifyRequest) {
        if (evaluateRatioModifyRequest == null){
            throw new SbcRuntimeException("K-090801");
        }

        if (evaluateRatioModifyRequest.getGoodsRatio().add(evaluateRatioModifyRequest.getLogisticsRatio())
                .add(evaluateRatioModifyRequest.getServerRatio()).compareTo(BigDecimal.ONE) < 0){
            throw new SbcRuntimeException("K-090802");
        }

        return evaluateRatioSaveProvider.modify(evaluateRatioModifyRequest);
    }

}
