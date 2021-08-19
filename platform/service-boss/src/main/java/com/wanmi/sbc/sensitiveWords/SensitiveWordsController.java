package com.wanmi.sbc.sensitiveWords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.sensitivewords.EsSensitiveWordsProvider;
import com.wanmi.sbc.elastic.api.provider.sensitivewords.EsSensitiveWordsQueryProvider;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsSaveRequest;
import com.wanmi.sbc.elastic.api.response.sensitivewords.EsSensitiveWordsPageResponse;
import com.wanmi.sbc.elastic.bean.vo.sensitivewords.EsSensitiveWordsVO;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsQueryProvider;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsSaveProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.SensitiveWordsByIdResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsListResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsPageResponse;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 敏感词管理
 * Created by wjj
 */
@RestController
@RequestMapping("/sensitiveWords")
public class SensitiveWordsController {

    @Autowired
    private SensitiveWordsQueryProvider queryProvider;

    @Autowired
    private EsSensitiveWordsQueryProvider esSensitiveWordsQueryProvider;

    @Autowired
    private EsSensitiveWordsProvider esSensitiveWordsProvider;

    @Autowired
    private SensitiveWordsSaveProvider saveProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 分页查询敏感词
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/sensitiveWordsList", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<EsSensitiveWordsVO>> findSensitiveWordsList(@RequestBody
                                                                                             SensitiveWordsQueryRequest
                                                                                             queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO);
        /*queryRequest.putSort("createTime", "desc");
        queryRequest.putSort("sensitiveId", "desc");
        BaseResponse<SensitiveWordsPageResponse> response = queryProvider.page(queryRequest);*/
        EsSensitiveWordsQueryRequest req = EsSensitiveWordsQueryRequest.builder().build();
        BeanUtils.copyProperties(queryRequest, req);
        BaseResponse<EsSensitiveWordsPageResponse> response = esSensitiveWordsQueryProvider.page(req);
        if (Objects.isNull(response.getContext())) {
            return null;
        }
        return BaseResponse.success(response.getContext().getSensitiveWordsVOPage());
    }

    /**
     * 新增敏感词
     * 多行文本同时添加(换行分隔),若重复则更新添加时间
     */
    @ApiOperation(value = "新增敏感词")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<Integer> add(@Valid @RequestBody SensitiveWordsAddRequest addRequest) {
        addRequest.setCreateTime(LocalDateTime.now());
        addRequest.setCreateUser(commonUtil.getOperator().getName());
        addRequest.setDelFlag(DeleteFlag.NO);


        BaseResponse<SensitiveWordsSaveResponse> response = saveProvider.add(addRequest);

        //数据同步es
        List<SensitiveWordsVO> sensitiveWordsList = response.getContext().getSensitiveWordsList();
        if (CollectionUtils.isNotEmpty(sensitiveWordsList)) {
            List<EsSensitiveWordsVO> newList = KsBeanUtil.convert(sensitiveWordsList, EsSensitiveWordsVO.class);
            EsSensitiveWordsSaveRequest saveRequest = EsSensitiveWordsSaveRequest.builder().sensitiveWordsList(newList).build();
            esSensitiveWordsProvider.addSensitiveWords(saveRequest);
        }

        operateLogMQUtil.convertAndSend("设置", "新增敏感词",
                "新增敏感词：" + addRequest.getSensitiveWords());
        return BaseResponse.success(response.getContext().getCount());
    }

    /**
     * 编辑敏感词
     *
     * @param modifyRequest
     * @return
     */
    @ApiOperation(value = "编辑敏感词")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse<Long>> edit(@Valid @RequestBody SensitiveWordsModifyRequest modifyRequest) {
        SensitiveWordsListRequest request = new SensitiveWordsListRequest();
        request.setSensitiveWords(modifyRequest.getSensitiveWords());
        request.setDelFlag(DeleteFlag.NO);
        //根据名称查询敏感词
        SensitiveWordsListResponse voList = queryProvider.list(request).getContext();
        //编辑的时候要出去自己
        if (Objects.nonNull(voList) && Objects.nonNull(voList.getSensitiveWordsVOList()) && voList
                .getSensitiveWordsVOList().size() > 0) {
            if (voList.getSensitiveWordsVOList().size() == 1) {
                if (!modifyRequest.getSensitiveId().equals(voList.getSensitiveWordsVOList
                        ().get(0).getSensitiveId())) {
                    return ResponseEntity.ok(BaseResponse.error("敏感词不可重复"));
                }
            } else {
                return ResponseEntity.ok(BaseResponse.error("敏感词不可重复"));
            }
        }
        modifyRequest.setUpdateTime(LocalDateTime.now());
        modifyRequest.setUpdateUser(commonUtil.getOperator().getName());
        SensitiveWordsVO sensitiveWordsVO = saveProvider.modify(modifyRequest).getContext().getSensitiveWordsVO();
        //同步es
        EsSensitiveWordsVO obj = KsBeanUtil.convert(sensitiveWordsVO, EsSensitiveWordsVO.class);
        EsSensitiveWordsSaveRequest saveRequest = EsSensitiveWordsSaveRequest.builder()
                .sensitiveWordsList(Collections.singletonList(obj))
                .build();
        esSensitiveWordsProvider.addSensitiveWords(saveRequest);

        operateLogMQUtil.convertAndSend("设置", "编辑敏感词",
                "编辑敏感词：" + modifyRequest.getSensitiveWords());
        return ResponseEntity.ok(BaseResponse.success(sensitiveWordsVO.getSensitiveId()));
    }

    /**
     * 删除敏感词
     *
     * @param sensitiveId
     * @return
     */
    @ApiOperation(value = "删除敏感词")
    @RequestMapping(value = "/delete/{sensitiveId}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse<Long>> delete(@PathVariable Long sensitiveId) {
        SensitiveWordsByIdRequest request = new SensitiveWordsByIdRequest();
        request.setSensitiveId(sensitiveId);
        SensitiveWordsByIdResponse response = queryProvider.getById(request).getContext();
        if (Objects.isNull(response) || DeleteFlag.YES.compareTo(response.getSensitiveWordsVO().getDelFlag()) == 0) {
            return ResponseEntity.ok(BaseResponse.error("敏感词不存在"));
        }
        SensitiveWordsModifyRequest modifyRequest = new SensitiveWordsModifyRequest();
        BeanUtils.copyProperties(response.getSensitiveWordsVO(), modifyRequest);
        modifyRequest.setDeleteTime(LocalDateTime.now());
        modifyRequest.setDeleteUser(commonUtil.getOperator().getName());
        modifyRequest.setDelFlag(DeleteFlag.YES);
        SensitiveWordsVO sensitiveWordsVO = saveProvider.modify(modifyRequest).getContext().getSensitiveWordsVO();

        EsSensitiveWordsVO obj = KsBeanUtil.convert(sensitiveWordsVO, EsSensitiveWordsVO.class);
        EsSensitiveWordsSaveRequest saveRequest = EsSensitiveWordsSaveRequest.builder()
                .sensitiveWordsList(Collections.singletonList(obj))
                .build();
        esSensitiveWordsProvider.addSensitiveWords(saveRequest);

        operateLogMQUtil.convertAndSend("设置", "删除敏感词",
                "删除敏感词：" + modifyRequest.getSensitiveWords());
        return ResponseEntity.ok(BaseResponse.success(sensitiveWordsVO.getSensitiveId()));
    }

    /**
     * 批量删除敏感词
     *
     * @param delByIdListRequest
     * @return
     */
    @ApiOperation(value = "批量删除敏感词")
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> deleteByIds(@Valid @RequestBody SensitiveWordsDelByIdListRequest delByIdListRequest) {
        delByIdListRequest.setDeleteTime(LocalDateTime.now());
        delByIdListRequest.setDeleteUser(commonUtil.getOperator().getName());
        delByIdListRequest.setDelFlag(DeleteFlag.YES);

        //同步改变es
        BaseResponse<SensitiveWordsDeleteResponse> response = saveProvider.deleteByIdList(delByIdListRequest);
        List<SensitiveWordsVO> sensitiveWordsList = response.getContext().getSensitiveWordsList();
        if (CollectionUtils.isNotEmpty(sensitiveWordsList)) {
            List<EsSensitiveWordsVO> newList = KsBeanUtil.convert(sensitiveWordsList, EsSensitiveWordsVO.class);
            EsSensitiveWordsSaveRequest saveRequest = EsSensitiveWordsSaveRequest.builder()
                    .sensitiveWordsList(newList)
                    .build();
            esSensitiveWordsProvider.addSensitiveWords(saveRequest);
        }
        operateLogMQUtil.convertAndSend("设置", "批量删除敏感词",
                "删除敏感词ids：" + delByIdListRequest.getSensitiveIdList());

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
//    @ApiOperation(value = "根据id获取敏感词")
//    @RequestMapping(value = "/sensitiveWordsById/{sensitiveId}", method = RequestMethod.GET)
//    public BaseResponse<SensitiveWordsByIdResponse> items(@PathVariable Long sensitiveId) {
//        SensitiveWordsByIdRequest request = new SensitiveWordsByIdRequest();
//        request.setSensitiveId(sensitiveId);
//        BaseResponse<SensitiveWordsByIdResponse> response = queryProvider.getById(request);
//        return response;
//    }

    private void addSensitiveWords(){

    }


    @ApiOperation(value = "拦截器跳转")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> test() {
        throw new SbcRuntimeException(CommonErrorCode.EMPLOYEE_DISABLE);
    }
}
