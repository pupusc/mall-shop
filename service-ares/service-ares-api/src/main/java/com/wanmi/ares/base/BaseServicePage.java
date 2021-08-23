package com.wanmi.ares.base;

import lombok.Data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-6
 * \* Time: 11:28
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class BaseServicePage<T> {
    /**
     * 页码
     */
    private int number;

    /**
     * 每页条数
     */
    private int size;

    /**
     * 总数据大小
     */
    private long total;

    /**
     * 分页对象
     */
    private Pageable pageable;

    /**
     * 具体数据内容
     */
    private List<T> content = new ArrayList<>();

    public BaseServicePage() {
    }

    public BaseServicePage(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }

    public BaseServicePage(Page page, Pageable pageable) {
        this(page.getContent(), pageable, page.getTotalElements());
    }

    public BaseServicePage(List<T> content, Pageable pageable, long total) {
        this.content.addAll(content);
        this.pageable = pageable;
        this.total = !content.isEmpty() && pageable != null && pageable.getOffset() + pageable.getPageSize() > total
                ? pageable.getOffset() + content.size() : total;
        this.number = pageable == null ? 0 : pageable.getPageNumber();
        this.size = pageable == null ? 0 : pageable.getPageSize();
    }
}
