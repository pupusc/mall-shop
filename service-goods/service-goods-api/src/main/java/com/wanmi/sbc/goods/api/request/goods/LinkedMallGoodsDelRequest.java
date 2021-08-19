package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.goods.bean.dto.LinkedMallItemDelDTO;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemModificationDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LinkedMallGoodsDelRequest implements Serializable {

    private static final long serialVersionUID = -1447445654915774550L;

    private List<LinkedMallItemDelDTO> linkedMallItemDelDTOS;
}

