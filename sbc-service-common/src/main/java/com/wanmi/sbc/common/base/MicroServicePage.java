package com.wanmi.sbc.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微服务分页Page对象
 *
 * @Author: wanggang
 * @CreateDate: 2018/10/15 11:04
 * @Version: 1.0
 */
@ApiModel
public class MicroServicePage<T>  implements Page<T>{

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private int number;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private int size;

    /**
     * 总数据大小
     */
    @ApiModelProperty(value = "总数据大小")
    private long total;

    /**
     * 分页对象
     */
    @ApiModelProperty(value = "分页对象")
    @JsonIgnore
    private Pageable pageable;

    /**
     * 具体数据内容
     */
    @ApiModelProperty(value = "具体数据内容")
    private List<T> content = new ArrayList<>();


    public MicroServicePage() {
    }

    public MicroServicePage(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }

    public MicroServicePage(Page page, Pageable pageable) {
        this(page.getContent(), pageable, page.getTotalElements());
    }

    public MicroServicePage(List<T> content, Pageable pageable, long total) {
        this.content.addAll(content);
        this.pageable = pageable;
        this.total = Objects.nonNull(pageable) ? pageable.toOptional().filter(it -> !content.isEmpty())
                .filter(it -> it.getOffset() + it.getPageSize() > total)
                .map(it -> it.getOffset() + content.size())
                .orElse(total) : 0 ;
        this.number = pageable == null ? 0 : pageable.getPageNumber();
        this.size = pageable == null ? 0 : pageable.getPageSize();
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }


    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new PageImpl(this.getConvertedContent(converter), this.getPageable(), this.total);
    }


    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }


    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : null;
    }

    @Override
    public Pageable previousPageable() {

        if (hasPrevious()) {
            return pageable.previousOrFirst();
        }

        return null;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable == null ? null : pageable.getSort();
    }

    @Override
    public String toString() {

        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    public long getTotal() {
        return total;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return this.stream().map(converter::apply).collect(Collectors.toList());
    }
}
