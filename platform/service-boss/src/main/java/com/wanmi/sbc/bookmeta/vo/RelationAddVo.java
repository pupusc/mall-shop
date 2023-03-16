package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/16:03
 * @Description:
 */
@Data
public class RelationAddVo {
    private List<MetaBookRelationAddReqVO> list;
    private String bookId;

}
