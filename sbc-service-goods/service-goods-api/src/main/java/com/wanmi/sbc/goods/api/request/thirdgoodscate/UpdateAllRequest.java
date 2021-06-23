package com.wanmi.sbc.goods.api.request.thirdgoodscate;

import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class UpdateAllRequest implements Serializable {
    private static final long serialVersionUID = 4393635284800886207L;
    @Valid
    private List<ThirdGoodsCateDTO> thirdGoodsCateDTOS;
}
