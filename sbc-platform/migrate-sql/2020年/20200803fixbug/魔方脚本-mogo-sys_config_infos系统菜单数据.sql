// 5ab9f29fe846533aadfcde0c 系统菜单数据(新增了页面类型——分类页)
db.getCollection("sys_config_infos").insert({
    "_id": "5ab9f29fe846533aadfcde0c",
    "envCode": "test1",
    "systemCode": "d2cStore",
    "pageCates": [
        {
            "parentId": "5ab9f3524547ab3bb41e7f77",
            "value": "",
            "name": "首页",
            "_id": ObjectId("5aba0af815dfc54212cd423e")
        },
        {
            "parentId": "5ab9f3524547ab3bb41e7f77",
            "value": "",
            "name": "海报页",
            "_id": ObjectId("5aba136215dfc54212cd4243")
        },
        {
            "parentId": "5ab9f3524547ab3bb41e7f77",
            "value": "",
            "name": "文章页",
            "_id": ObjectId("5aba136c15dfc54212cd4244")
        },
        {
            "parentId": "5ab9f3524547ab3bb41e7f77",
            "value": "",
            "name": "基础页",
            "_id": ObjectId("5aba137615dfc54212cd4245")
        },
        {
            "parentId": "5ab9f35c4547ab3bb41e7f78",
            "value": "",
            "name": "首页",
            "_id": ObjectId("5aba13da15dfc54212cd4246")
        },
        {
            "parentId": "5ab9f35c4547ab3bb41e7f78",
            "value": "",
            "name": "海报页",
            "_id": ObjectId("5aba13e015dfc54212cd4247")
        },
        {
            "parentId": "5ab9f35c4547ab3bb41e7f78",
            "value": "",
            "name": "文章页",
            "_id": ObjectId("5aba13e915dfc54212cd4248")
        },
        {
            "parentId": "5ab9f3824547ab3bb41e7f79",
            "value": "",
            "name": "首页",
            "_id": ObjectId("5aba140215dfc54212cd4249")
        },
        {
            "parentId": "5ab9f38d4547ab3bb41e7f7a",
            "value": "",
            "name": "首页",
            "_id": ObjectId("5aba140c15dfc54212cd424a")
        },
        {
            "parentId": "5ab9f3a14547ab3bb41e7f7b",
            "value": "",
            "name": "首页",
            "_id": ObjectId("5aba18f015dfc54212cd4257")
        },
        {
            "parentId": "5ab9f3b54547ab3bb41e7f7c",
            "value": "",
            "name": "点单机",
            "_id": ObjectId("5aba1aad15dfc54212cd4259")
        }
    ],
    "platforms": [
        {
            "value": "weixin",
            "name": "微信端",
            "_id": ObjectId("5ab9f3524547ab3bb41e7f77")
        },
        {
            "value": "pc",
            "name": "PC端",
            "_id": ObjectId("5ab9f35c4547ab3bb41e7f78")
        },
        {
            "value": "adScreen",
            "name": "大屏广告机",
            "_id": ObjectId("5ab9f3824547ab3bb41e7f79")
        },
        {
            "value": "saleMachine",
            "name": "自动售货机",
            "_id": ObjectId("5ab9f38d4547ab3bb41e7f7a")
        },
        {
            "value": "pad",
            "name": "客显副屏",
            "_id": ObjectId("5ab9f3a14547ab3bb41e7f7b")
        },
        {
            "value": "orderMachine",
            "name": "点单机",
            "_id": ObjectId("5ab9f3b54547ab3bb41e7f7c")
        }
    ],
    "pageTypes": [
        {
            "parentId": "5aba0af815dfc54212cd423e",
            "value": "index",
            "name": "首页",
            "_id": ObjectId("5aba111715dfc54212cd423f")
        },
        {
            "parentId": "5aba136215dfc54212cd4243",
            "value": "poster",
            "name": "海报页",
            "_id": ObjectId("5aba172015dfc54212cd424b")
        },
        {
            "parentId": "5aba136215dfc54212cd4243",
            "value": "custom",
            "name": "海报页（old）",
            "_id": ObjectId("5aba173515dfc54212cd424c")
        },
        {
            "parentId": "5aba136c15dfc54212cd4244",
            "value": "article",
            "name": "文章页",
            "_id": ObjectId("5aba17a715dfc54212cd424d")
        },
        {
            "parentId": "5aba137615dfc54212cd4245",
            "value": "goodsList",
            "name": "列表页",
            "_id": ObjectId("5aba17d415dfc54212cd424e")
        },
        {
            "parentId": "5aba137615dfc54212cd4245",
            "value": "goodsInfo",
            "name": "详情页",
            "_id": ObjectId("5aba17df15dfc54212cd424f")
        },
        {
            "parentId": "5aba137615dfc54212cd4245",
            "value": "classify",
            "name": "分类页",
            "_id": ObjectId("5aba17e715dfc54212cd4250")
        },
        {
            "parentId": "5aba137615dfc54212cd4245",
            "value": "service",
            "name": "客服页",
            "_id": ObjectId("5aba17f315dfc54212cd4251")
        },
        {
            "parentId": "5aba13e015dfc54212cd4247",
            "value": "poster",
            "name": "海报页",
            "_id": ObjectId("5aba182015dfc54212cd4252")
        },
        {
            "parentId": "5aba13e915dfc54212cd4248",
            "value": "article",
            "name": "文章页",
            "_id": ObjectId("5aba183515dfc54212cd4253")
        },
        {
            "parentId": "5aba13da15dfc54212cd4246",
            "value": "index",
            "name": "首页",
            "_id": ObjectId("5aba184415dfc54212cd4254")
        },
        {
            "parentId": "5aba140215dfc54212cd4249",
            "value": "poster",
            "name": "首页",
            "_id": ObjectId("5aba189415dfc54212cd4255")
        },
        {
            "parentId": "5aba140c15dfc54212cd424a",
            "value": "saleMachine",
            "name": "首页",
            "_id": ObjectId("5aba18b115dfc54212cd4256")
        },
        {
            "parentId": "5aba18f015dfc54212cd4257",
            "value": "index",
            "name": "首页",
            "_id": ObjectId("5aba18f715dfc54212cd4258")
        },
        {
            "parentId": "5aba1aad15dfc54212cd4259",
            "value": "poster",
            "name": "点单机",
            "_id": ObjectId("5aba1ab715dfc54212cd425a")
        }
    ],
    "__v": NumberInt(0),
    "barCates": [
        {
            "value": "",
            "_id": ObjectId("5add9b42e15b9a13a2a3bd26"),
            "name": "商品和营销",
            "parentId": "5ab9f3524547ab3bb41e7f77"
        },
        {
            "value": "",
            "_id": ObjectId("5add9e36e15b9a13a2a3bd27"),
            "name": "图文排版",
            "parentId": "5ab9f3524547ab3bb41e7f77"
        },
        {
            "value": "",
            "_id": ObjectId("5add9e3ee15b9a13a2a3bd28"),
            "name": "多媒体",
            "parentId": "5ab9f3524547ab3bb41e7f77"
        }
    ]
})