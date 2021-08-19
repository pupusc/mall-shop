package com.wanmi.ms.domain;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回对象，继承自spring的org.springframework.data.domain.PageImpl；
 * 新增默认构造函数，确保dubbo正常序列化
 * Created by aqlu on 15/9/2.
 */
public class Page<T> extends PageImpl<T> {

    public Page() {
        this(new ArrayList<T>(0));
    }

    public Page(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public Page(List<T> content) {
        super(content);
    }
}
