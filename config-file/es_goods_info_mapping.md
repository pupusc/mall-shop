curl -H "Content-Type:application/json" -X PUT "http://localhost:9200/es_goods_info/es_goods_info/_mapping?pretty" -d '
{
      "es_goods_info" : {
        "properties" : {
          "addedTime" : {
            "type" : "date",
            "index" : false,
            "format" : "yyyy-MM-dd HH:mm:ss.SSS"
          },
          "auditStatus" : {
            "type" : "integer"
          },
          "contractEndDate" : {
            "type" : "date",
            "format" : "yyyy-MM-dd HH:mm:ss.SSS"
          },
          "contractStartDate" : {
            "type" : "date",
            "format" : "yyyy-MM-dd HH:mm:ss.SSS"
          },
          "distributionGoodsStatus" : {
            "type" : "integer"
          },
          "forbidStatus" : {
            "type" : "integer"
          },
          "goodsBrand" : {
            "properties" : {
              "brandId" : {
                "type" : "long"
              },
              "brandName" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "pinYin" : {
                "type" : "text",
                "analyzer" : "pinyin"
              },
              "pinyin" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "sPinYin" : {
                "type" : "text",
                "index" : false
              }
            }
          },
          "goodsCate" : {
            "properties" : {
              "cateId" : {
                "type" : "long"
              },
              "cateName" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "pinYin" : {
                "type" : "text",
                "analyzer" : "pinyin"
              },
              "sPinYin" : {
                "type" : "text",
                "index" : false
              }
            }
          },
          "goodsId" : {
            "type" : "keyword"
          },
          "goodsInfo" : {
            "properties" : {
              "addedFlag" : {
                "type" : "long"
              },
              "addedTime" : {
                "type" : "date",
                "index" : false,
                "format" : "yyyy-MM-dd HH:mm:ss.SSS"
              },
              "allowPriceSet" : {
                "type" : "integer",
                "index" : false
              },
              "aloneFlag" : {
                "type" : "boolean",
                "index" : false
              },
              "auditStatus" : {
                "type" : "integer",
                "index" : false
              },
              "brandId" : {
                "type" : "long"
              },
              "buyCount" : {
                "type" : "long",
                "index" : false
              },
              "buyPoint" : {
                "type" : "long"
              },
              "cateId" : {
                "type" : "long"
              },
              "checked" : {
                "type" : "boolean"
              },
              "commissionRate" : {
                "type" : "double"
              },
              "companyInfoId" : {
                "type" : "long"
              },
              "companyType" : {
                "type" : "integer"
              },
              "costPrice" : {
                "type" : "double",
                "index" : false
              },
              "count" : {
                "type" : "long",
                "index" : false
              },
              "couponLabels" : {
                "type" : "object"
              },
              "createTime" : {
                "type" : "date",
                "index" : false,
                "format" : "yyyy-MM-dd HH:mm:ss.SSS"
              },
              "customFlag" : {
                "type" : "integer",
                "index" : false
              },
              "delFlag" : {
                "type" : "long"
              },
              "distributionCommission" : {
                "type" : "double"
              },
              "distributionGoodsAudit" : {
                "type" : "integer"
              },
              "distributionGoodsAuditReason" : {
                "type" : "keyword",
                "index" : false
              },
              "distributionSalesCount" : {
                "type" : "integer",
                "index" : false
              },
              "enterPriseAuditStatus" : {
                "type" : "integer"
              },
              "enterPriseGoodsAuditReason" : {
                "type" : "text",
                "index" : false
              },
              "enterPriseMaxPrice" : {
                "type" : "double"
              },
              "enterPriseMinPrice" : {
                "type" : "double"
              },
              "enterPrisePrice" : {
                "type" : "double"
              },
              "enterpriseCustomerFlag" : {
                "type" : "boolean"
              },
              "enterpriseDiscountFlag" : {
                "type" : "boolean"
              },
              "enterprisePriceType" : {
                "type" : "integer"
              },
              "erpGoodsNo" : {
                "type" : "text",
                "index" : false
              },
              "esSortPrice" : {
                "type" : "double"
              },
              "freightTempId" : {
                "type" : "long",
                "index" : false
              },
              "goodsCollectNum" : {
                "type" : "long"
              },
              "goodsCubage" : {
                "type" : "double",
                "index" : false
              },
              "goodsEvaluateNum" : {
                "type" : "integer"
              },
              "goodsFavorableCommentNum" : {
                "type" : "long"
              },
              "goodsFeedbackRate" : {
                "type" : "long"
              },
              "goodsId" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "goodsInfoBarcode" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "goodsInfoId" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "goodsInfoImg" : {
                "type" : "text",
                "index" : false
              },
              "goodsInfoName" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "goodsInfoNo" : {
                "type" : "keyword"
              },
              "goodsSalesNum" : {
                "type" : "long"
              },
              "goodsStatus" : {
                "type" : "long",
                "index" : false
              },
              "goodsType" : {
                "type" : "integer"
              },
              "goodsUnit" : {
                "type" : "text",
                "index" : false
              },
              "goodsWeight" : {
                "type" : "double",
                "index" : false
              },
              "grouponLabel" : {
                "type" : "object"
              },
              "intervalMaxPrice" : {
                "type" : "double",
                "index" : false
              },
              "intervalMinPrice" : {
                "type" : "double",
                "index" : false
              },
              "intervalPriceIds" : {
                "type" : "long",
                "index" : false
              },
              "joinDistributior" : {
                "type" : "integer",
                "index" : false
              },
              "levelDiscountFlag" : {
                "type" : "integer",
                "index" : false
              },
              "marketPrice" : {
                "type" : "double"
              },
              "marketingLabels" : {
                "type" : "object"
              },
              "maxCount" : {
                "type" : "long",
                "index" : false
              },
              "priceType" : {
                "type" : "integer",
                "index" : false
              },
              "providerGoodsInfoId" : {
                "type" : "text",
                "index" : false
              },
              "providerId" : {
                "type" : "text",
                "index" : false
              },
              "providerStatus" : {
                "type" : "integer"
              },
              "salePrice" : {
                "type" : "double",
                "index" : false
              },
              "saleType" : {
                "type" : "long"
              },
              "smallProgramCode" : {
                "type" : "text",
                "index" : false
              },
              "specText" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "stock" : {
                "type" : "long"
              },
              "storeId" : {
                "type" : "long"
              },
              "storeName" : {
                "type" : "text",
                "fields" : {
                  "keyword" : {
                    "type" : "keyword",
                    "ignore_above" : 256
                  }
                }
              },
              "supplyPrice" : {
                "type" : "float"
              },
              "thirdPlatformSkuId" : {
                "type" : "text",
                "index" : false
              },
              "thirdPlatformSpuId" : {
                "type" : "text",
                "index" : false
              },
              "thirdPlatformType" : {
                "type" : "integer"
              },
              "updateTime" : {
                "type" : "date",
                "index" : false,
                "format" : "yyyy-MM-dd HH:mm:ss.SSS"
              },
              "validFlag" : {
                "type" : "integer",
                "index" : false
              },
              "vendibilityStatus" : {
                "type" : "integer"
              }
            }
          },
          "goodsLabelList" : {
            "type" : "nested",
            "properties" : {
              "delFlag" : {
                "type" : "long"
              },
              "goodsLabelId" : {
                "type" : "long"
              },
              "labelName" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "labelSort" : {
                "type" : "integer"
              },
              "labelVisible" : {
                "type" : "boolean"
              }
            }
          },
          "goodsName" : {
            "type" : "keyword"
          },
          "goodsNo" : {
            "type" : "keyword"
          },
          "goodsSource" : {
            "type" : "integer"
          },
          "goodsUnit" : {
            "type" : "text",
            "index" : false
          },
          "id" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          },
          "linePrice" : {
            "type" : "double",
            "index" : false
          },
          "lowGoodsName" : {
            "type" : "text"
          },
          "pinyinGoodsName" : {
            "type" : "text",
            "analyzer" : "pinyin"
          },
          "propDetailIds" : {
            "type" : "long"
          },
          "sortNo" : {
            "type" : "long"
          },
          "storeCateIds" : {
            "type" : "long"
          },
          "classify": {
            "properties": {
              "id":{
                "type": "integer"
              },
              "name":{
                "type":"keyword"
              }
            }
          },
          "storeState" : {
            "type" : "integer"
          },
          "vendibilityStatus" : {
            "type" : "integer"
          }
        }
      }
    }