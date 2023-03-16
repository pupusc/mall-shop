package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/16:06
 * @Description:
 */
@Data
public class RelationAddBO implements Serializable {
    private List<MetaBookRelationAddBO> list;
    private int bookId;
}
