package com.wanmi.ares.report.trade.model.request;

import com.wanmi.ares.base.BaseRequest;
import com.wanmi.ares.enums.SortOrder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * 交易报表查询
 * Created by sunkun on 2017/9/26.
 */
@Data
public class TradeReportRequest extends BaseRequest {

    /**
     * 开始时候
     * 20170925
     */
    @NotBlank
    private LocalDate beginDate;

    /**
     * 结束时间
     * 20170926
     */
    @NotBlank
    private LocalDate endDate;

    /**
     * 排序字段 (默认日期排序)
     */
    private String sortName = "date";

    /**
     * 商户id
     */
    @NotBlank
    private String companyId = "0";

    /**
     * 排序: 默认倒序
     */
    private SortOrder sortOrder = SortOrder.DESC;

    private int pageNum = 1;

    private int pageSize = 10;

    private Boolean isWeek = false;

    public Pageable getPageable() {
        return PageRequest.of(pageNum, pageSize);
    }


}
