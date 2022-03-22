package com.wanmi.sbc.goods.provider.impl.sitesearch;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.fandeng.SiteSearchNotifyModel;
import com.wanmi.sbc.goods.mq.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-03-17 23:10:00
 */
@Slf4j
@RestController
public class SyncDataController {
    @Autowired
    private ProducerService producerService;

    @PostMapping("/goods/sitesearch/dataSync")
    public BusinessResponse<Object> dataSync(@RequestBody SiteSearchNotifyModel model) {
        log.info("==>>手动执行数据同步，param = {}", JSON.toJSONString(model));

        if (StringUtils.isBlank(model.getType()) || CollectionUtils.isEmpty(model.getIds())) {
            return BusinessResponse.error(CommonErrorCode.PARAMETER_ERROR, "参数错误");
        }

        List<String> syncList = new ArrayList<>();
        for (String id : model.getIds()) {
            syncList.add(id);
            if (syncList.size() == 10) {
                doSync(model.getType(), syncList);
                syncList.clear();
            }
        }
        doSync(model.getType(), syncList);
        log.info("==>>手动执行数据同步，完成！");
        return BusinessResponse.success(true);
    }

    private void doSync(String type, List<String> ids) {
        if (ids.size() > 0) {
            SiteSearchNotifyModel syncModel = new SiteSearchNotifyModel();
            syncModel.setType(type);
            syncModel.setIds(ids);
            producerService.siteSearchDataNotify(syncModel);
        }
    }
}
