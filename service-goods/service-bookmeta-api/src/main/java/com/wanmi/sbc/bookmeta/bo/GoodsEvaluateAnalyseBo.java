package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/13:31
 * @Description:
 */
@Data
public class GoodsEvaluateAnalyseBo implements Serializable {
    private int id;
    private String evaluateId;
    private String evaluateContentKey;
    private String name;
    private String evaluateContent;
    private Page page = new Page(1,10);

}
