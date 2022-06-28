package com.soybean.elastic.constant;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/5 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class ConstantMultiMatchField {
    /**
     * 设置最小匹配数量
     */
    public static final String FIELD_MINIMUM_SHOULD_MATCH = "2";
    /**
     * 书单/榜单
     */
    public static final String FIELD_BOOK_LIST_BOOKLISTNAME = "bookListName";

    public static final String FIELD_BOOK_LIST_SPU_SPUNAME = "spuName";

    public static final String FIELD_BOOK_LIST_SPU_SPUNAME_KEYWORD = "spuName.keyword";

    /**
     * 商品图书
     */
    public static final String FIELD_SPU_SPUNAME = "spuName";
    public static final String FIELD_SPU_SPUNAME_KEYWORD = "spuName.keyword";
    public static final String FIELD_SPU_SPUSUBNAME = "spuSubName";
    public static final String FIELD_SPU_ANCHORRECOMS_RECOMNAME = "anchorRecoms.recomName";
    public static final String FIELD_SPU_CLASSIFY_CLASSIFYNAME = "classify.classifyName";
    public static final String FIELD_SPU_CLASSIFY_FCLASSIFYNAME = "classify.fclassifyName";


    public static final String FIELD_SPU_BOOK_BOOKNAME = "book.bookName";
    public static final String FIELD_SPU_BOOK_BOOKNAME_KEYWORD = "book.bookName.keyword";
    public static final String FIELD_SPU_BOOK_BOOKORIGINNAME = "book.bookOriginName";
    public static final String FIELD_SPU_BOOK_BOOKDESC = "book.bookDesc";
    public static final String FIELD_SPU_BOOK_AUTHORNAMES = "book.authorNames";
    public static final String FIELD_SPU_BOOK_PUBLISHER = "book.publisher";
    public static final String FIELD_SPU_BOOK_PUBLISHER_KEYWORD = "book.publisher.keyword";
    public static final String FIELD_SPU_BOOK_PRODUCER = "book.producer";
    public static final String FIELD_SPU_BOOK_CLUMPNAME = "book.clumpName";
    public static final String FIELD_SPU_BOOK_AWARDS_AWARDNAME = "book.awards.awardName";
    public static final String FIELD_SPU_BOOK_GROUPNAME = "book.groupName";
//    public static final String FIELD_SPU_BOOK_SERIESNAME = "book.seriesName";
    public static final String FIELD_SPU_BOOK_BINDINGNAME = "book.bindingName";
    public static final String FIELD_SPU_BOOK_TAGS_TAGNAME= "book.tags.tagName";
    public static final String FIELD_SPU_BOOK_TAGS_STAGNAME = "book.tags.stagName";



}
