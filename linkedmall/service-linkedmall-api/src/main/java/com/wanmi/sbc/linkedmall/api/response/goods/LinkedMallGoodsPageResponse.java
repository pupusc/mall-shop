package com.wanmi.sbc.linkedmall.api.response.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
public class LinkedMallGoodsPageResponse<T> extends MicroServicePage<T> implements Serializable {

}
