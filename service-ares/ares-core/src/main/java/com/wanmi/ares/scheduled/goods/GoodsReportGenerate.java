package com.wanmi.ares.scheduled.goods;

import com.wanmi.ares.task.GoodsTask;
import com.wanmi.ares.utils.CommUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-9-24
 * \* Time: 11:25
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@JobHandler(value = "goodsReportGenerate")
@Component
public class GoodsReportGenerate extends IJobHandler {
    @Resource
    private GoodsTask goodsTask;

    @Override
    public ReturnT<String> execute(String types) throws Exception {

        if(CommUtils.isNumeric(types)) {
            String strs[] = types.split(",");
            for(String type: strs){
                if(StringUtils.isNotBlank(type)){
                    goodsTask.generate(Integer.parseInt(type));
                }
            }
        }else{
            return new ReturnT<>(ReturnT.FAIL_CODE,"请求参数不合法");
        }

        return SUCCESS;
    }
}
