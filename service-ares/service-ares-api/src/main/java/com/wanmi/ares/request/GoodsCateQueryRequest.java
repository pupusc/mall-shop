package com.wanmi.ares.request;

import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ms.domain.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 商品分类
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GoodsCateQueryRequest extends BaseMqRequest {

    private static final long serialVersionUID = -1827018468986375856L;

  /*  *//**
     * 删除标识
     *//*
    private Boolean delFlag;*/

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 分类层次
     */
    private Integer grade;

    /**
     * 商家id
     */
    private String companyId;

    /**
     * 批量ID查询
     */
    private List<String> ids;


    /**
     * 父分类ids
     */
    private List<Long> parentIds;

    /**
     * 页码
     */
    private Long pageNum = 0L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 获取分页参数对象
     * @return
     */
    public PageRequest getPageable(){
        return new PageRequest(pageNum.intValue(),pageSize.intValue());
    }
}
