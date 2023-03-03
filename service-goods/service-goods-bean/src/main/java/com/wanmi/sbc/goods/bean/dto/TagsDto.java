package com.wanmi.sbc.goods.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/2 13:24
 */
@Data
public class TagsDto implements Serializable {
    private String isBook;
    private List<Tags> tags;

    @Data
    public static class Tags {
        private String showName;
        private String name;
        private Integer id;
        private Integer orderType;
        private Integer type = 1;
    }
}


