package com.wanmi.sbc.common.base;

import java.io.*;
import java.util.Objects;

/**
 * @author Liang Jun
 * @date 2022-02-16 14:28:00
 */
public class Page implements Serializable {
    /**
     * 当前页码
     */
    private Integer pageNo = 1;
    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
    /**
     * 总记录数
     */
    private Integer totalCount = 0;
    /**
     * 总页数
     */
    private Integer pageCount = 0;

    public Page() {}

    /**
     * Description: 构建分页对象，计算总页数
     */
    public Page(Integer pageNo, Integer pageSize) {
        this(pageNo, pageSize, 0);
    }

    /**
     *
     * Description: 构建分页对象，计算总页数
     */
    public Page(Integer pageNo, Integer pageSize, Integer totalCount) {
        setPageNo(pageNo);
        setPageSize(pageSize);
        if (totalCount != null && totalCount > 1) {
            this.totalCount = totalCount;
            this.pageCount = (totalCount - 1) / Integer.valueOf(this.pageSize) + 1;
        }
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = Objects.nonNull(pageNo) && pageNo>0 ? pageNo : 1;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = Objects.nonNull(pageSize) &&pageSize>0&&pageSize<=1000 ? pageSize : 10;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
