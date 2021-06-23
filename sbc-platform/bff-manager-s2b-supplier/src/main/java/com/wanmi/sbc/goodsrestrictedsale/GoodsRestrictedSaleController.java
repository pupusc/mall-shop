package com.wanmi.sbc.goodsrestrictedsale;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.*;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedSaleVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "限售配置管理API", tags = "GoodsRestrictedSaleController")
@RestController
@RequestMapping(value = "/goodsrestrictedsale")
public class GoodsRestrictedSaleController {

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private GoodsRestrictedSaleSaveProvider goodsRestrictedSaleSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询限售配置")
    @PostMapping("/page")
    public BaseResponse<GoodsRestrictedSalePageResponse> getPage(@RequestBody @Valid GoodsRestrictedSalePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("restrictedId", "desc");
        pageReq.setStoreId(commonUtil.getStoreIdWithDefault());
        GoodsRestrictedSalePageResponse response = goodsRestrictedSaleQueryProvider.page(pageReq).getContext();
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "列表查询限售配置")
    @PostMapping("/list")
    public BaseResponse<GoodsRestrictedSaleListResponse> getList(@RequestBody @Valid GoodsRestrictedSaleListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("restrictedId", "desc");
        return goodsRestrictedSaleQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询限售配置")
    @GetMapping("/{restrictedId}")
    public BaseResponse<GoodsRestrictedSaleByIdResponse> getById(@PathVariable Long restrictedId) {
        if (restrictedId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsRestrictedSaleByIdRequest idReq = new GoodsRestrictedSaleByIdRequest();
        idReq.setRestrictedId(restrictedId);
        return goodsRestrictedSaleQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增限售配置")
    @PostMapping("/add")
    public BaseResponse<GoodsRestrictedSaleAddResponse> add(@RequestBody @Valid GoodsRestrictedSaleAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setStoreId(commonUtil.getStoreIdWithDefault());
        return goodsRestrictedSaleSaveProvider.addBatch(addReq);
    }

    @ApiOperation(value = "修改限售配置")
    @PutMapping("/modify")
    public BaseResponse<GoodsRestrictedSaleModifyResponse> modify(@RequestBody @Valid GoodsRestrictedSaleModifyRequest modifyReq) {
        modifyReq.setUpdateTime(LocalDateTime.now());
        return goodsRestrictedSaleSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除限售配置")
    @DeleteMapping("/{restrictedId}")
    public BaseResponse deleteById(@PathVariable Long restrictedId) {
        if (restrictedId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsRestrictedSaleDelByIdRequest delByIdReq = new GoodsRestrictedSaleDelByIdRequest();
        delByIdReq.setRestrictedId(restrictedId);
        return goodsRestrictedSaleSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除限售配置")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid GoodsRestrictedSaleDelByIdListRequest delByIdListReq) {
        return goodsRestrictedSaleSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出限售配置列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        GoodsRestrictedSaleListRequest listReq = JSON.parseObject(decrypted, GoodsRestrictedSaleListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("restrictedId", "desc");
        List<GoodsRestrictedSaleVO> dataRecords = goodsRestrictedSaleQueryProvider.list(listReq).getContext().getGoodsRestrictedSaleVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("限售配置列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<GoodsRestrictedSaleVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("店铺ID", new SpelColumnRender<GoodsRestrictedSaleVO>("storeId")),
            new Column("货品的skuId", new SpelColumnRender<GoodsRestrictedSaleVO>("goodsInfoId")),
            new Column("限售方式 0: 按订单 1：按会员", new SpelColumnRender<GoodsRestrictedSaleVO>("restrictedType")),
            new Column("是否每人限售标识 ", new SpelColumnRender<GoodsRestrictedSaleVO>("restrictedPrePersonFlag")),
            new Column("是否每单限售的标识", new SpelColumnRender<GoodsRestrictedSaleVO>("restrictedPreOrderFlag")),
            new Column("是否指定会员限售的标识", new SpelColumnRender<GoodsRestrictedSaleVO>("restrictedAssignFlag")),
            new Column("个人限售的方式(  0:终生限售  1:周期限售)", new SpelColumnRender<GoodsRestrictedSaleVO>("personRestrictedType")),
            new Column("个人限售的周期 (0:周   1:月  2:年)", new SpelColumnRender<GoodsRestrictedSaleVO>("personRestrictedCycle")),
            new Column("限售数量", new SpelColumnRender<GoodsRestrictedSaleVO>("restrictedNum")),
            new Column("起售数量", new SpelColumnRender<GoodsRestrictedSaleVO>("startSaleNum"))
        };
        excelHelper.addSheet("限售配置列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }
}
