package com.wanmi.sbc.setting.util.error;

/**
 * <p>搜索词相关的异常码定义</p>
 */
public final class SearchTermsErrorCode {

    private SearchTermsErrorCode() {
    }

    /**
     * 搜索词不存在
     */
    public final static String NOT_EXIST = "K-080001";

    /**
     * 最多可设置20个联想词
     */
    public final static String  ASSOCIATION_WORDS_QUANTITATIVE_RESTRICTION = "association-words-quantitative-restriction";


    /**
     * 最多可设置20个热门搜索词
     */
    public final static String  POPULAR_SEARCH_TERMS_QUANTITATIVE_RESTRICTION = "popular-search-terms-quantitatative-restriction";

    /**
     * 长尾词最多1-5位字符
     */
    public final static String  SEARCH_TERM_LENGTH = "search-term-length";

    /**
     * 预置搜索词限制个数(1)
     */
    public final static String PRESET_SEARCH_TERM_RESTRICTIONS = "preset-search-term-restrictions";

    /**
     * 热门搜索词已存在
     */
    public final static String SEARCH_TERMS_NAME_EXIST = "K-091501";

    /**
     * 搜索词已存在
     */
    public final static String SEARCH_ASSOCIATION_NAME_EXIST = "K-091502";

    /**
     * 联想词已存在
     */
    public final static String ASSOCIATION_NAME_EXIST = "K-091503";

    /**
     * 最多可添加3个长尾词
     */
    public final static String LONG_TAIL_WORD_MAX_ERROR = "K-091504";

    /**
     * 长尾词不能重复
     */
    public final static String LONG_TAIL_WORD_EXIST = "K-091505";

    /**
     * 联想词不存在
     */
    public final static String ASSOCIATION_WORD_EXIST = "K-091506";
}
