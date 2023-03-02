package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/01/15:07
 * @Description:
 */
@Data
public class AuthorityQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = -93577520761453533L;

    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：1启用；2停用；
     */
    private Integer status;
    /**
     * 父级id
     */
    private String authorityUrl;
    /**
     * 场景：1适读对象；
     */
    private Integer scene;


/*    private  int pageNo;

    private  int pageSize;*/
    /**
     * 分页参数
     */
//    private Page page = new Page(pageNo, pageSize);

    private Page page = new Page(1, 10);
    private Integer isStatic;

}
