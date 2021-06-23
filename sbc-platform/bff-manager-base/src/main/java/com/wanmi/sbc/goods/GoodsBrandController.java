package com.wanmi.sbc.goods;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsCateBrandProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsBrandProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandUpdateRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandSaveRequest;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandAddRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandModifyRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandSaveRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandAddResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.validator.GoodsBrandValidator;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品品牌服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "GoodsBrandController", description = "商品品牌服务")
@RestController
@RequestMapping("/goods")
public class GoodsBrandController {

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsBrandProvider goodsBrandProvider;

    @Autowired
    private GoodsBrandValidator goodsBrandValidator;

    @Autowired
    private EsCateBrandProvider esCateBrandProvider;

    @Autowired
    private EsGoodsBrandProvider esGoodsBrandProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @InitBinder
    public void initBinder(DataBinder binder) {
        if (binder.getTarget() instanceof GoodsBrandSaveRequest) {
            binder.setValidator(goodsBrandValidator);
        }
    }

    /**
     * 查询商品品牌
     *
     * @param queryRequest 商品品牌参数
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品品牌")
    @RequestMapping(value = "/allGoodsBrands", method = RequestMethod.GET)
    public BaseResponse<List<GoodsBrandVO>> query(GoodsBrandListRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        queryRequest.putSort("brandId", SortType.ASC.toValue());
        return BaseResponse.success(goodsBrandQueryProvider.list(queryRequest).getContext().getGoodsBrandVOList());
    }

    /**
     * 新增商品品牌
     */
    @ApiOperation(value = "新增商品品牌")
    @RequestMapping(value = "/goodsBrand", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse<Long>> add(@Valid @RequestBody GoodsBrandSaveRequest saveRequest) {
        GoodsBrandAddRequest addRequest = new GoodsBrandAddRequest();
        KsBeanUtil.copyPropertiesThird(saveRequest.getGoodsBrand(), addRequest);

        operateLogMQUtil.convertAndSend("商品", "新增品牌",
                "新增品牌：" + saveRequest.getGoodsBrand().getBrandName());
        GoodsBrandAddResponse context = goodsBrandProvider.add(addRequest).getContext();

        //同步es
        GoodsBrandVO goodsBrandVO = KsBeanUtil.convert(context, GoodsBrandVO.class);
        EsGoodsBrandSaveRequest request = EsGoodsBrandSaveRequest.builder().goodsBrandVOList(Lists.newArrayList(goodsBrandVO)).build();
        esGoodsBrandProvider.addGoodsBrandList(request);

        return ResponseEntity.ok(BaseResponse.success(context.getBrandId()));
    }

    /**
     * 编辑商品品牌
     */
    @ApiOperation(value = "编辑商品品牌")
    @RequestMapping(value = "/goodsBrand", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> edit(@Valid @RequestBody GoodsBrandSaveRequest saveRequest) {
        if (saveRequest.getGoodsBrand().getBrandId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsBrandModifyRequest request = new GoodsBrandModifyRequest();
        KsBeanUtil.copyPropertiesThird(saveRequest.getGoodsBrand(), request);
        GoodsBrandVO oldGoodsBrand = goodsBrandProvider.modify(request).getContext();
        esCateBrandProvider.updateBrandFromEs(EsBrandUpdateRequest.builder().isDelete(false)
                .goodsBrand(oldGoodsBrand).build());

        //同步es
        EsGoodsBrandSaveRequest editRequest = EsGoodsBrandSaveRequest.builder().goodsBrandVOList(Lists.newArrayList(oldGoodsBrand)).build();
        esGoodsBrandProvider.addGoodsBrandList(editRequest);

        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "编辑品牌",
                "编辑品牌：" + saveRequest.getGoodsBrand().getBrandName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
