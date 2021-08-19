package com.wanmi.sbc.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.*;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleAddResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleModifyResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSalePageResponse;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsListResponse;
import com.wanmi.sbc.goods.bean.dto.BookingSaleGoodsDTO;
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


@Api(description = "预售信息管理API", tags = "BookingSaleController")
@RestController
@RequestMapping(value = "/booking/sale")
public class BookingSaleController {

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private BookingSaleProvider bookingSaleProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @ApiOperation(value = "分页查询预售信息")
    @PostMapping("/page")
    public BaseResponse<BookingSalePageResponse> getPage(@RequestBody @Valid BookingSalePageRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.putSort("createTime", "desc");
        request.setStoreId(commonUtil.getStoreId());
        request.setPlatform(Platform.SUPPLIER);
        return bookingSaleQueryProvider.page(request);
    }


    @ApiOperation(value = "根据id查询预售详情信息(编辑)")
    @GetMapping("/{id}")
    public BaseResponse<BookingSaleByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BookingSaleVO bookingSaleVO = bookingSaleQueryProvider.getById(BookingSaleByIdRequest.builder().id(id).
                storeId(commonUtil.getStoreId()).build()).getContext().getBookingSaleVO();

        List<BookingSaleGoodsVO> bookingSaleGoodsList = getBookingSaleGoodsInfo(id);

        bookingSaleVO.setBookingSaleGoodsList(bookingSaleGoodsList);
        return BaseResponse.success(BookingSaleByIdResponse.builder().bookingSaleVO(bookingSaleVO).build());
    }

    @ApiOperation(value = "根据id查询预售详情信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<BookingSaleGoodsListResponse> getBookingSaleDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.success(BookingSaleGoodsListResponse.builder().bookingSaleGoodsVOList(getBookingSaleGoodsInfo(id)).build());
    }


    /**
     * 获取活动商品详情
     *
     * @param id
     * @return
     */
    private List<BookingSaleGoodsVO> getBookingSaleGoodsInfo(Long id) {
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();

        List<BookingSaleGoodsVO> bookingSaleGoodsVOList = bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().
                bookingSaleId(id).storeId(commonUtil.getStoreId()).build()).getContext().getBookingSaleGoodsVOList();
        if (CollectionUtils.isEmpty(bookingSaleGoodsVOList)) {
            return bookingSaleGoodsVOList;
        }
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().
                goodsInfoIds(bookingSaleGoodsVOList.stream().map(BookingSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build()).getContext().getGoodsInfos();

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

        bookingSaleGoodsVOList.forEach(saleGood -> {
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
        return bookingSaleGoodsVOList;
    }


    @ApiOperation(value = "新增预售信息")
    @PostMapping("/add")
    public BaseResponse<BookingSaleAddResponse> add(@RequestBody @Valid BookingSaleAddRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.setCreatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        validateWholeSaleGood(request.getBookingSaleGoodsList().stream().map(BookingSaleGoodsDTO::getGoodsInfoId).collect(Collectors.toList()), request.getStoreId());
        List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(request.getBookingSaleGoodsList().stream().map(v -> v.getGoodsId()).collect(Collectors.toList())).build()).getContext().getGoodsVOList();
        Optional<GoodsVO> optional = goodsVOList.stream().filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType())).findFirst();
        if (optional.isPresent()) {
            throw  new SbcRuntimeException("K-080024");
        }
        return bookingSaleProvider.add(request);
    }

    @ApiOperation(value = "修改预售信息")
    @PutMapping("/modify")
    public BaseResponse<BookingSaleModifyResponse> modify(@RequestBody @Valid BookingSaleModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        validateWholeSaleGood(request.getBookingSaleGoodsList().stream().map(BookingSaleGoodsDTO::getGoodsInfoId).collect(Collectors.toList()), request.getStoreId());
        return bookingSaleProvider.modify(request);
    }

    @ApiOperation(value = "根据id删除预售信息")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return bookingSaleProvider.deleteById(BookingSaleDelByIdRequest.builder().id(id).
                storeId(commonUtil.getStoreId()).build());
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


    @ApiOperation(value = "根据id暂停活动/开始活动")
    @DeleteMapping("/status/{id}")
    public BaseResponse deleteByIdList(@RequestBody @Valid BookingSaleStatusRequest request) {
        BookingSaleVO bookingSaleVO = bookingSaleQueryProvider.getById(BookingSaleByIdRequest.builder().
                storeId(commonUtil.getStoreId()).id(request.getId()).build()).getContext().getBookingSaleVO();
        if (Objects.isNull(bookingSaleVO)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (bookingSaleVO.getPauseFlag().equals(request.getPauseFlag())) {
            return BaseResponse.SUCCESSFUL();
        }
        request.setStoreId(commonUtil.getStoreId());
        return bookingSaleProvider.modifyStatus(request);
    }


   /* @ApiOperation(value = "导出预售信息列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        BookingSaleListRequest listReq = JSON.parseObject(decrypted, BookingSaleListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
//        listReq.putSort("id", "desc");
        List<BookingSaleVO> dataRecords = bookingSaleQueryProvider.list(listReq).getContext().getBookingSaleVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("预售信息列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<BookingSaleVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("活动名称", new SpelColumnRender<BookingSaleVO>("activityName")),
                new Column("商户id", new SpelColumnRender<BookingSaleVO>("storeId")),
                new Column("预售类型 0：全款预售  1：定金预售", new SpelColumnRender<BookingSaleVO>("bookingType")),
                new Column("定金支付开始时间", new SpelColumnRender<BookingSaleVO>("handSelStartTime")),
                new Column("定金支付结束时间", new SpelColumnRender<BookingSaleVO>("handSelEndTime")),
                new Column("尾款支付开始时间", new SpelColumnRender<BookingSaleVO>("tailStartTime")),
                new Column("尾款支付结束时间", new SpelColumnRender<BookingSaleVO>("tailEndTime")),
                new Column("预售开始时间", new SpelColumnRender<BookingSaleVO>("bookingStartTime")),
                new Column("预售结束时间", new SpelColumnRender<BookingSaleVO>("bookingEndTime")),
                new Column("发货日期 2020-01-10", new SpelColumnRender<BookingSaleVO>("deliverTime")),
                new Column("参加会员  -1:全部客户 0:全部等级 other:其他等级", new SpelColumnRender<BookingSaleVO>("joinLevel")),
                new Column("是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）", new SpelColumnRender<BookingSaleVO>("joinLevelType")),
                new Column("是否暂停 0:否 1:是", new SpelColumnRender<BookingSaleVO>("pauseFlag"))
        };
        excelHelper.addSheet("预售信息列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
