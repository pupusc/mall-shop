package com.wanmi.sbc.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponQueryProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponAddRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponByIdRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponListRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponPageRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponAddResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponPageResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Api(description = "卡券管理API", tags = "VirtualCouponController")
@RestController
@RequestMapping(value = "/virtualcoupon")
public class VirtualCouponController {

    @Autowired
    private VirtualCouponQueryProvider virtualCouponQueryProvider;

    @Autowired
    private VirtualCouponProvider virtualCouponProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @ApiOperation(value = "分页查询卡券")
    @PostMapping("/page")
    public BaseResponse<VirtualCouponPageResponse> getPage(@RequestBody @Valid VirtualCouponPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("createTime", "desc");
        pageReq.setStoreId(commonUtil.getStoreId());
        BaseResponse<VirtualCouponPageResponse> page = virtualCouponQueryProvider.page(pageReq);
        List<String> skuIds = page.getContext().getVirtualCouponVOPage().getContent().stream().map(VirtualCouponVO::getSkuId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (!skuIds.isEmpty()) {
            List<EsGoodsInfoVO> skus = esGoodsInfoElasticQueryProvider.listByIds(EsGoodsInfoListRequest.builder().goodsInfoIds(skuIds).build()).getContext().getGoodsInfos();
            page.getContext().getVirtualCouponVOPage().forEach(virtualCouponVO -> {
                if (StringUtils.isNotBlank(virtualCouponVO.getSkuId())) {
                    Optional<EsGoodsInfoVO> first = skus.stream().filter(sku -> virtualCouponVO.getSkuId().equals(sku.getId())).findFirst();
                    first.ifPresent(e -> {
                        virtualCouponVO.setSkuName(e.getGoodsInfo().getGoodsInfoName());
                        virtualCouponVO.setSkuNo(e.getGoodsInfo().getGoodsInfoNo());
                    });
                }
            });
        }
        return page;
    }

    @ApiOperation(value = "列表查询卡券")
    @PostMapping("/list")
    public BaseResponse<VirtualCouponListResponse> getList(@RequestBody @Valid VirtualCouponListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("createTime", "desc");
        return virtualCouponQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询卡券")
    @GetMapping("/{id}")
    public BaseResponse<VirtualCouponByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        VirtualCouponByIdRequest idReq = new VirtualCouponByIdRequest();
        idReq.setId(id);
        return virtualCouponQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增卡券")
    @PostMapping("/add")
    public BaseResponse<VirtualCouponAddResponse> add(@RequestBody @Valid VirtualCouponAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setStoreId(commonUtil.getStoreId());
        addReq.setUpdatePerson(commonUtil.getOperatorId());
        addReq.setUpdateTime(LocalDateTime.now());
        addReq.setPublishStatus(0);
        addReq.setSumNumber(0);
        addReq.setSaledNumber(0);
        return virtualCouponProvider.add(addReq);
    }

}
