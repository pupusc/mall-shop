package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointDeductRequest;
import com.wanmi.sbc.customer.api.response.fandeng.FanDengConsumeResponse;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsProvider;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsQueryProvider;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsListRequest;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsModifyRequest;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import com.wanmi.sbc.order.bean.vo.ExceptionOfTradePointsVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * @ClassName PointDeductionTradeJobHandler
 * @Description 积分抵扣订单补偿定时任务
 * @Author caofang
 * @Date 2021/3/17 11:21
 **/
@JobHandler(value = "pointDeductionTradeJobHandler")
@Component
@Slf4j
public class PointDeductionTradeJobHandler extends IJobHandler {

    private static final List<HandleStatus> HANDLE_STATUSES = Arrays.asList(HandleStatus.PROCESSING_FAILED, HandleStatus.PENDING);

    private static final List<Integer> errorTimes = Arrays.asList(1, 2);

    @Autowired
    private ExternalProvider externalProvider;

    @Autowired
    private ExceptionOfTradePointsQueryProvider exceptionOfTradePointsQueryProvider;

    @Autowired
    private ExceptionOfTradePointsProvider exceptionOfTradePointsProvider;

    @Override
    public ReturnT<String> execute(String paramStr) throws Exception {
        //查询错误次数小于3
        List<ExceptionOfTradePointsVO> exceptionOfTradePointsVOList = exceptionOfTradePointsQueryProvider
                .list(ExceptionOfTradePointsListRequest.builder()
                        .handleStatuses(HANDLE_STATUSES)
                        .errorTimes(errorTimes)
                        .delFlag(DeleteFlag.NO)
                        .build()).getContext().getExceptionOfTradePointsVOList();
        if (CollectionUtils.isNotEmpty(exceptionOfTradePointsVOList)) {
            //单次只处理一百条数据
            if (exceptionOfTradePointsVOList.size() > 100) {
                exceptionOfTradePointsVOList = exceptionOfTradePointsVOList.subList(0, 100);
            }
            for (ExceptionOfTradePointsVO exceptionOfTradePointsVO : exceptionOfTradePointsVOList) {
                this.toFandeng(exceptionOfTradePointsVO);
            }
        }
        return SUCCESS;
    }

    @Async
    public void toFandeng(ExceptionOfTradePointsVO exceptionOfTradePoints) {
        //调用樊登积分扣除
        BaseResponse<FanDengConsumeResponse> fanDengConsumeResponseBaseResponse = new BaseResponse<>();
        try {
            fanDengConsumeResponseBaseResponse = externalProvider.pointDeduct(FanDengPointDeductRequest.builder()
                    .deductCode(exceptionOfTradePoints.getDeductCode()).build());
            //处理成功更新状态
            exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
            exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
            exceptionOfTradePoints.setHandleStatus(HandleStatus.SUCCESSFULLY_PROCESSED);
            log.info("樊登积分扣除成功！积分抵扣码：{}", exceptionOfTradePoints.getDeductCode());
        } catch (Exception e) {
            //存在则更新，更新错误信息及处理状态
            exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
            exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
            exceptionOfTradePoints.setHandleStatus(HandleStatus.PROCESSING_FAILED);
            exceptionOfTradePoints.setErrorTime(exceptionOfTradePoints.getErrorTime() + 1);
            log.error("樊登积分扣除失败！积分抵扣码：{}", exceptionOfTradePoints.getDeductCode());
        } finally {
            //更新积分订单抵扣
            exceptionOfTradePointsProvider.modify(KsBeanUtil.convert(exceptionOfTradePoints, ExceptionOfTradePointsModifyRequest.class));
            log.info("积分订单抵扣异常信息：{}", JSONObject.toJSONString(exceptionOfTradePoints));
        }
    }
}
