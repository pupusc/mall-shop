DELETE es_spu_new

PUT es_spu_new
{
"settings": {
"number_of_replicas": 1,
"number_of_shards": 1
}
}

POST es_spu_new/es_spu_new/_mapping
{
"es_spu_new": {
"properties": {
"spuId": {
"type": "keyword"
},
"spuName": {
"type": "text",
"analyzer": "ik_max_word",
"fields": {
"keyword": {
"type": "keyword",
"ignore_above": 256
}
}
},
"spuSubName": {
"type": "text",
"analyzer": "ik_max_word"
},
"spuCategory": {
"type": "integer"
},
"pic": {
"type": "keyword",
"index": false
},
"unBackgroundPic": {
"type": "keyword",
"index": false
},
"channelTypes": {
"type": "integer"
},
"salesNum": {
"type": "long"
},
"anchorRecoms": {
"properties": {
"recomId": {
"type": "integer"
},
"recomName": {
"type": "keyword"
}
}
},
"salesPrice": {
"type": "double"
},
"cpsSpecial": {
"type": "integer"
},
"addedFlag": {
"type": "integer"
},
"addedTime": {
"type": "date",
"format": "yyyy-MM-dd HH:mm:ss.SSS"
},
"createTime": {
"type": "date",
"format": "yyyy-MM-dd HH:mm:ss.SSS"
},
"indexTime": {
"type": "date",
"format": "yyyy-MM-dd HH:mm:ss.SSS"
},
"delFlag": {
"type": "integer"
},
"auditStatus": {
"type": "integer"
},
"book": {
"type": "nested",
"properties": {
"isbn": {
"type": "keyword"
},
"bookName": {
"type": "text",
"analyzer": "ik_max_word",
"fields": {
"keyword": {
"type": "keyword",
"ignore_above": 256
}
}
},
"bookOriginName": {
"type": "keyword"
},
"bookDesc": {
"type": "text",
"analyzer": "ik_max_word"
},
"authorNames": {
"type": "keyword"
},
"score": {
"type": "double"
},
"publisher": {
"type": "text",
"analyzer": "ik_max_word",
"fields": {
"keyword": {
"type": "keyword",
"ignore_above": 256
}
}
},
"fixPrice": {
"type": "double"
},
"producer": {
"type": "text",
"analyzer": "ik_max_word"
},
"clumpName": {
"type": "text",
"analyzer": "ik_max_word"
},
"awards": {
"properties": {
"awardCategory": {
"type": "integer"
},
"awardName": {
"type": "text",
"analyzer": "ik_max_word"
}
}
},
"groupName": {
"type": "text",
"analyzer": "ik_max_word"
},
"bindingName": {
"type": "text",
"analyzer": "ik_max_word"
},
"tags": {
"properties": {
"stagId": {
"type": "integer"
},
"stagName": {
"type": "keyword"
},
"tagId": {
"type": "integer"
},
"tagName": {
"type": "keyword"
}
}
}
}
},

        "classify": {
          "properties": {
            "classifyId": {
              "type": "integer"
            },
            "classifyName": {
              "type": "keyword"
            },
            "fclassifyId": {
              "type": "integer"
            },
            "fclassifyName": {
              "type": "keyword"
            }
          }
        },
        "comment": {
          "type": "nested",
          "properties": {
            "evaluateSum": {
              "type": "integer"
            },
            "goodEvaluateRatio": {
              "type": "keyword"
            },
            "goodEvaluateSum": {
              "type": "integer"
            }
          }
        }
        
      }
    }
}