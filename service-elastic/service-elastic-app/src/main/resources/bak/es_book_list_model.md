

DELETE es_book_list_model

PUT es_book_list_model
{
"settings": {
"number_of_replicas": 1,
"number_of_shards": 1
}
}

POST es_book_list_model/es_book_list_model/_mapping
{
"es_book_list_model": {
"properties": {
"bookListId": {
"type": "keyword"
},
"bookListBusinessType": {
"type": "integer"
},
"bookListCategory": {
"type": "integer"
},
"bookListName": {
"type": "text",
"analyzer": "ik_max_word"
},
"bookListDesc": {
"type": "text",
"analyzer": "ik_max_word"
},
"hasTop": {
"type": "integer"
},
"publishState": {
"type": "integer"
},
"createTime": {
"type": "date",
"format": "yyyy-MM-dd HH:mm:ss.SSS"
},
"updateTime": {
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
"spuNum": {
"type": "integer"
},
"spus": {
"type": "nested",
"properties": {
"channelTypes": {
"type": "integer"
},
"pic": {
"type": "keyword",
"index": false
},
"sortNum": {
"type": "integer"
},
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
}
}
}
}
}
}