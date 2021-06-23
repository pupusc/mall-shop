package com.wanmi.sbc.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.*;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleAddResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleModifyResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSalePageResponse;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsDetailResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsDTO;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "预约抢购管理API", tags = "AppointmentSaleController")
@RestController
@RequestMapping(value = "/appointmentsale")
public class AppointmentSaleController {

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private AppointmentSaleProvider appointmentSaleProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;


    @Autowired
    private AppointmentSaleGoodsQueryProvider appointmentSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询预约抢购")
    @PostMapping("/page")
    public BaseResponse<AppointmentSalePageResponse> getPage(@RequestBody @Valid AppointmentSalePageRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.putSort("createTime", "desc");
        request.setStoreId(commonUtil.getStoreId());
        request.setStatus(request.getQueryTab());
        request.setPlatform(Platform.SUPPLIER);
        return appointmentSaleQueryProvider.page(request);
    }


    @ApiOperation(value = "根据id查询预约抢购商品详情")
    @GetMapping("/goods/{id}")
    public BaseResponse<AppointmentSaleGoodsDetailResponse> getAppointmentSaleGoodsDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<AppointmentSaleGoodsVO> saleGoodsVOList = getAppointmentSaleGoodsInfo(id);

        return BaseResponse.success(AppointmentSaleGoodsDetailResponse.builder().appointmentSaleGoodsVOList(saleGoodsVOList).build());
    }


    @ApiOperation(value = "新增预约抢购活动")
    @PostMapping("/add")
    public BaseResponse<AppointmentSaleAddResponse> add(@RequestBody @Valid AppointmentSaleAddRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.setCreatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        validateWholeSaleGood(request.getAppointmentSaleGoods().stream().map(AppointmentSaleGoodsDTO::getGoodsInfoId).collect(Collectors.toList()), request.getStoreId());
        List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(request.getAppointmentSaleGoods().stream().map(v -> v.getGoodsId()).collect(Collectors.toList())).build()).getContext().getGoodsVOList();
        Optional<GoodsVO> optional = goodsVOList.stream().filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType())).findFirst();
        if (optional.isPresent()) {
            throw  new SbcRuntimeException("K-080023");
        }
        return appointmentSaleProvider.add(request);
    }


    /**
     * 批发商品校验
     *
     * @param goodInfoIdList
     */
    private void validateWholeSaleGood(List<String> goodInfoIdList, Long storeId) {
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().goodsInfoIds(goodInfoIdList).build()).getContext().getGoodsInfos();
        if (CollectionUtils.isEmpty(goodsInfos)) {
            throw new SbcRuntimeException("K-000009");
        }
        if (CollectionUtils.isNotEmpty(goodsInfos.stream().filter(m -> m.getSaleType() == SaleType.WHOLESALE.toValue()).collect(Collectors.toList()))) {
            throw new SbcRuntimeException("K-600006");
        }
        if (CollectionUtils.isNotEmpty(goodsInfos.stream().filter(m -> Objects.isNull(m.getStoreId()) || !m.getStoreId().equals(storeId)).collect(Collectors.toList()))) {
            throw new SbcRuntimeException("K-110208");
        }

    }


    @ApiOperation(value = "根据id查询预约抢购详情(编辑)")
    @GetMapping("/{id}")
    public BaseResponse<AppointmentSaleModifyResponse> getAppointmentSaleDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        AppointmentSaleVO appointmentSaleVO = appointmentSaleQueryProvider.getById(AppointmentSaleByIdRequest.builder().id(id).storeId(commonUtil.getStoreId()).build()).getContext().getAppointmentSaleVO();

        List<AppointmentSaleGoodsVO> saleGoodsVOList = getAppointmentSaleGoodsInfo(id);

        appointmentSaleVO.setAppointmentSaleGoods(saleGoodsVOList);
        return BaseResponse.success(AppointmentSaleModifyResponse.builder().appointmentSaleVO(appointmentSaleVO).build());
    }


    /**
     * 获取活动商品详情
     *
     * @param id
     * @return
     */
    private List<AppointmentSaleGoodsVO> getAppointmentSaleGoodsInfo(Long id) {
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();

        List<AppointmentSaleGoodsVO> saleGoodsVOList = appointmentSaleGoodsQueryProvider.list(AppointmentSaleGoodsListRequest.builder().
                appointmentSaleId(id).storeId(commonUtil.getStoreId()).build()).getContext().getAppointmentSaleGoodsVOList();
        if (CollectionUtils.isEmpty(saleGoodsVOList)) {
            return saleGoodsVOList;
        }
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().
                goodsInfoIds(saleGoodsVOList.stream().map(AppointmentSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build()).getContext().getGoodsInfos();

        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, m -> m));

        List<String> skuIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

        goodsInfoSpecDetailMap.putAll(goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                .filter(v -> StringUtils.isNotBlank(v.getDetailName()))
                .collect(Collectors.toMap(GoodsInfoSpecDetailRelVO::getGoodsInfoId, GoodsInfoSpecDetailRelVO::getDetailName, (a, b) -> a.concat(" ").concat(b))));

        Map<Long, GoodsBrandVO> goodsBrandVOMap = goodsBrandQueryProvider.listByIds(GoodsBrandByIdsRequest.builder().brandIds(goodsInfos.stream().
                map(GoodsInfoVO::getBrandId).collect(Collectors.toList())).build()).getContext().getGoodsBrandVOList()
                .stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId, m -> m));


        Map<Long, GoodsCateVO> goodsCateVOMap = goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(goodsInfos.stream().map(GoodsInfoVO::getCateId).
                collect(Collectors.toList()))).getContext().getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId, m -> m));

        saleGoodsVOList.forEach(saleGood -> {
            if (goodsInfoVOMap.containsKey(saleGood.getGoodsInfoId())) {
                GoodsInfoVO goodsInfoVO = goodsInfoVOMap.get(saleGood.getGoodsInfoId());
                if (Objects.nonNull(goodsInfoVO.getBrandId()) && goodsBrandVOMap.containsKey(goodsInfoVO.getBrandId())) {
                    goodsInfoVO.setBrandName(goodsBrandVOMap.get(goodsInfoVO.getBrandId()).getBrandName());
                }
                if (goodsCateVOMap.containsKey(goodsInfoVO.getCateId())) {
                    goodsInfoVO.setCateName(goodsCateVOMap.get(goodsInfoVO.getCateId()).getCateName());
                }
                //填充规格值
                goodsInfoVO.setSpecText(goodsInfoSpecDetailMap.get(goodsInfoVO.getGoodsInfoId()));
                saleGood.setGoodsInfoVO(goodsInfoVO);
            }
        });
        return saleGoodsVOList;
    }


    @ApiOperation(value = "修改预约抢购活动信息")
    @PutMapping("/modify")
    public BaseResponse<AppointmentSaleModifyResponse> modify(@RequestBody @Valid AppointmentSaleModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        validateWholeSaleGood(request.getAppointmentSaleGoods().stream().map(AppointmentSaleGoodsDTO::getGoodsInfoId).collect(Collectors.toList()), request.getStoreId());
        return appointmentSaleProvider.modify(request);
    }

    @ApiOperation(value = "根据id删除预约抢购")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return appointmentSaleProvider.deleteById(AppointmentSaleDelByIdRequest.builder().id(id).storeId(commonUtil.getStoreId()).build());
    }

    @ApiOperation(value = "根据id暂停活动/开始活动")
    @PutMapping("/status/{id}")
    public BaseResponse deleteByIdList(@RequestBody @Valid AppointmentSaleStatusRequest request) {
        AppointmentSaleVO appointmentSaleVO = appointmentSaleQueryProvider.getById(AppointmentSaleByIdRequest.builder().storeId(commonUtil.getStoreId()).id(request.getId()).build()).getContext().getAppointmentSaleVO();
        if (Objects.isNull(appointmentSaleVO)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (appointmentSaleVO.getPauseFlag().equals(request.getPauseFlag())) {
            return BaseResponse.SUCCESSFUL();
        }
        request.setStoreId(commonUtil.getStoreId());
        return appointmentSaleProvider.modifyStatus(request);
    }

   /* @ApiOperation(value = "导出预约抢购列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        AppointmentSaleListRequest listReq = JSON.parseObject(decrypted, AppointmentSaleListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        List<AppointmentSaleVO> dataRecords = appointmentSaleQueryProvider.list(listReq).getContext().getAppointmentSaleVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("预约抢购列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }*/

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<AppointmentSaleVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("活动名称", new SpelColumnRender<AppointmentSaleVO>("activityName")),
                new Column("商户id", new SpelColumnRender<AppointmentSaleVO>("storeId")),
                new Column("预约类型 0：不预约不可购买  1：不预约可购买", new SpelColumnRender<AppointmentSaleVO>("appointmentType")),
                new Column("预约开始时间", new SpelColumnRender<AppointmentSaleVO>("appointmentStartTime")),
                new Column("预约结束时间", new SpelColumnRender<AppointmentSaleVO>("appointmentEndTime")),
                new Column("抢购开始时间", new SpelColumnRender<AppointmentSaleVO>("snapUpStartTime")),
                new Column("抢购结束时间", new SpelColumnRender<AppointmentSaleVO>("snapUpEndTime")),
                new Column("发货日期 2020-01-10", new SpelColumnRender<AppointmentSaleVO>("deliverTime")),
                new Column("参加会员  -2 指定用户 -1:全部客户 0:全部等级 other:其他等级 -3：指定人群 -4：企业会员", new SpelColumnRender<AppointmentSaleVO>("joinLevel")),
                new Column("是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）", new SpelColumnRender<AppointmentSaleVO>("joinLevelType")),
                new Column("是否暂停 0:否 1:是", new SpelColumnRender<AppointmentSaleVO>("pauseFlag"))
        };
        excelHelper.addSheet("预约抢购列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
