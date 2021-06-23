package com.wanmi.ares.report.flow.model.reponse;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName FlowStoreRepoetResponse
 * @Description 店铺uv统计数据
 * @Author lvzhenwei
 * @Date 2019/9/11 10:01
 **/
@Data
public class FlowStoreReportResponse implements Serializable {

    private static final long serialVersionUID = 222722478899828527L;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 访问人数
     */
    private Long uv;
}
