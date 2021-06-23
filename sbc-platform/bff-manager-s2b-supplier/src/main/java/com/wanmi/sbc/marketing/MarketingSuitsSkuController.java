package com.wanmi.sbc.marketing;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.marketing.api.provider.marketingsuitssku.MarketingSuitsSkuProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuitssku.MarketingSuitsSkuQueryProvider;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.*;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.*;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;


@Api(description = "组合活动关联商品sku表管理API", tags = "MarketingSuitsSkuController")
@RestController
@RequestMapping(value = "/marketingsuitssku")
public class MarketingSuitsSkuController {

    @Autowired
    private MarketingSuitsSkuQueryProvider marketingSuitsSkuQueryProvider;

    @Autowired
    private MarketingSuitsSkuProvider marketingSuitsSkuSaveProvider;


    @ApiOperation(value = "分页查询组合活动关联商品sku表")
    @PostMapping("/page")
    public BaseResponse<MarketingSuitsSkuPageResponse> getPage(@RequestBody @Valid MarketingSuitsSkuPageRequest pageReq) {
        pageReq.putSort("id", "desc");
        return marketingSuitsSkuQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询组合活动关联商品sku表")
    @PostMapping("/list")
    public BaseResponse<MarketingSuitsSkuListResponse> getList(@RequestBody @Valid MarketingSuitsSkuListRequest listReq) {
//        listReq.putSort("id", "desc");
        return marketingSuitsSkuQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询组合活动关联商品sku表")
    @GetMapping("/{id}")
    public BaseResponse<MarketingSuitsSkuByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        MarketingSuitsSkuByIdRequest idReq = new MarketingSuitsSkuByIdRequest();
        idReq.setId(id);
        return marketingSuitsSkuQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "根据idList批量删除组合活动关联商品sku表")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsSkuDelByIdListRequest delByIdListReq) {
        return marketingSuitsSkuSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出组合活动关联商品sku表列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        MarketingSuitsSkuListRequest listReq = JSON.parseObject(decrypted, MarketingSuitsSkuListRequest.class);
//        listReq.putSort("id", "desc");
        List<MarketingSuitsSkuVO> dataRecords = marketingSuitsSkuQueryProvider.list(listReq).getContext().getMarketingSuitsSkuVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("组合活动关联商品sku表列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<MarketingSuitsSkuVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("组合id", new SpelColumnRender<MarketingSuitsSkuVO>("suitsId")),
            new Column("促销活动id", new SpelColumnRender<MarketingSuitsSkuVO>("marketingId")),
            new Column("skuId", new SpelColumnRender<MarketingSuitsSkuVO>("skuId")),
            new Column("单个优惠价格（优惠多少）", new SpelColumnRender<MarketingSuitsSkuVO>("discountPrice")),
            new Column("sku数量", new SpelColumnRender<MarketingSuitsSkuVO>("num"))
        };
        excelHelper.addSheet("组合活动关联商品sku表列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
