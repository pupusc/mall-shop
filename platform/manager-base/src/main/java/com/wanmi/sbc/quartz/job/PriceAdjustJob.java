package com.wanmi.sbc.quartz.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceExecuteFailRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceExecuteRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordByAdjustNoRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.AdjustPriceExecuteResponse;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import com.wanmi.sbc.quartz.enums.TaskBizType;
import com.wanmi.sbc.quartz.enums.TaskStatus;
import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import com.wanmi.sbc.quartz.service.QuartzManagerService;
import com.wanmi.sbc.quartz.service.TaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class PriceAdjustJob implements Job {

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private QuartzManagerService quartzManagerService;

    @Autowired
    private PriceAdjustmentRecordQueryProvider priceAdjustmentRecordQueryProvider;

    @Autowired
    private PriceAdjustmentRecordDetailProvider adjustmentRecordDetailProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private PriceAdjustmentRecordDetailProvider priceAdjustmentRecordDetailProvider;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String adjustNo = jobDataMap.getString("bizId");
        log.info("商品调价，制单号：{},定时任务开始运行！", adjustNo);
        TaskInfo taskInfo = taskJobService.findByBizId(adjustNo);
        if (Objects.isNull(taskInfo) || taskInfo.getBizType() != TaskBizType.PRICE_ADJUST || taskInfo.getState() == TaskStatus.END) {
            log.info("根据业务ID：{},查询此任务已不存在/不是商品调价任务/已结束=====>>移除此任务！", adjustNo);
            quartzManagerService.delete(adjustNo, TaskJobService.PRICE_ADJUST);
            return;
        }

        PriceAdjustmentRecordVO recordVO = priceAdjustmentRecordQueryProvider
                .getByAdjustNo(PriceAdjustmentRecordByAdjustNoRequest.builder().adjustNo(adjustNo).build())
                .getContext().getPriceAdjustmentRecordVO();

        if (Objects.isNull(recordVO)) {
            log.info("根据调价制单号：{},未查询到调价记录，此记录已删除========>>更新任务状态&&移除此任务", adjustNo);
            //更新任务为已结束
            this.modifyTaskInfo(adjustNo, taskInfo);
            return;
        }

        //执行调价生效
        try {
            AdjustPriceExecuteResponse response = adjustmentRecordDetailProvider.adjustPriceExecute(new AdjustPriceExecuteRequest(adjustNo
                    , recordVO.getStoreId())).getContext();
            if(CollectionUtils.isNotEmpty(response.getSkuIds())) {
                EsGoodsInfoAdjustPriceRequest adjustPriceRequest = EsGoodsInfoAdjustPriceRequest.builder()
                        .goodsInfoIds(response.getSkuIds())
                        .type(response.getType()).build();
                //同步es
                BaseResponse baseResponse = esGoodsInfoElasticProvider.adjustPrice(adjustPriceRequest);
                if (CommonErrorCode.SUCCESSFUL.equals(baseResponse.getCode())) {
                    //调价成功调用
                    log.info("根据调价制单号：{},进行商品调价，调价成功========>>更新任务状态&&移除此任务", adjustNo);
                }
            }
        } catch (Exception e){
            log.error("调价失败，" + e);
            AdjustPriceExecuteFailRequest failRequest = AdjustPriceExecuteFailRequest.builder()
                    .adjustNo(adjustNo)
                    .result(PriceAdjustmentResult.FAIL)
                    .failReason("系统异常").build();
            priceAdjustmentRecordDetailProvider.adjustPriceExecuteFail(failRequest);
        } finally {
            this.modifyTaskInfo(adjustNo, taskInfo);
        }



    }

    /**
     * 更新任务状态
     *
     * @param adjustNo
     * @param taskInfo
     */
    private void modifyTaskInfo(String adjustNo, TaskInfo taskInfo) {
        //更新任务为已结束
        taskInfo.setEndTime(LocalDateTime.now());
        taskInfo.setState(TaskStatus.END);
        taskJobService.addTaskJob(taskInfo);
        //删除任务
        quartzManagerService.delete(adjustNo, TaskJobService.PRICE_ADJUST);
    }
}
