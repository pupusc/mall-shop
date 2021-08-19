package com.wanmi.sbc.setting.log;

import com.wanmi.sbc.setting.BaseTest;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.api.response.OperationLogPageResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by daiyitian on 16/6/27.
 * 操作日志服务器单元测试
 */
public class OperationLogServiceTest extends BaseTest {

    @Autowired
    private OperationLogService operationLogService;


    /**
     * 初始图片服务器
     */
    @Test
    public void testInit() {
        try {
            OperationLog log = new OperationLog();
            log.setEmployeeId("2c8080815b9f613f015b9f651bc10004");
            log.setOpName("blalal");
            log.setOpCode("登录");
            log.setOpContext("登录BOSS系统");
            operationLogService.add(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void query() {
        OperationLogListRequest queryRequest = OperationLogListRequest.builder()
                .opName("13913933910")
                .beginTime("2018-10-10 16:22:07")
                .endTime("2018-10-11 10:13:05")
                .build();
        Page<OperationLog> operationLogs = operationLogService.queryOpLogByCriteria(queryRequest);
        System.out.println(operationLogs);
    }

    public void export(){
        OperationLogListRequest queryRequest = OperationLogListRequest.builder()
                .opName("13913933910")
                .beginTime("2018-10-10 16:22:07")
                .endTime("2018-10-11 10:13:05")
                .build();
        List<OperationLog> operationLogs = operationLogService.exportOpLogByCriteria(queryRequest);
        System.out.println(operationLogs);
    }

}
