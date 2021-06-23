package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsCateBrandProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsCateDeleteRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsCateUpdateNameRequest;
import com.wanmi.sbc.goods.api.provider.cate.*;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.api.response.cate.BossGoodsCateDeleteByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateModifyResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsCateSortDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.request.GoodsCateModify;
import com.wanmi.sbc.goods.service.GoodsCateExcelService;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeBatchAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeByScopeIdRequest;
import com.wanmi.sbc.marketing.bean.dto.CouponMarketingScopeDTO;
import com.wanmi.sbc.marketing.bean.vo.CouponMarketingScopeVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * 商品分类服务
 * Created by chenli on 17/10/30.
 */
@RequestMapping("/goods")
@RestController
@Validated
@Api(tags = "BossGoodsCateController",description = "商品分类服务" )
public class BossGoodsCateController {

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsCateProvider goodsCateProvider;

    @Autowired
    private BossGoodsCateQueryProvider bossGoodsCateQueryProvider;

    @Autowired
    private GoodsCateExcelService goodsCateExcelService;

    @Autowired
    private BossGoodsCateProvider bossGoodsCateProvider;

    @Autowired
    private CouponMarketingScopeProvider couponMarketingScopeProvider;

    @Autowired
    private CouponMarketingScopeQueryProvider couponMarketingScopeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private GoodsCateExcelProvider goodsCateExcelProvider;

    @Autowired
    private EsCateBrandProvider esCateBrandProvider;

    /**
     * 查询商品分类
     *
     * @param queryRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品分类")
    @RequestMapping(value = "/goodsCates", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateVO>> list( GoodsCateListByConditionRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.putSort("isDefault", SortType.DESC.toValue());
        queryRequest.putSort("sort", SortType.ASC.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        return BaseResponse.success(goodsCateQueryProvider.listByCondition(queryRequest).getContext().getGoodsCateVOList());
    }

    /**
     * 新增商品分类
     */
    @ApiOperation(value = "新增商品分类")
    @RequestMapping(value = "/goodsCate", method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody GoodsCateAddRequest saveRequest) {
        GoodsCateVO goodsCate = goodsCateProvider.add(saveRequest).getContext().getGoodsCate();
        //查询父分类是否关联优惠券
        List<CouponMarketingScopeVO> couponMarketingScopes = couponMarketingScopeQueryProvider.listByScopeId(
                CouponMarketingScopeByScopeIdRequest.builder().scopeId(String.valueOf(goodsCate.getCateParentId()))
                        .build()).getContext().getScopeVOList();
        if(CollectionUtils.isNotEmpty(couponMarketingScopes)){
            couponMarketingScopes.stream().map(couponScope ->{
                couponScope.setMarketingScopeId(null);
                couponScope.setCateGrade(couponScope.getCateGrade()+1);
                couponScope.setScopeId(String.valueOf(goodsCate.getCateId()));
                return couponScope;
            });
            couponMarketingScopeProvider.batchAdd(
                    CouponMarketingScopeBatchAddRequest.builder()
                            .scopeDTOList(KsBeanUtil.convert(couponMarketingScopes, CouponMarketingScopeDTO.class))
                            .build());
        }
        //操作日志记录
        if (isNull(saveRequest.getGoodsCate().getCateId())) {
            operateLogMQUtil.convertAndSend("商品", "新增一级类目",
                    "新增一级类目：" + saveRequest.getGoodsCate().getCateName());
        } else {
            operateLogMQUtil.convertAndSend("商品", "添加子类目",
                    "添加子类目：" + saveRequest.getGoodsCate().getCateName());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取商品分类详情信息
     *
     * @param cateId 商品分类编号
     * @return 商品详情
     */
    @ApiOperation(value = "获取商品分类详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId",
            value = "分类Id", required = true)
    @RequestMapping(value = "/goodsCate/{cateId}", method = RequestMethod.GET)
    public ResponseEntity<GoodsCateByIdResponse> list(@PathVariable Long cateId) {

        if(cateId == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        GoodsCateByIdRequest goodsCateByIdRequest = new GoodsCateByIdRequest();
        goodsCateByIdRequest.setCateId(cateId);
        return ResponseEntity.ok(goodsCateQueryProvider.getById(goodsCateByIdRequest).getContext());
    }

    /**
     * 编辑商品分类
     */
    @ApiOperation(value = "编辑商品分类")
    @RequestMapping(value = "/goodsCate", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> edit(@Valid @RequestBody GoodsCateModify goodsCateModify) {
        if(Objects.isNull(goodsCateModify.getGoodsCate().getCateId())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BaseResponse<GoodsCateModifyResponse> modifyResponse = goodsCateProvider.modify(goodsCateModify.getGoodsCate());
        //更新es信息
        esCateBrandProvider.updateToEs(EsCateUpdateNameRequest.builder().
                goodsCateListVOList(modifyResponse.getContext().getGoodsCateListVOList()).build());
        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "编辑类目",
                "编辑类目：" + goodsCateModify.getGoodsCate().getCateName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 检测图片分类是否有子类
     */
    @ApiOperation(value = "检测图片分类是否有子类")
    @RequestMapping(value = "/goodsCate/child", method = RequestMethod.POST)
    public BaseResponse checkChild(@RequestBody BossGoodsCateCheckSignChildRequest queryRequest) {
        if(queryRequest == null || queryRequest.getCateId() == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.success(bossGoodsCateQueryProvider.checkSignChild(queryRequest));
    }

    /**
     * 检测图片分类是否有商品
     */
    @ApiOperation(value = "检测图片分类是否有商品")
    @RequestMapping(value = "/goodsCate/goods", method = RequestMethod.POST)
    public BaseResponse checkGoods(@RequestBody BossGoodsCateCheckSignGoodsRequest queryRequest) {
        if(queryRequest == null || queryRequest.getCateId() == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.success(bossGoodsCateQueryProvider.checkSignGoods(queryRequest));
    }

    /**
     * 删除商品分类
     */
    @ApiOperation(value = "删除商品分类")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId",
                    value = "分类Id", required = true)
    @RequestMapping(value = "/goodsCate/{cateId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BossGoodsCateDeleteByIdRequest bossGoodsCateDeleteByIdRequest = new BossGoodsCateDeleteByIdRequest();
        bossGoodsCateDeleteByIdRequest.setCateId(cateId);
        BaseResponse<BossGoodsCateDeleteByIdResponse> baseResponse = bossGoodsCateProvider.deleteById(bossGoodsCateDeleteByIdRequest);
        esCateBrandProvider.deleteCateFromEs(new EsCateDeleteRequest(baseResponse.getContext().getLongList(), null, false));

        GoodsCateByIdRequest goodsCateByIdRequest = new GoodsCateByIdRequest();
        goodsCateByIdRequest.setCateId(cateId);
        GoodsCateByIdResponse goodsCate = goodsCateQueryProvider.getById(goodsCateByIdRequest).getContext();
        //操作日志记录
        if (Objects.nonNull(goodsCate)) {
            operateLogMQUtil.convertAndSend("商品", "删除类目",
                    "删除类目：" + goodsCate.getCateName());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 拖拽排序商品分类
     */
    @ApiOperation(value = "拖拽排序商品分类")
    @RequestMapping(value = "/goods-cate/sort", method = RequestMethod.PUT)
    public BaseResponse goodsCateSort(@RequestBody List<GoodsCateSortDTO> goodsCateList) {
        return   goodsCateProvider.batchModifySort(new GoodsCateBatchModifySortRequest(goodsCateList));
    }

    /**
     * 下载模板
     */
    @ApiOperation(value = "下载模板")
    @RequestMapping(value = "/goodsCate/excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        goodsCateExcelService.exportTemplate();
    }


    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @RequestMapping(value = "/goodsCate/excel/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsCateExcelService.downErrExcel(commonUtil.getOperatorId(), ext);
    }

    /**
     * 上传模板
     *
     * @param uploadFile
     * @return
     */
    @ApiOperation(value = "上传模板")
    @RequestMapping(value = "/goodsCate/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(goodsCateExcelService.upload(uploadFile, commonUtil.getOperatorId()));
    }

    /**
     * 类目导入
     *
     * @param ext
     * @return
     */
    @ApiOperation(value = "类目导入")
    @RequestMapping(value = "/goodsCate/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> importGoodsCate(@PathVariable String ext) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsCateExcelService.importGoodsCate(commonUtil.getOperatorId(), ext);
        return BaseResponse.success(Boolean.TRUE);
    }

}
