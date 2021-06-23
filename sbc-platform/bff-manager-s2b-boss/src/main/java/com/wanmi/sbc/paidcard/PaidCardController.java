package com.wanmi.sbc.paidcard;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.response.paidcard.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.SynGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.SyncGoodsInfoResponse;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsErpNoRequest;
import com.wanmi.sbc.paidcard.service.PaidCardJobService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@Api(description = "付费会员管理API", tags = "PaidCardController")
@RestController
@RequestMapping(value = "/paidcard")
public class PaidCardController {

    @Autowired
    private PaidCardQueryProvider paidCardQueryProvider;

    @Autowired
    private PaidCardSaveProvider paidCardSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PaidCardJobService paidCardJobService;

    @Autowired
    private GuanyierpProvider guanyierpProvider;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @ApiOperation(value = "分页查询付费会员")
    @PostMapping("/page")
    public BaseResponse<PaidCardPageResponse> getPage(@RequestBody @Valid PaidCardPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return paidCardQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询付费会员")
    @PostMapping("/list")
    public BaseResponse<PaidCardListResponse> getList(@RequestBody @Valid PaidCardListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return paidCardQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询付费会员")
    @GetMapping("/{id}")
    public BaseResponse<PaidCardByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardByIdRequest idReq = new PaidCardByIdRequest();
        idReq.setId(id);
        return paidCardQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增付费会员")
    @PostMapping("/add")
    public BaseResponse<PaidCardAddResponse> add(@RequestBody @Valid PaidCardAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setEnable(EnableEnum.ENABLE);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());

        //同商品校验erp编码是否存在
        List<String> erpNos = addReq.getRuleList().stream().map(rule -> rule.getErpSkuCode()).filter(erpNo -> StringUtils.isNotBlank(erpNo)).collect(Collectors.toList());
        goodsQueryProvider.vaildErpNo(GoodsErpNoRequest.builder().erpGoodsNo(addReq.getErpSpuCode()).erpGoodsInfoNos(erpNos).build());

        return paidCardSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改付费会员")
    @PutMapping("/modify")
    public BaseResponse<PaidCardModifyResponse> modify(@RequestBody @Valid PaidCardModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return paidCardSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除付费会员")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardDelByIdRequest delByIdReq = new PaidCardDelByIdRequest();
        delByIdReq.setId(id);
        return paidCardSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除付费会员")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardDelByIdListRequest delByIdListReq) {
        return paidCardSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "付费会员启用禁用操作")
    @PostMapping("/change-enable-status")
    public BaseResponse changeEnableStatus(@RequestBody @Valid PaidCardEnableRequest request) {
        return paidCardSaveProvider.changeEnableStatus(request);
    }

    @ApiOperation(value = "导出付费会员列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PaidCardListRequest listReq = JSON.parseObject(decrypted, PaidCardListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<PaidCardVO> dataRecords = paidCardQueryProvider.list(listReq).getContext().getPaidCardVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("付费会员列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }
    @Value("${application.erp.version}")
    private String version;

    /**
     * 根据spu编码查询sku编码
     * @param request
     * @return
     */
    @ApiOperation(value = "根据spu编码查询sku编码")
    @PostMapping("/queryErpInfo")
    public BaseResponse queryErpInfo(@RequestBody @Valid PaidCardErpQueryRequest request) {
        BaseResponse<SyncGoodsInfoResponse> resp
                = guanyierpProvider.syncGoodsInfo(SynGoodsInfoRequest.builder()
                .spuCode(request.getSpuErpCode())
                .build());
        return resp;
    }
    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<PaidCardVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("名称", new SpelColumnRender<PaidCardVO>("name")),
            new Column("背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址", new SpelColumnRender<PaidCardVO>("background")),
            new Column("付费会员图标", new SpelColumnRender<PaidCardVO>("icon")),
            new Column("折扣率", new SpelColumnRender<PaidCardVO>("discountRate")),
            new Column("规则说明", new SpelColumnRender<PaidCardVO>("rule")),
            new Column("付费会员用户协议", new SpelColumnRender<PaidCardVO>("agreement")),
            new Column("启动禁用标识 1：启用；2：禁用", new SpelColumnRender<PaidCardVO>("enable")),
            new Column("背景类型0背景色；1背景图片", new SpelColumnRender<PaidCardVO>("bgType")),
            new Column("前景色", new SpelColumnRender<PaidCardVO>("textColor"))
        };
        excelHelper.addSheet("付费会员列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    @GetMapping("/sendWillExpireMsg")
    public BaseResponse sendWillExpireMsg() {
        paidCardJobService.sendWillExpireMsg();
        return BaseResponse.SUCCESSFUL();
    }


    @GetMapping("/sendExpireMsg")
    public BaseResponse sendExpireMsg() {
        paidCardJobService.sendExpireMsg();
        return BaseResponse.SUCCESSFUL();
    }

    @PostMapping("/resetEs")
    public BaseResponse resetEs(@RequestBody BaseQueryRequest req) {
        esCustomerDetailProvider.resetEs(req);

        return BaseResponse.SUCCESSFUL();

    }



}
