package com.wanmi.sbc.livegoods;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.LiveErrCodeUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import com.wanmi.sbc.customer.bean.vo.StoreSimpleInfo;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.request.livegoods.*;
import com.wanmi.sbc.goods.api.response.livegoods.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.wanmi.sbc.common.exception.SbcRuntimeException;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.MiniProgramUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;


@Api(description = "直播商品管理API", tags = "LiveGoodsController")
@RestController
@RequestMapping(value = "/livegoods")
public class LiveGoodsController {

    @Autowired
    private LiveGoodsQueryProvider liveGoodsQueryProvider;

    @Autowired
    private LiveGoodsProvider liveGoodsProvider;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private MiniProgramUtil miniProgramUtil;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private StoreQueryProvider storeQueryProvider;


    @ApiOperation(value = "分页查询直播商品")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<LiveGoodsPageNewResponse> getPage(@RequestBody @Valid LiveGoodsQueryRequest pageReq) {
        //根据所属店铺名称模糊查询
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("createTime", "desc");
        if (StringUtils.isBlank(pageReq.getStoreName())){
            pageReq.setStoreId(commonUtil.getStoreId());
        }
        return liveGoodsQueryProvider.pageNew(pageReq);
//        if(StringUtils.isNotBlank(pageReq.getStoreName())){
//
//            //查询所有匹配的storeId
//           List<StoreSimpleInfo> storeSimpleInfos = storeQueryProvider.listByStoreName(new ListStoreByNameRequest(pageReq.getStoreName())).getContext().getStoreSimpleInfos();
//           Map<Long, String> storeIdAndNameMap = storeSimpleInfos.stream().filter(c -> c.getStoreId() != null).collect(Collectors.toMap(StoreSimpleInfo::getStoreId, c -> {
//               String name = c.getStoreName();
//               if (StringUtils.isEmpty(name)) {
//                   name = "-";
//               }
//               return name;
//           }, (oldValue, newValue) -> newValue));
//           //用于获取所属店铺名称
//           Map<Long, String> storeName =new HashMap<>();
//           //查询所有结果
//           LiveGoodsListRequest request = KsBeanUtil.convert(pageReq, LiveGoodsListRequest.class);
//           List<LiveGoodsVO> liveGoodsVOList = liveGoodsQueryProvider.list(request).getContext().getLiveGoodsVOList();
//           //映射
//           List<LiveGoodsVO> collect = new ArrayList<>();
//
//           for (LiveGoodsVO liveGoodsVO : liveGoodsVOList) {
//               if (storeIdAndNameMap.keySet().contains(liveGoodsVO.getStoreId())){
//                   collect.add(liveGoodsVO);
//                   //获取店铺所属名称
//                   storeName.put(liveGoodsVO.getStoreId(),storeIdAndNameMap.get(liveGoodsVO.getStoreId()));
//               }
//           }
//
//           //实时获取商品库存和goodId（非微信端商品id）
//           collect.stream().filter(Objects::nonNull).forEach(c -> {
//               if (StringUtils.isNotBlank(c.getGoodsInfoId())) {
//                   GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(c.getGoodsInfoId(), c.getStoreId())).getContext();
//                   if (Objects.nonNull(goodsInfo) && Objects.nonNull(goodsInfo.getStock() )) {
//                       c.setStock(goodsInfo.getStock());
//                   }else {
//                       c.setStock(0L);
//                   }
//                   if (Objects.nonNull(goodsInfo) && Objects.nonNull(goodsInfo.getGoodsId())) {
//                       c.setGoodsIdForDetails(goodsInfo.getGoodsId());
//                   }
//
//               }
//           });
//           //查询规格信息
//           /*Map<String, List<GoodsInfoVO>> listMap = collect.stream().filter(Objects::nonNull).collect(Collectors.toMap(LiveGoodsVO::getGoodsInfoId, c -> {
//               GoodsInfoViewPageRequest goodsInfoViewPageRequest = new GoodsInfoViewPageRequest();
//               goodsInfoViewPageRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
//               goodsInfoViewPageRequest.setGoodsId(c.getGoodsIdForDetails());
//               return goodsInfoQueryProvider.pageView(goodsInfoViewPageRequest).getContext().getGoodsInfoPage().getContent();
//           }, (oldValue, newValue) -> newValue));*/
//            List<String> goodsInfoList = collect.stream().filter(Objects::nonNull).map(LiveGoodsVO::getGoodsInfoId).collect(Collectors.toList());
//            GoodsInfoViewPageRequest goodsInfoViewPageRequest = new GoodsInfoViewPageRequest();
//            goodsInfoViewPageRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
//            goodsInfoViewPageRequest.setGoodsInfoIds(goodsInfoList);
//            List<GoodsInfoVO> goodsInfoVOList = goodsInfoQueryProvider.pageView(goodsInfoViewPageRequest).getContext().getGoodsInfoPage().getContent();
//           LiveGoodsPageResponse response = new LiveGoodsPageResponse();
//           PageImpl<LiveGoodsVO> newPage = new PageImpl<>(collect, pageReq.getPageable(),collect.size());
//           MicroServicePage<LiveGoodsVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
//           response.setLiveGoodsVOPage(microPage);
//           response.setStoreName(storeName);
//           response.setGoodsInfoList(goodsInfoVOList);
//           return BaseResponse.success(response);
//       }else {
//            Long storeId = commonUtil.getStoreId();
//           pageReq.setStoreId(storeId);
//           MicroServicePage<LiveGoodsVO> liveGoodsVOPage = liveGoodsQueryProvider.page(pageReq).getContext().getLiveGoodsVOPage();
//           //实时获取商品库存和goodId（非微信端商品id）
//           liveGoodsVOPage.getContent().stream().forEach(c -> {
//               if (StringUtils.isNotBlank(c.getGoodsInfoId())) {
//                   GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(c.getGoodsInfoId(), c.getStoreId())).getContext();
//                   if (Objects.nonNull(goodsInfo) && Objects.nonNull(goodsInfo.getStock() )) {
//                       c.setStock(goodsInfo.getStock());
//                   }else {
//                       c.setStock(0L);
//                   }
//                   if (Objects.nonNull(goodsInfo) && Objects.nonNull(goodsInfo.getGoodsId())) {
//                       c.setGoodsIdForDetails(goodsInfo.getGoodsId());
//                   }
//
//               }
//           });
//
//           //根据storeId查询店铺名称
//           Map<Long, String> storeName = liveGoodsVOPage.getContent().stream().filter(liveGoodsVO -> liveGoodsVO.getStoreId() != null).collect(Collectors.toMap(LiveGoodsVO::getStoreId, c -> {
//                       String name = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(c.getStoreId())).getContext().getStoreVO().getStoreName();
//                       if (StringUtils.isEmpty(name)) {
//                           name = "-";
//                       }
//                       return name;
//                   }, (oldValue, newValue) -> newValue)
//           );
//
//           //查询规格信息
//          /* Map<String, List<GoodsInfoVO>> listMap =  liveGoodsVOPage.getContent().stream().filter(Objects::nonNull).collect(Collectors.toMap(LiveGoodsVO::getGoodsInfoId, c -> {
//               GoodsInfoViewPageRequest goodsInfoViewPageRequest = new GoodsInfoViewPageRequest();
//               goodsInfoViewPageRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
//               goodsInfoViewPageRequest.setGoodsId(c.getGoodsIdForDetails());
//               return goodsInfoQueryProvider.pageView(goodsInfoViewPageRequest).getContext().getGoodsInfoPage().getContent();
//           }, (oldValue, newValue) -> newValue));*/
//           List<String> goodsInfoList = liveGoodsVOPage.getContent().stream().filter(Objects::nonNull).map(LiveGoodsVO::getGoodsInfoId).collect(Collectors.toList());
//           GoodsInfoViewPageRequest goodsInfoViewPageRequest = new GoodsInfoViewPageRequest();
//           goodsInfoViewPageRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
//           goodsInfoViewPageRequest.setGoodsInfoIds(goodsInfoList);
//            List<GoodsInfoVO> goodsInfoVOList = goodsInfoQueryProvider.pageView(goodsInfoViewPageRequest).getContext().getGoodsInfoPage().getContent();
//            LiveGoodsPageResponse response = new LiveGoodsPageResponse();
//           response.setLiveGoodsVOPage(liveGoodsVOPage);
//           response.setStoreName(storeName);
//           response.setGoodsInfoList(goodsInfoVOList);
//           return BaseResponse.success(response);
//       }
    }

    @ApiOperation(value = "列表查询直播商品")
    @PostMapping("/list")
    public BaseResponse<LiveGoodsListResponse> getList(@RequestBody @Valid LiveGoodsListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("createTime", "desc");
        return liveGoodsQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据goodid查询直播商品详情")
    @RequestMapping(value = "/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<LiveGoodsByIdResponse> getById(@PathVariable Long goodsId) {
        //判断直播开关开关是否开启
        this.isOpen();
        if (goodsId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveGoodsByIdResponse response = new LiveGoodsByIdResponse();
        LiveGoodsByIdRequest idReq = new LiveGoodsByIdRequest();
        idReq.setGoodsId(goodsId);
        LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(idReq).getContext().getLiveGoodsVO();
        //根据goodsInfoId查询商品详情
        if (StringUtils.isNotBlank(liveGoodsVO.getGoodsInfoId())) {
            GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(liveGoodsVO.getGoodsInfoId(), liveGoodsVO.getStoreId())).getContext();
            response.setGoodsInfoVO(goodsInfo);
        }

        response.setLiveGoodsVO(liveGoodsVO);

        return BaseResponse.success(response);
    }

    @ApiOperation(value = "直播间添加直播商品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<LiveGoodsAddResponse> add(@RequestBody @Valid LiveGoodsAddRequest addReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        //获取accessToken
        String accessToken = miniProgramUtil.getToken();
        addReq.setAccessToken(accessToken);
        return liveGoodsProvider.add(addReq);
    }

    @ApiOperation(value = "supplier端添加直播商品")
    @RequestMapping(value = "/supplier", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<LiveGoodsSupplierAddResponse> supplier(@RequestBody @Valid LiveGoodsSupplierAddRequest supplierAddReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        return liveGoodsProvider.supplier(supplierAddReq);
    }

    @ApiOperation(value = "修改直播商品")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse<LiveGoodsModifyResponse> modify(@RequestBody @Valid LiveGoodsModifyRequest modifyReq) {
        modifyReq.setUpdateTime(LocalDateTime.now());

        return liveGoodsProvider.modify(modifyReq);
    }

    @ApiOperation(value = "修改直播商品(例如：驳回)")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public BaseResponse<LiveGoodsModifyResponse> status(@RequestBody @Valid LiveGoodsUpdateRequest updateRequest) {
        updateRequest.setUpdateTime(LocalDateTime.now());
        updateRequest.setDelFlag(DeleteFlag.NO);
        return liveGoodsProvider.status(updateRequest);
    }

    @ApiOperation(value = "根据id删除直播商品（微信接口端）")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public BaseResponse deleteByGoodsId(@RequestBody @Valid LiveGoodsDelByIdRequest delByIdReq) {
        if (delByIdReq.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //获取accessToken
        String accessToken = miniProgramUtil.getToken();
        delByIdReq.setAccessToken(accessToken);
        return liveGoodsProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除直播商品")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid LiveGoodsDelByIdListRequest delByIdListReq) {
        return liveGoodsProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出直播商品列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        LiveGoodsListRequest listReq = JSON.parseObject(decrypted, LiveGoodsListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        // listReq.portSort("goodsId", "desc");
        List<LiveGoodsVO> dataRecords = liveGoodsQueryProvider.list(listReq).getContext().getLiveGoodsVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("直播商品列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<LiveGoodsVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("商品标题", new SpelColumnRender<LiveGoodsVO>("name")),
                new Column("填入mediaID", new SpelColumnRender<LiveGoodsVO>("coverImgUrl")),
                new Column("价格类型，1：一口价，2：价格区间，3：显示折扣价", new SpelColumnRender<LiveGoodsVO>("priceType")),
                new Column("直播商品价格左边界", new SpelColumnRender<LiveGoodsVO>("price")),
                new Column("直播商品价格右边界", new SpelColumnRender<LiveGoodsVO>("price2")),
                new Column("商品详情页的小程序路径", new SpelColumnRender<LiveGoodsVO>("url")),
                new Column("库存", new SpelColumnRender<LiveGoodsVO>("stock")),
                new Column("商品详情id", new SpelColumnRender<LiveGoodsVO>("goodsInfoId")),
                new Column("店铺标识", new SpelColumnRender<LiveGoodsVO>("storeId")),
                new Column("提交审核时间", new SpelColumnRender<LiveGoodsVO>("submitTime")),
                new Column("审核单ID", new SpelColumnRender<LiveGoodsVO>("auditId")),
                new Column("审核状态,0:未审核1 审核通过2审核失败3禁用中", new SpelColumnRender<LiveGoodsVO>("auditStatus")),
                new Column("审核原因", new SpelColumnRender<LiveGoodsVO>("auditReason")),
                new Column("删除时间", new SpelColumnRender<LiveGoodsVO>("deleteTime")),
                new Column("删除人", new SpelColumnRender<LiveGoodsVO>("deletePerson"))
        };
        excelHelper.addSheet("直播商品列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }


    @ApiOperation(value = "直播商品提审")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse audit(@RequestBody @Valid LiveGoodsAuditRequest auditReq) {
        List<LiveGoodsVO> goodsInfoVOList = auditReq.getGoodsInfoVOList();
        goodsInfoVOList.stream().forEach(c -> {
            /*if (c.getCoverImgUrl()==null) {
                //获取直播商品详情（图片）
                GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(c.getGoodsInfoId(), c.getStoreId())).getContext();
                c.setCoverImgUrl(goodsInfo.getGoodsInfoImg());
            }*/
            c.setSubmitTime(LocalDateTime.now());
        });
        //获取accessToken
        String accessToken = miniProgramUtil.getToken();
        auditReq.setAccessToken(accessToken);
        return liveGoodsProvider.audit(auditReq);
    }

    /**
     * 判断直播开关是否开启
     */
    public void isOpen() {
        ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
        configQueryRequest.setDelFlag(0);
        configQueryRequest.setConfigKey("liveSwitch");
        configQueryRequest.setConfigType("liveSwitch");
        Integer status = systemConfigQueryProvider.findByConfigKeyAndDelFlag(configQueryRequest).getContext().getConfigVOList().get(0).getStatus();
        if (status == 0) {
            throw new SbcRuntimeException("10001", LiveErrCodeUtil.getErrCodeMessage(10001));
        }
    }

}
