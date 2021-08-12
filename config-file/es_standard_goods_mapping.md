curl -H "Content-Type:application/json" -XPUT "http://localhost:9200/es_standard_goods/es_standard_goods/_mapping?pretty" -d '
{
    "es_standard_goods":
    {
        "properties":
        {
            "addedFlag":
            {
                "type": "integer"
            },
            "brandId":
            {
                "type": "long"
            },
            "cateId":
            {
                "type": "long"
            },
            "costPrice":
            {
                "type": "double"
            },
            "createTime":
            {
                "type": "date",
                "index": false,
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            },
            "delFlag":
            {
                "type": "integer"
            },
            "deleteReason":
            {
                "type": "text"
            },
            "goodsCubage":
            {
                "type": "double"
            },
            "goodsDetail":
            {
                "type": "text"
            },
            "goodsId":
            {
                "type": "keyword"
            },
            "goodsImg":
            {
                "type": "keyword"
            },
            "goodsInfoIds":
            {
                "type": "keyword"
            },
            "goodsInfoNos":
            {
                "type": "keyword"
            },
            "goodsMobileDetail":
            {
                "type": "text"
            },
            "goodsName":
            {
                "type": "keyword"
            },
            "goodsNo":
            {
                "type": "keyword"
            },
            "goodsSource":
            {
                "type": "integer"
            },
            "goodsSubtitle":
            {
                "type": "keyword"
            },
            "goodsUnit":
            {
                "type": "keyword"
            },
            "goodsVideo":
            {
                "type": "keyword"
            },
            "goodsWeight":
            {
                "type": "double"
            },
            "marketPrice":
            {
                "type": "double"
            },
            "moreSpecFlag":
            {
                "type": "integer"
            },
            "providerGoodsId":
            {
                "type": "keyword"
            },
            "providerName":
            {
                "type": "keyword"
            },
            "recommendedRetailPrice":
            {
                "type": "double"
            },
            "relStoreIds":
            {
                "type": "long"
            },
            "sellerId":
            {
                "type": "long"
            },
            "stock":
            {
                "type": "long"
            },
            "storeId":
            {
                "type": "long"
            },
            "supplyPrice":
            {
                "type": "double"
            },
            "thirdCateId":
            {
                "type": "long"
            },
            "thirdPlatformSpuId":
            {
                "type": "keyword"
            },
            "updateTime":
            {
                "type": "date",
                "index": false,
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            }
        }
    }
}'