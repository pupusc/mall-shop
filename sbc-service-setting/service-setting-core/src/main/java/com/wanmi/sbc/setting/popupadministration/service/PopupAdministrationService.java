package com.wanmi.sbc.setting.popupadministration.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.popupadministration.*;
import com.wanmi.sbc.setting.api.response.popupadministration.PageManagementResponse;
import com.wanmi.sbc.setting.bean.vo.PopupAdministrationVO;
import com.wanmi.sbc.setting.popupadministration.model.ApplicationPage;
import com.wanmi.sbc.setting.popupadministration.model.PopupAdministration;
import com.wanmi.sbc.setting.popupadministration.repository.ApplicationPageRepository;
import com.wanmi.sbc.setting.popupadministration.repository.PopupAdministrationRepository;
import com.wanmi.sbc.setting.util.error.PopupAdministrationErrorCode;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 弹窗管理
 */
@Service
public class PopupAdministrationService {

    @Autowired
    private PopupAdministrationRepository popupAdministrationRepository;

    @Autowired
    private ApplicationPageRepository applicationPageRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 新增弹窗管理
     *
     * @param popupAdministrationRequest
     * @return
     */
    @Transactional
    public BaseResponse add(PopupAdministrationRequest popupAdministrationRequest) {
        PopupAdministration popupAdministration = new PopupAdministration();
        KsBeanUtil.copyPropertiesThird(popupAdministrationRequest, popupAdministration);
        popupAdministration.setDelFlag(DeleteFlag.NO);
        popupAdministration.setIsPause(BoolFlag.NO);
        popupAdministration.setCreateTime(LocalDateTime.now());
        if (popupAdministration.getCreatePerson() != null) {
            popupAdministration.setCreatePerson(popupAdministration.getCreatePerson());
        }
        if (CollectionUtils.isNotEmpty(popupAdministrationRequest.getApplicationPageName())) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String application : popupAdministrationRequest.getApplicationPageName()) {
                stringBuilder.append(application).append(";");
            }
            popupAdministration.setApplicationPageName(stringBuilder.toString().substring(0,
                    stringBuilder.length() - 1));
        }
        popupAdministrationRepository.save(popupAdministration);
        List<ApplicationPage> applicationList =
                popupAdministrationRequest.getApplicationPageName().stream().map(applicationpage -> {
                    ApplicationPage applicationPage = new ApplicationPage();
                    applicationPage.setApplicationPageName(applicationpage);
                    applicationPage.setPopupId(popupAdministration.getPopupId());
                    //新增设置默认排序
                    applicationPage.setSortNumber(1L);
                    return applicationPage;
                }).collect(Collectors.toList());
        applicationPageRepository.saveAll(applicationList);
        return BaseResponse.success(popupAdministration);
    }

    /**
     * 修改弹窗管理
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public PopupAdministration modify(PopupAdministrationModifyRequest request) {
        PopupAdministration popupAdministration =
                popupAdministrationRepository.findById(request.getPopupId()).orElseThrow(() -> new SbcRuntimeException(PopupAdministrationErrorCode.POPUP_MANAGEMENT_DOES_EXIST));
        popupAdministration.setPopupName(request.getPopupName());
        popupAdministration.setBeginTime(request.getBeginTime());
        popupAdministration.setEndTime(request.getEndTime());
        popupAdministration.setPopupUrl(request.getPopupUrl());
        popupAdministration.setJumpPage(request.getJumpPage());
        popupAdministration.setLaunchFrequency(request.getLaunchFrequency());
        popupAdministration.setIsPause(BoolFlag.NO);
        if (StringUtil.isBlank(request.getPopupName())) {
            popupAdministration.setUpdatePerson(request.getUpdatePerson());
        }
        if (CollectionUtils.isNotEmpty(request.getApplicationPageName())) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String application : request.getApplicationPageName()) {
                stringBuilder.append(application).append(";");
            }
            popupAdministration.setApplicationPageName(stringBuilder.toString().substring(0,
                    stringBuilder.length() - 1));
        }
        popupAdministration.setUpdateTime(LocalDateTime.now());
        popupAdministrationRepository.save(popupAdministration);

        applicationPageRepository.deleteByPopupId(popupAdministration.getPopupId());

        List<ApplicationPage> applicationList =
                request.getApplicationPageName().stream().map(applicationpage -> {
                    ApplicationPage applicationPage = new ApplicationPage();
                    applicationPage.setApplicationPageName(applicationpage);
                    applicationPage.setPopupId(popupAdministration.getPopupId());
                    //新增设置默认排序
                    applicationPage.setSortNumber(1L);
                    return applicationPage;
                }).collect(Collectors.toList());
        applicationPageRepository.saveAll(applicationList);
        return popupAdministration;
    }

    /**
     * 删除弹窗管理
     *
     * @param popupAdministrationId
     * @return
     */
    @Transactional
    public int deletePopupAdministration(Long popupAdministrationId) {
        PopupAdministration popupAdministration =
                popupAdministrationRepository.findById(popupAdministrationId).orElseThrow(() -> new SbcRuntimeException(PopupAdministrationErrorCode.POPUP_MANAGEMENT_DOES_EXIST));
        return popupAdministrationRepository.deletePopupAdministration(popupAdministrationId);
    }


    /**
     * 暂停弹窗
     *
     * @param popupAdministrationId
     * @return
     */
    @Transactional
    public int pausePopupAdministration(Long popupAdministrationId) {
        PopupAdministration popupAdministration =
                popupAdministrationRepository.findById(popupAdministrationId).orElseThrow(() -> new SbcRuntimeException(PopupAdministrationErrorCode.POPUP_MANAGEMENT_DOES_EXIST));
        return popupAdministrationRepository.pausePopupAdministration(popupAdministrationId);
    }

    /**
     * 启动弹窗
     *
     * @param popupAdministrationId
     * @return
     */
    @Transactional
    public int startPopupAdministration(Long popupAdministrationId) {
        PopupAdministration popupAdministration =
                popupAdministrationRepository.findById(popupAdministrationId).orElseThrow(() -> new SbcRuntimeException(PopupAdministrationErrorCode.POPUP_MANAGEMENT_DOES_EXIST));
        return popupAdministrationRepository.startPopupAdministration(popupAdministrationId);
    }

    /**
     * 分页&搜索查询
     *
     * @param request
     * @return
     */
    public MicroServicePage<PopupAdministrationVO> page(PopupAdministrationPageRequest request) {
        String sql = "SELECT t.* FROM popup t ";
        //查询记录的总数量
        String countSql = "SELECT count(1) count FROM popup t ";
        //条件查询
        String whereSql = "WHERE 1 = 1";

        if (StringUtils.isNotBlank(request.getPopupName())) {
            whereSql += " AND t.popup_name LIKE concat('%', :popupName, '%')";
        }

        switch (request.getQueryTab()) {
            case STARTED://进行中
                whereSql += " AND now() >= t.begin_time AND now() <= t.end_time AND t.is_pause = 0";
                break;
            case PAUSED://暂停中
                whereSql += " AND now() >= t.begin_time AND now() <= t.end_time AND t.is_pause = 1";
                break;
            case NOT_START://未开始
                whereSql += " AND now() < t.begin_time";
                break;
            case ENDED://已结束
                whereSql += " AND now() > t.end_time";
                break;
            case S_NS: // 进行中&未开始
                whereSql += " AND now() <= t.end_time AND t.is_pause = 0";
                break;
            default:
                break;
        }
        whereSql += " AND t.del_flag=0 order by t.create_time desc";
        Query query = entityManager.createNativeQuery(sql.concat(whereSql));
        //组装查询参数
        this.wrapperQueryParam(query, request);
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).addEntity("t", PopupAdministration.class);
        List<PopupAdministrationVO> responsesList =
                ((List<PopupAdministration>) query.getResultList()).stream().map(source -> {
                    PopupAdministrationVO response = new PopupAdministrationVO();
                    BeanUtils.copyProperties(source, response);
                    return response;
                }).collect(Collectors.toList());

        long count = 0;

        if (CollectionUtils.isNotEmpty(responsesList)) {
            Query queryCount = entityManager.createNativeQuery(countSql.concat(whereSql));
            //组装查询参数
            this.wrapperQueryParam(queryCount, request);
            count = Long.parseLong(queryCount.getSingleResult().toString());
        }
        return new MicroServicePage<>(responsesList, request.getPageable(), count);
    }

    /**
     * 根据id获取弹窗管理
     *
     * @param PopupAdministrationId
     * @return
     */
    public PopupAdministration getPopupAdministrationById(Long PopupAdministrationId) {
        return popupAdministrationRepository.findById(PopupAdministrationId).orElseThrow(() -> new SbcRuntimeException(PopupAdministrationErrorCode.POPUP_MANAGEMENT_DOES_EXIST));
    }

    /**
     * 根据应用名称查询对应的弹窗
     *
     * @param request
     * @return
     */
    public BaseResponse pageManagementAndPopupAdministrationList(PageManagementRequest request) {
        //查询应用页下的所有弹窗
        List<ApplicationPage> administrationList =
                applicationPageRepository.findAllByApplicationPageNameContainingOrderBySortNumberAsc(request.getApplicationPageName());
        List<PopupAdministrationVO> popupAdministrationVOList=new ArrayList<>();
        for (ApplicationPage applicationPage : administrationList) {
            PopupAdministration popupAdministration = popupAdministrationRepository.findAllByPopupIdAndDelFlag(applicationPage.getPopupId(),DeleteFlag.NO);

            if (popupAdministration!=null) {
                //未进行，进行中，暂停中的弹窗
                if (popupAdministration.getBeginTime().isAfter(LocalDateTime.now()) || LocalDateTime.now().isBefore(popupAdministration.getEndTime()) || popupAdministration.getIsPause() == BoolFlag.YES) {
                    PopupAdministrationVO popupAdministrationVO = new PopupAdministrationVO();
                    KsBeanUtil.copyPropertiesThird(popupAdministration, popupAdministrationVO);
                    popupAdministrationVOList.add(popupAdministrationVO);
                }
            }
        }
        return BaseResponse.success(new PageManagementResponse((request.getApplicationPageName()),
                popupAdministrationVOList));
    }

    /**
     * 应用页弹窗管理排序
     *
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse sortPopupAdministration(List<PopupAdministrationSortRequest> request) {
        for (PopupAdministrationSortRequest popupAdministrationSortRequest : request) {
            applicationPageRepository.sortPopupAdministration(popupAdministrationSortRequest.getApplicationPageName()
                    , popupAdministrationSortRequest.getPopupId(), popupAdministrationSortRequest.getSortNumber());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 组装查询参数
     *
     * @param query
     * @param request
     */
    private void wrapperQueryParam(Query query, PopupAdministrationPageRequest request) {
        if (StringUtils.isNotBlank(request.getPopupName())) {
            query.setParameter("popupName", request.getPopupName());
        }
    }
}
