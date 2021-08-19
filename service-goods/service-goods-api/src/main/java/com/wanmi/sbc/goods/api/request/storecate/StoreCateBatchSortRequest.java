package com.wanmi.sbc.goods.api.request.storecate;

import com.wanmi.sbc.goods.bean.dto.StoreCateRequestDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCateBatchSortRequest extends StoreCateRequestDTO implements Serializable {

    private static final long serialVersionUID = -1328529228647299748L;


}
