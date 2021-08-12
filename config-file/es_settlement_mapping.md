curl -H "content-type:application/json" -X PUT "http://localhost:9200/es_settlement/es_settlement/_mapping?pretty" -d '
{
    "es_settlement":
    {
        "properties":
        {
            "commissionPrice":
            {
                "type": "double"
            },
            "commonCouponPrice":
            {
                "type": "double"
            },
            "createTime":
            {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            },
            "deliveryPrice":
            {
                "type": "double"
            },
            "endTime":
            {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            },
            "id":
            {
                "type": "text",
                "fields":
                {
                    "keyword":
                    {
                        "type": "keyword",
                        "ignore_above": 256
                    }
                }
            },
            "platformPrice":
            {
                "type": "double"
            },
            "pointPrice":
            {
                "type": "double"
            },
            "providerPrice":
            {
                "type": "double"
            },
            "returnNum":
            {
                "type": "long"
            },
            "returnPrice":
            {
                "type": "double"
            },
            "saleNum":
            {
                "type": "long"
            },
            "salePrice":
            {
                "type": "double"
            },
            "settleCode":
            {
                "type": "text"
            },
            "settleId":
            {
                "type": "long"
            },
            "settleStatus":
            {
                "type": "integer"
            },
            "settleTime":
            {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            },
            "splitPayPrice":
            {
                "type": "double"
            },
            "startTime":
            {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS"
            },
            "storeId":
            {
                "type": "long"
            },
            "storeName":
            {
                "type": "keyword"
            },
            "storePrice":
            {
                "type": "double"
            },
            "storeType":
            {
                "type": "integer"
            }
        }
    }
}'