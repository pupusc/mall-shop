package com.wanmi.sbc.crm.autotag;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagProvider;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagQueryProvider;
import com.wanmi.sbc.crm.api.provider.autotagOther.AutoTagOtherQueryProvider;
import com.wanmi.sbc.crm.api.request.autotag.*;
import com.wanmi.sbc.crm.api.request.autotagother.AutoTagOtherPageRequest;
import com.wanmi.sbc.crm.api.response.autotag.*;
import com.wanmi.sbc.crm.api.response.autotagother.AutotagOtherPageResponse;
import com.wanmi.sbc.crm.bean.enums.TagParamColumn;
import com.wanmi.sbc.crm.bean.enums.TagParamType;
import com.wanmi.sbc.crm.bean.vo.AutoTagVO;
import com.wanmi.sbc.crm.bean.vo.TagRuleParamsVO;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreNameListByStoreIdsResquest;
import com.wanmi.sbc.customer.bean.vo.StoreNameVO;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "自动标签管理API", tags = "AutoTagController")
@RestController
@RequestMapping(value = "/autotag")
public class AutoTagController {

    @Autowired
    private AutoTagQueryProvider autoTagQueryProvider;

    @Autowired
    private AutoTagProvider autoTagProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AutoTagOtherQueryProvider autoTagOtherQueryProvider;

    @ApiOperation(value = "分页查询自动标签")
    @PostMapping("/page")
    public BaseResponse<AutotagOtherPageResponse> getPage(@RequestBody @Valid AutoTagOtherPageRequest pageReq) {
       return autoTagOtherQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询自动标签")
    @PostMapping("/list")
    public BaseResponse<AutoTagListResponse> getList(@RequestBody @Valid AutoTagListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return autoTagQueryProvider.list(listReq);
    }

    @ApiOperation(value = "列表查询自动标签")
    @PostMapping("/preferenceList")
    public BaseResponse<PreferenceTagListResponse> getPreferenceList(@RequestBody @Valid AutoTagListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return autoTagQueryProvider.getPreferenceList(listReq);
    }

    @ApiOperation(value = "自动标签总数")
    @GetMapping("/count")
    public BaseResponse<Long> getCount() {
        return autoTagQueryProvider.getCount();
    }

    @ApiOperation(value = "根据id查询自动标签")
    @GetMapping("/{id}")
    public BaseResponse<AutoTagByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        AutoTagByIdRequest idReq = new AutoTagByIdRequest();
        idReq.setId(id);
        BaseResponse<AutoTagByIdResponse> response = autoTagQueryProvider.getById(idReq);
//        fillInfoName(response.getContext().getAutoTagVO());
        return response;
    }

    @ApiOperation(value = "新增自动标签")
    @PostMapping("/add")
    @MultiSubmit
    public BaseResponse<AutoTagAddResponse> add(@RequestBody @Valid AutoTagAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        return autoTagProvider.add(addReq);
    }

    @ApiOperation(value = "修改自动标签")
    @PutMapping("/modify")
    @MultiSubmit
    public BaseResponse<AutoTagModifyResponse> modify(@RequestBody @Valid AutoTagModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        return autoTagProvider.modify(modifyReq);
    }

    @ApiOperation(value = "同步标签")
    @PostMapping("/init")
    public BaseResponse init(@RequestBody @Valid AutoTagInitRequest request) {
        request.setCreatePerson(commonUtil.getOperatorId());
        return autoTagProvider.init(request);
    }

    @ApiOperation(value = "系统标签列表")
    @PostMapping("/system-list")
    public BaseResponse<AutoTagInitListResponse> systemList() {
        return autoTagQueryProvider.systemList();
    }

    @ApiOperation(value = "根据id删除自动标签")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        AutoTagDelByIdRequest delByIdReq = new AutoTagDelByIdRequest();
        delByIdReq.setId(id);
        return autoTagProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除自动标签")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid AutoTagDelByIdListRequest delByIdListReq) {
        return autoTagProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出自动标签列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        AutoTagListRequest listReq = JSON.parseObject(decrypted, AutoTagListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        List<AutoTagVO> dataRecords = autoTagQueryProvider.list(listReq).getContext().getAutoTagVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("自动标签列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<AutoTagVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("自动标签名称", new SpelColumnRender<AutoTagVO>("tagName")),
            new Column("会员人数", new SpelColumnRender<AutoTagVO>("customerCount")),
            new Column("标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签", new SpelColumnRender<AutoTagVO>("type")),
            new Column("一级维度且或关系，0：且，1：或", new SpelColumnRender<AutoTagVO>("relationType"))
        };
        excelHelper.addSheet("自动标签列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    /**
     * 填充类目、品类、品牌、商品、店铺的名称
     * @param vo
     */
    private void fillInfoName(AutoTagVO vo) {
        List<TagRuleParamsVO> params = vo.getRuleList().stream().flatMap(v -> v.getRuleParamList().stream())
                .filter(v -> CollectionUtils.isNotEmpty(v.getValues())
                        && Objects.equals(TagParamType.WHERE.toValue(), v.getType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        //商品类目提取<id,名称>
        List<Long> cateIds = params.stream()
                .filter(v -> TagParamColumn.CATE_TOP_ID.toValue().equals(v.getColumnMame())
                        || TagParamColumn.CATE_ID.toValue().equals(v.getColumnMame()))
                .flatMap(c -> c.getValues().stream()).map(Long::valueOf).collect(Collectors.toList());
        Map<Long, String> cateNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(cateIds)) {
            GoodsCateByIdsRequest request = new GoodsCateByIdsRequest();
            request.setCateIds(cateIds);
            cateNameMap.putAll(goodsCateQueryProvider.getByIds(request).getContext().getGoodsCateVOList().stream()
                    .collect(Collectors.toMap(GoodsCateVO::getCateId, GoodsCateVO::getCateName)));
        }
        //商品品牌提取<id,名称>
        List<Long> brandIds = params.stream()
                .filter(v -> TagParamColumn.BRAND_ID.toValue().equals(v.getColumnMame()))
                .flatMap(c -> c.getValues().stream()).map(Long::valueOf).collect(Collectors.toList());
        Map<Long, String> brandNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(brandIds)) {
            GoodsBrandByIdsRequest request = new GoodsBrandByIdsRequest();
            request.setBrandIds(brandIds);
            brandNameMap.putAll(goodsBrandQueryProvider.listByIds(request).getContext().getGoodsBrandVOList().stream
                    ().collect(Collectors.toMap(GoodsBrandVO::getBrandId, GoodsBrandVO::getBrandName)));
        }
        //商品提取<id,名称>
        List<String> goodsIds = params.stream()
                .filter(v -> TagParamColumn.GOODS_ID.toValue().equals(v.getColumnMame()))
                .flatMap(c -> c.getValues().stream()).collect(Collectors.toList());
        Map<String, String> spuNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            GoodsListByIdsRequest request = new GoodsListByIdsRequest();
            request.setGoodsIds(goodsIds);
            spuNameMap.putAll(goodsQueryProvider.listByIds(request).getContext().getGoodsVOList().stream()
                    .collect(Collectors.toMap(GoodsVO::getGoodsId, GoodsVO::getGoodsName)));
        }
        //店铺提取<id,名称>
        List<Long> storeIds = params.stream()
                .filter(v -> TagParamColumn.STORE_ID.toValue().equals(v.getColumnMame()))
                .flatMap(c -> c.getValues().stream()).map(Long::valueOf).collect(Collectors.toList());
        Map<Long, String> storeNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeIds)) {
            StoreNameListByStoreIdsResquest request = new StoreNameListByStoreIdsResquest();
            request.setStoreIds(storeIds);
            storeNameMap.putAll(storeQueryProvider.listStoreNameByStoreIds(request).getContext().getStoreNameList()
                    .stream().collect(Collectors.toMap(StoreNameVO::getStoreId, StoreNameVO::getStoreName)));
        }
        //填充
        final String defaultStr = "信息已被删除";
        vo.getRuleList().forEach(rule -> {
            rule.getRuleParamList().forEach(para -> {
                List<String> valueNames = null;
                if (TagParamColumn.CATE_TOP_ID.toValue().equals(para.getColumnMame())
                        || TagParamColumn.CATE_ID.toValue().equals(para.getColumnMame())) {
                    valueNames = para.getValues().stream().map(v -> cateNameMap.getOrDefault(Long.valueOf(v), defaultStr))
                            .collect(Collectors.toList());
                } else if (TagParamColumn.BRAND_ID.toValue().equals(para.getColumnMame())) {
                    valueNames = para.getValues().stream().map(v -> brandNameMap.getOrDefault(Long.valueOf(v),defaultStr))
                            .collect(Collectors.toList());
                } else if (TagParamColumn.GOODS_ID.toValue().equals(para.getColumnMame())) {
                    valueNames = para.getValues().stream().map(v -> spuNameMap.getOrDefault(v, defaultStr))
                            .collect(Collectors.toList());
                } else if (TagParamColumn.STORE_ID.toValue().equals(para.getColumnMame())) {
                    valueNames = para.getValues().stream().map(v -> storeNameMap.getOrDefault(Long.valueOf(v), defaultStr))
                            .collect(Collectors.toList());
                }
                para.setValueNames(valueNames);
            });
        });
    }

}
