package com.wanmi.sbc.marketing;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.request.marketingsuits.*;
import com.wanmi.sbc.marketing.api.response.marketingsuits.*;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsVO;
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


@Api(description = "组合商品主表管理API", tags = "MarketingSuitsController")
@RestController
@RequestMapping(value = "/marketingsuits")
public class MarketingSuitsController {

    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;

    @Autowired
    private MarketingSuitsProvider marketingSuitsSaveProvider;

    @ApiOperation(value = "分页查询组合商品主表")
    @PostMapping("/page")
    public BaseResponse<MarketingSuitsPageResponse> getPage(@RequestBody @Valid MarketingSuitsPageRequest pageReq) {
        pageReq.putSort("id", "desc");
        return marketingSuitsQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询组合商品主表")
    @PostMapping("/list")
    public BaseResponse<MarketingSuitsListResponse> getList(@RequestBody @Valid MarketingSuitsListRequest listReq) {
//        listReq.putSort("id", "desc");
        return marketingSuitsQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询组合商品主表")
    @GetMapping("/{id}")
    public BaseResponse<MarketingSuitsByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        MarketingSuitsByIdRequest idReq = new MarketingSuitsByIdRequest();
        idReq.setId(id);
        return marketingSuitsQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "根据idList批量删除组合商品主表")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsDelByIdListRequest delByIdListReq) {
        return marketingSuitsSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出组合商品主表列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        MarketingSuitsListRequest listReq = JSON.parseObject(decrypted, MarketingSuitsListRequest.class);
//        listReq.putSort("id", "desc");
        List<MarketingSuitsVO> dataRecords = marketingSuitsQueryProvider.list(listReq).getContext().getMarketingSuitsVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("组合商品主表列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<MarketingSuitsVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("促销id", new SpelColumnRender<MarketingSuitsVO>("marketingId")),
            new Column("套餐主图（图片url全路径）", new SpelColumnRender<MarketingSuitsVO>("mainImage")),
            new Column("套餐价格", new SpelColumnRender<MarketingSuitsVO>("suitsPrice"))
        };
        excelHelper.addSheet("组合商品主表列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
