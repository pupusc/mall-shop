# 分享注册分享购买流程

## H5端微信浏览器分享

1 读取当前登录人信息  
2 将登录人id作为分享人标识拼接url，追加shareUserId参数  
3 分享成功后该用户获得分享商品的成长值/积分  

1 其他用户点击链接,经过网络,到达前端服务器  
2 H5端解析链接，判断是否包含分享人标识，将shareUserId存进sessionStorage  
3 在注册或下单时将shareUserId保存进参数列表调用bff接口  
4 bff接口接收参数，用户下单则将分享人标识存进订单表，订单完成时为分享人增加成长值/积分

## 小程序图文分享
1 H5拼装小程序图文分享需要的参数，将当前登录人id作为shareUserId跳转到微信小程序指定页面  
2 小程序页面js获取shareUserId参数，拼接url并调用bff接口生成二维码  

1 其他用户扫码，在小程序sharePage.js中onLoad函数中调用bff接口解码，将包含shareUserId的链接回传给H5端  
2 解析链接与H5端一致

## APP图文分享
1 将当前登录人id作为shareUserId拼接url，并调用bff接口生成二维码  
2 扫码流程与小程序一致  