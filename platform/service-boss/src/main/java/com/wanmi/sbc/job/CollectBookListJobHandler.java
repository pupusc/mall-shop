package com.wanmi.sbc.job;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 书单采集
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/15 2:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@JobHandler("collectBookListJobHandler")
@Component
@Slf4j
public class CollectBookListJobHandler extends IJobHandler {


    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        CollectJobReq collectJobReq = new CollectJobReq();
        try {
            if (StringUtils.isNotBlank(param)) {
                String[] split = param.split(",");
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                collectJobReq.setStartTime(LocalDateTime.parse(split[0], df));
                collectJobReq.setEndTime(LocalDateTime.parse(split[1], df));
            }
        } catch (Exception ex) {
            log.error("CollectSpuNewJobHandler run error", ex);
        }
        esBookListModelProvider.init(collectJobReq);
        return ReturnT.SUCCESS;
    }
}
