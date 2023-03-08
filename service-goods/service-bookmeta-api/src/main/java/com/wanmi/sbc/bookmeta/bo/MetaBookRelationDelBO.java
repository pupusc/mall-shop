package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/07/9:52
 * @Description:
 */
@Data
public class MetaBookRelationDelBO implements Serializable {
    private int id;

    private int bookId;
}
