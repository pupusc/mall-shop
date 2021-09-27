package com.wanmi.sbc.index.requst;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>热销榜请求类</p>
 * Created by yinxianzhi on 2019-05-20-上午9:20.
 */
@Data
public class VersionRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * 是否刷新商品
     */
    private Boolean falshFlag = Boolean.TRUE;



}
