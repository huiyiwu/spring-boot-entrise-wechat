>应用访问流程
1. 通过移动端传递的参数corpid(企业ID)、corpsecret(企业应用secret),获取access_token

2. 通过access_token及移动端传递的code参数获取userId

3.通过access_token和userId获取userInfo


> 注意点
1. 获取access_token的接口有频率限制，所以获取后保存在缓存中

2. access_token在有效期内返回相同token，多人返回相同access_token

3.获取UserId时，接口返回数据如下，部分字段首字母为大写会导致转换实体类失败，需处理。
```json
  {
     "errcode": 0,
      "errmsg": "ok",
      "UserId":"USERID",
      "DeviceId":"DEVICEID"
  }
```
> 网页授权链接，企业微信中菜单配置的访问地址的值，格式如下：
```
https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
```
参数说明

参数|必须|描述
---|---|---
appId|true|企业的CorpID
redirect_uri|true|授权后重a定向的回调链接地址，即应用页面的访问地址，需用urlencode对链接编码
response_type|true|固定为code,跳转成功后访问链接中会带有code参数
scope|true|应用授权作用域。企业自建应用固定填写：snsapi_base
state|false|重定向后会带上state参数.可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
#wechat_redirect|是|判断是否需要带上身份信息