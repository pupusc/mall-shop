package com.wanmi.sbc.goods.api.request.goodscatethirdcaterel;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsCateThirdCateRelDTO;
import lombok.*;
import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>平台类目和第三方平台类目映射新增参数</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateThirdCateRelAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;
	@Valid
	@Size(min = 1,message = "至少选择一个平台类目")
	private List< GoodsCateThirdCateRelDTO> goodsCateThirdCateRels;

}