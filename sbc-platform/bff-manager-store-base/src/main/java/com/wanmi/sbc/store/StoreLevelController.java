package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelSaveProvider;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListByConditionRequest;
import com.wanmi.sbc.customer.api.request.storelevel.*;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelWithRightsResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelAddResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelListResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelModifyResponse;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 商铺会员等级
 *
 * @author yang
 * @since 2019/2/28
 */
@Api(description = "商铺会员等级", tags = "StoreLevelController")
@RestController
@RequestMapping("/store/storeLevel")
public class StoreLevelController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private StoreLevelSaveProvider storeLevelSaveProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 列表查询商铺会员等级
     *
     * @return
     */
    @ApiOperation(value = "列表查询商铺会员等级")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResponse<StoreLevelListResponse> list() {
        Long storeId = commonUtil.getStoreId();
        StoreLevelListRequest request = StoreLevelListRequest.builder().storeId(commonUtil.getStoreId()).build();
        return storeLevelQueryProvider.listAllStoreLevelByStoreId(request);
    }

    /**
     * 分页查询平台会员等级
     *
     * @return
     */
    @ApiOperation(value = "分页查询平台会员等级")
    @RequestMapping(value = "/listBoss", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelWithRightsResponse> listBoss() {
        return customerLevelQueryProvider.listCustomerLevelRightsInfo();
    }

    /**
     * 新增商铺等级
     *
     * @param storeLevelAddRequest
     * @return
     */
    @ApiOperation(value = "保存商铺会员等级")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<StoreLevelAddResponse> add(@Valid @RequestBody StoreLevelAddRequest storeLevelAddRequest) {
        // 验证参数
        String result = checkLevelName(null, storeLevelAddRequest.getLevelName(), storeLevelAddRequest.getDiscountRate(),
                storeLevelAddRequest.getAmountConditions(), storeLevelAddRequest.getOrderConditions());
        if (result != null) {
            return BaseResponse.error(result);
        }
        storeLevelAddRequest.setStoreId(commonUtil.getStoreId());
        storeLevelAddRequest.setCreatePerson(commonUtil.getOperatorId());
        storeLevelAddRequest.setCreateTime(LocalDateTime.now());
        operateLogMQUtil.convertAndSend("商家", "新增会员等级",
                "新增会员等级：" + storeLevelAddRequest.getLevelName());

        return storeLevelSaveProvider.add(storeLevelAddRequest);
    }

    /**
     * 修改商铺会员等级
     *
     * @param storeLevelModifyRequest
     * @return
     */
    @ApiOperation(value = "修改商铺会员等级")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse<StoreLevelModifyResponse> modify(@Valid @RequestBody StoreLevelModifyRequest storeLevelModifyRequest) {
        // 验证参数
        String result = checkLevelName(storeLevelModifyRequest.getStoreLevelId(), storeLevelModifyRequest.getLevelName(), storeLevelModifyRequest.getDiscountRate(),
                storeLevelModifyRequest.getAmountConditions(), storeLevelModifyRequest.getOrderConditions());
        if (result != null) {
            return BaseResponse.error(result);
        }
        storeLevelModifyRequest.setUpdatePerson(commonUtil.getOperatorId());
        storeLevelModifyRequest.setUpdateTime(LocalDateTime.now());
        operateLogMQUtil.convertAndSend("商家", "修改会员等级",
                "修改会员等级：" + storeLevelModifyRequest.getLevelName());

        return storeLevelSaveProvider.modify(storeLevelModifyRequest);
    }

    /**
     * 批量保存会员等级
     *
     * @param storeLevelListModifyRequest
     * @return
     */
    @ApiOperation(value = "批量保存会员等级")
    @RequestMapping(value = "/modifyList", method = RequestMethod.PUT)
    public BaseResponse modifyList(@RequestBody @Valid StoreLevelListModifyRequest storeLevelListModifyRequest) {
        if (CollectionUtils.isNotEmpty(storeLevelListModifyRequest.getStoreLevelVOList())) {
            storeLevelListModifyRequest.getStoreLevelVOList().forEach(storeLevelVO -> {
                storeLevelVO.setUpdatePerson(commonUtil.getOperatorId());
                storeLevelVO.setUpdateTime(LocalDateTime.now());
            });
            storeLevelSaveProvider.modifyList(storeLevelListModifyRequest);
            operateLogMQUtil.convertAndSend("商家", "批量修改店铺等级", "批量修改店铺等级");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除商铺会员等级
     *
     * @param storeLevelId
     * @return
     */
    @ApiOperation(value = "删除商铺会员等级")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeLevelId", value = "店铺等级Id",
            required = true)
    @RequestMapping(value = "/deleteById/{storeLevelId}", method = RequestMethod.DELETE)
    public BaseResponse deleteById(@PathVariable("storeLevelId") Long storeLevelId) {
        // 判断是否有会员是该等级
        StoreCustomerRelaListByConditionRequest conditionRequest = new StoreCustomerRelaListByConditionRequest();
        conditionRequest.setStoreLevelId(storeLevelId);
        List<StoreCustomerRelaVO> relaVOList = storeCustomerQueryProvider.listByCondition(conditionRequest).getContext().getRelaVOList();
        if (relaVOList != null && relaVOList.size() > 0) {
            return BaseResponse.error("已有会员达到该等级，不可删除");
        }
        // 判断是否删除的最高等级
        StoreLevelListRequest request = StoreLevelListRequest.builder().storeId(commonUtil.getStoreId()).build();
        List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider.listAllStoreLevelByStoreId(request).getContext().getStoreLevelVOList();
        if (storeLevelVOList != null && storeLevelVOList.size() > 0) {
            if (storeLevelVOList.size() == 1) {
                return BaseResponse.error("默认等级无法删除");
            }
            StoreLevelVO storeLevelVO = storeLevelVOList.get(storeLevelVOList.size() - 1);
            if (!storeLevelVO.getStoreLevelId().equals(storeLevelId)) {
                return BaseResponse.error("只能删除最高等级");
            }
        }
        StoreLevelByIdRequest storeLevelByIdRequest = new StoreLevelByIdRequest();
        storeLevelByIdRequest.setStoreLevelId(storeLevelId);
        StoreLevelVO storeLevelVO = storeLevelQueryProvider.getById(storeLevelByIdRequest).getContext().getStoreLevelVO();
        operateLogMQUtil.convertAndSend("商家", "删除会员等级",
                "删除会员等级：" + storeLevelVO.getLevelName());

        return storeLevelSaveProvider.deleteById(StoreLevelDelByIdRequest.builder().storeLevelId(storeLevelId).build());
    }

    /**
     * 验证参数
     *
     * @param storeLevelId
     * @param levelName
     * @param discountRate
     * @param amountConditions
     * @param orderConditions
     * @return
     */
    private String checkLevelName(Long storeLevelId, String levelName, BigDecimal discountRate, BigDecimal amountConditions, Integer orderConditions) {
        // 等级名称验证
        int length = levelName.length();
        if (!(length >= 1 && length <= 10)) {
            return "等级名称仅限1-10位字符";
        }
        List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider.getByStoreIdAndLevelName(
                StoreLevelByStoreIdAndLevelNameRequest.builder()
                        .levelName(levelName)
                        .storeId(commonUtil.getStoreId())
                        .build()
        ).getContext().getStoreLevelVOList();
        if (storeLevelVOList.size() > 0) {
            StoreLevelVO storeLevelVO = storeLevelVOList.get(0);
            // 新建有相同名称或者修改除该等级外有相同名称
            if (Objects.isNull(storeLevelId)
                    || !(storeLevelVO.getStoreLevelId().equals(storeLevelId))) {
                return "等级名称不可重复";
            }
        }
        StoreLevelListRequest request = StoreLevelListRequest.builder().storeId(commonUtil.getStoreId()).build();
        List<StoreLevelVO> storeLevelVOS = storeLevelQueryProvider.listAllStoreLevelByStoreId(request)
                .getContext().getStoreLevelVOList();
        // 验证客户等级数量
        if (storeLevelId == null) {
            if (storeLevelVOS.size() >= 10) {
                return "最多支持10个客户等级";
            }
        }

        // 验证折扣率
        if (!(discountRate.compareTo(BigDecimal.valueOf(0)) > 0)
                || discountRate.compareTo(BigDecimal.valueOf(1)) > 0)

        {
            return "请输入0-1（不包含0）之间的数字，精确到小数点后两位";
        }

        BigDecimal result = discountRate.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (!(result.compareTo(discountRate) == 0))

        {
            return "请输入0-1（不包含0）之间的数字，精确到小数点后两位";
        }

        // 验证升级条件
        if (amountConditions == null && orderConditions == null) {
            return "至少填写一项升级条件";
        }
        if (storeLevelId == null) {
            if (amountConditions != null) {
                StoreLevelVO storeLevelAmountVO = storeLevelVOS.stream()
                        .filter(storeLevelVO -> storeLevelVO.getAmountConditions() != null)
                        .max(Comparator.comparing(StoreLevelVO::getAmountConditions))
                        .orElse(null);
                if (storeLevelAmountVO != null && !(amountConditions.compareTo(storeLevelAmountVO.getAmountConditions()) > 0)) {
                    return "当前数值应大于低等级金额";
                }
            }
            if (orderConditions != null) {
                StoreLevelVO storeLevelOrderVO = storeLevelVOS.stream()
                        .filter(storeLevelVO -> storeLevelVO.getOrderConditions() != null)
                        .max(Comparator.comparing(StoreLevelVO::getOrderConditions))
                        .orElse(null);
                if (storeLevelOrderVO != null && !(orderConditions.compareTo(storeLevelOrderVO.getOrderConditions()) > 0)) {
                    return "当前数值应大于低等级订单笔数";
                }
            }
        } else {
            if (amountConditions != null) {
                for (int i = 0; i < storeLevelVOS.size(); i++) {
                    if (storeLevelVOS.get(i).getStoreLevelId().equals(storeLevelId)) {
                        if (i != 0) {
                            List<StoreLevelVO> amountStoreLevelList = storeLevelVOS.subList(0, i);
                            StoreLevelVO storeLevelAmountVO = amountStoreLevelList.stream()
                                    .filter(storeLevelVO -> storeLevelVO.getAmountConditions() != null)
                                    .max(Comparator.comparing(StoreLevelVO::getAmountConditions))
                                    .orElse(null);
                            if (storeLevelAmountVO != null && !(amountConditions.compareTo(storeLevelAmountVO.getAmountConditions()) > 0)) {
                                return "当前数值应大于低等级金额";
                            }
                        }
                        if (i != 0 && i < storeLevelVOS.size() - 1) {
                            List<StoreLevelVO> amountStoreLevelList = storeLevelVOS.subList(i + 1, storeLevelVOS.size());
                            StoreLevelVO storeLevelAmountVO = amountStoreLevelList.stream()
                                    .filter(storeLevelVO -> storeLevelVO.getAmountConditions() != null)
                                    .min(Comparator.comparing(StoreLevelVO::getAmountConditions))
                                    .orElse(null);
                            if (storeLevelAmountVO != null && !(amountConditions.compareTo(storeLevelAmountVO.getAmountConditions()) < 0)) {
                                return "当前数值应小于高等级金额";
                            }
                        }
                    }
                }
            }
            if (orderConditions != null) {
                for (int i = 0; i < storeLevelVOS.size(); i++) {
                    if (storeLevelVOS.get(i).getStoreLevelId().equals(storeLevelId)) {
                        if (i != 0) {
                            List<StoreLevelVO> orderStoreLevelList = storeLevelVOS.subList(0, i);
                            StoreLevelVO orderLevelAmountVO = orderStoreLevelList.stream()
                                    .filter(storeLevelVO -> storeLevelVO.getOrderConditions() != null)
                                    .max(Comparator.comparing(StoreLevelVO::getOrderConditions))
                                    .orElse(null);
                            if (orderLevelAmountVO != null && !(orderConditions.compareTo(orderLevelAmountVO.getOrderConditions()) > 0)) {
                                return "当前数值应大于低等级订单笔数";
                            }
                        }
                        if (i != 0 && i < storeLevelVOS.size() - 1) {
                            List<StoreLevelVO> orderStoreLevelList = storeLevelVOS.subList(i + 1, storeLevelVOS.size());
                            StoreLevelVO orderLevelAmountVO = orderStoreLevelList.stream()
                                    .filter(storeLevelVO -> storeLevelVO.getOrderConditions() != null)
                                    .min(Comparator.comparing(StoreLevelVO::getOrderConditions))
                                    .orElse(null);
                            if (orderLevelAmountVO != null && !(orderConditions.compareTo(orderLevelAmountVO.getOrderConditions()) < 0)) {
                                return "当前数值应小于高等级订单笔数";
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
