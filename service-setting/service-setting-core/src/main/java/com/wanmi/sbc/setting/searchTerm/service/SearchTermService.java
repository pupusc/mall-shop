package com.wanmi.sbc.setting.searchTerm.service;

import com.wanmi.sbc.setting.searchTerm.repository.SearchTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/22/9:58
 * @Description:
 */
@Service
public class SearchTermService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SearchTermRepository searchTermRepository;


}
