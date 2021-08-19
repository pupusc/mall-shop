package com.wanmi.sbc.setting.page.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.page.model.root.MagicPage;
import com.wanmi.sbc.setting.page.repository.MagicPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>查询缓存在数据库中的页面dom</p>
 *
 * @author lq
 */
@Service("magicPageService")
public class MagicPageService {

    @Autowired
    private MagicPageRepository magicPageRepository;

    /**
     * 新增
     *
     * @author lq
     */
    @Transactional
    public MagicPage add(String htmlString, String operatePerson) {
        MagicPage mainPage = new MagicPage();
        mainPage.setHtmlString(htmlString);
        mainPage.setDelFlag(DeleteFlag.NO);
        mainPage.setCreateTime(LocalDateTime.now());
        mainPage.setOperatePerson(operatePerson);
        magicPageRepository.save(mainPage);
        return mainPage;
    }

    /**
     * 通过店铺id 查询在线客服
     *
     * @author lq
     */
    public MagicPage get() {
        return magicPageRepository.findLast();
    }
}
