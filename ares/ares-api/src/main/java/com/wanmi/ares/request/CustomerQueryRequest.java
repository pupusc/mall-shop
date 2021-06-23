package com.wanmi.ares.request;

import com.wanmi.ms.domain.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * 客户查询请求信息
 * Created by daiyitian on 2017/9/21.
 */
@Data
public class CustomerQueryRequest {

    /**
     * 页码
     */
    private Long pageNum = 0L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 搜索关键字
     * 范围仅限客户名称或账号
     */
    private String keyWord;

    /**
     * levelId查询
     */
    private Long levelId;

    private List<Long> levelIds;

    /**
     * employeeId查询
     */
    private String employeeId;

    /**
     * 客户Id
     */
    private String customerId;

    /**
     * 商家Id
     */
    private String companyInfoId;

    /**
     * 获取分页参数对象
     * @return
     */
    public PageRequest getPageable(){
        return new PageRequest(pageNum.intValue(),pageSize.intValue());
    }
}
