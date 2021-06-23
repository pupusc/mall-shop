package com.wanmi.sbc.job;

import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponQueryProvider;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponSaveProvider;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponSwitchRequest;
import com.wanmi.sbc.marketing.bean.vo.PointsCouponVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @since 2019/5/24
 */
@JobHandler(value = "pointsCouponJobHandler")
@Component
@Slf4j
public class PointsCouponJobHandler extends IJobHandler {

    @Autowired
    private PointsCouponQueryProvider pointsCouponQueryProvider;

    @Autowired
    private PointsCouponSaveProvider pointsCouponSaveProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("积分兑换券定时任务执行 " + LocalDateTime.now());
        List<PointsCouponVO> pointsCouponVOList = pointsCouponQueryProvider.queryOverdueList().getContext().getPointsCouponVOList();
        int total = pointsCouponVOList.size();
        pointsCouponVOList.forEach(pointsCouponVO -> {
            PointsCouponSwitchRequest pointsCouponSwitchRequest = PointsCouponSwitchRequest.builder()
                    .pointsCouponId(pointsCouponVO.getPointsCouponId())
                    .status(EnableStatus.DISABLE)
                    .build();
            pointsCouponSaveProvider.modifyStatus(pointsCouponSwitchRequest);
        });
        XxlJobLogger.log("积分商品定时任务执行结束： " + LocalDateTime.now() + ",处理总数为：" + total);
        return SUCCESS;
    }
}
