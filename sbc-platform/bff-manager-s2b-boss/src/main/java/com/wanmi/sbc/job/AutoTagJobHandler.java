package com.wanmi.sbc.job;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagProvider;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagQueryProvider;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagPageRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@JobHandler(value="autoTagJobHandler")
public class AutoTagJobHandler extends IJobHandler {

    @Autowired
    private AutoTagQueryProvider autoTagQueryProvider;

    @Autowired
    private AutoTagProvider autoTagProvider;

    static final String DATE_RANG = "1";
    static final String IDS = "2";

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if (StringUtils.isNotBlank(s)){
            if (s.contains("&")){
                String[] arr = s.split("&");
                if (arr.length < 2) return Resp(s);
                String type = arr[0];
                if (DATE_RANG.equals(type)) {
                    String dateArr = arr[1];
                    if (!dateArr.contains("~")) return Resp(s);
                    String[] dateRang = dateArr.split("~");
                    if (dateRang.length < 2) return Resp(s);
                    AutoTagPageRequest request = new AutoTagPageRequest();
                    LocalDateTime begin =
                            LocalDate.parse(dateRang[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MIN);
                    request.setCreateTimeBegin(begin);
                    request.setCreateTimeEnd(LocalDate.parse(dateRang[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX));
                    request.setDelFlag(DeleteFlag.NO);
                    request.setSortColumn("createTime");
                    request.setSortRole(SortType.DESC.toValue());
                    request.setPageSize(100);
                    try {
                        autoTagProvider.getSql(request);
                    } catch (Exception e){
                        return new ReturnT<String>(500, "CRM服务报错");
                    }

                } else if (IDS.equals(type)){
                    String idsArr = arr[1];
                    if (StringUtils.isBlank(idsArr)) return Resp(s);
                    String[] ids = idsArr.split(",");
                    List<Long> idList = new ArrayList<>();
                    for (String id : ids){
                        idList.add(Long.parseLong(id));
                    }
                    AutoTagPageRequest request = new AutoTagPageRequest();
                    request.setDelFlag(DeleteFlag.NO);
                    request.setIdList(idList);
                    try {
                        autoTagProvider.getSql(request);
                    } catch (Exception e){
                        return new ReturnT<String>(500, "CRM服务报错");
                    }
                }
            } else {
                return Resp(s);
            }
        } else {
            return Resp(s);
        }
        return SUCCESS;
    }

    private ReturnT<String> Resp(String s){
        XxlJobLogger.log("接受到的参数：{}", s);
        XxlJobLogger.log("正确参数格式-时间范围：1&2020-12-15～2020-12-16(注意~号是英文的)");
        XxlJobLogger.log("正确参数格式-当天：1&2020-12-15～2020-12-15(注意~号是英文的)");
        XxlJobLogger.log("正确参数格式-指定ID：2&1,2,3,4,5(注意,号是英文的)");
        return new ReturnT<String>(500, "参数错误");
    }
}
