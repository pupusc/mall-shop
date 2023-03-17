package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/18:14
 * @Description:
 */
@Data
public class GoodsEvaluateAnalyseReqVO implements Serializable {
    private int id;
    private String evaluateId;
    private String evaluateContentKey;
    private String evaluateContent;
    private String name;
    private Page page = new Page(1,10);
}
