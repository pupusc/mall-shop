package com.soybean.elastic.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 3:14 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum SearchBookListSortTypeEnum {

    DEFAULT(0, "默认"),
    UPDATE_TIME(1, "更新时间排序"),
    HAS_TOP_UPDATE_TIME(2, "按照置顶、更新时间排序")
    ;

    private Integer code;

    private String message;


    public static SearchBookListSortTypeEnum get(Integer code) {
        for (SearchBookListSortTypeEnum bookListSortType : values()) {
            if (Objects.equals(bookListSortType.getCode(), code)) {
                return bookListSortType;
            }
        }
        return null;
    }
}
