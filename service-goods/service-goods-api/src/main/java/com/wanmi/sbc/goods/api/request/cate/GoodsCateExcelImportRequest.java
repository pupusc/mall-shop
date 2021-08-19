package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 10:17 2018-12-18
 * @Description: 商品分类excel导入请求Request
 */
@ApiModel
@Data
public class GoodsCateExcelImportRequest implements Serializable {

    private static final long serialVersionUID = -1565355981812149930L;

    @ApiModelProperty("类目列表")
    private List<GoodsCateVO> goodsCateList;

}
