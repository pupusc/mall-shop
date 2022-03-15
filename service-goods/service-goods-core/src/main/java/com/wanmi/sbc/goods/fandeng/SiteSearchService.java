package com.wanmi.sbc.goods.fandeng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Liang Jun
 * @desc 主站搜索
 * @date 2022-03-15 22:24:00
 */
@Slf4j
@Service
public class SiteSearchService {
    private String syncBookResUrl = "search-orchestration-system/search/v100/syncPaperBookData";
    private String syncBookPkgUrl = "search-orchestration-system/search/v100/syncBookListData";


}
