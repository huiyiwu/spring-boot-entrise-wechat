package com.huchx.wx.controller;

import com.huchx.wx.pojo.UserInfoEntity;
import com.huchx.wx.utils.JsonUtils;
import com.huchx.wx.utils.RedisUtil;
import com.huchx.wx.pojo.AccessTokenEntity;
import com.huchx.wx.pojo.UserIdEntity;
import com.huchx.wx.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("wx")
public class WxController {

    @Autowired
    RedisUtil redisUtil;
    /**
     *
     * @param corpid        企业ID
     * @param corpsecret    企业应用secret
     * @param code          企业微信网页授权回调code，由前端传入，一个仅能使用一次
     * @return
     *
     * ****************************************************
     * gettoken接口(获取access_token)有频率限制，有效期内多次请求会造成拦截
     * ***************************************************
     */
    @RequestMapping("/getWXUserId")
    public ResponseVo<Map> getWXUserId(String corpid,String corpsecret,String code){
        RestTemplate restTemplate = new RestTemplate();

        try {
            /**
             * 获取access_token
             * 有效期内微信平台返回相同access_token且有频率限制，所以获取后保存在缓存中，失效时重新获取
             */
            AccessTokenEntity accessTokenEntity = (AccessTokenEntity) redisUtil.get("access_token");
            if (accessTokenEntity==null){
                //此方式将参数拼接到url中
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                        .queryParam("corpid","ww74a7e4122b8e40cf")
                        .queryParam("corpsecret", "1CN7B_70IJM4s_hsdyg3O2JHBts8NwLjYWQC8MESWAM");

                //返回ResponseEntity<T>类型
                accessTokenEntity = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.GET,
                        null,
                        AccessTokenEntity.class).getBody();

                //成功将access_token保存到缓存中，设置缓存有效期为access_token的有效期提前30s
                if (accessTokenEntity.getErrcode()==0){
                    redisUtil.set("access_token",accessTokenEntity,accessTokenEntity.getExpires_in()-30);
                }

            }
            /**
             * 获取access成功后获取userId
             */
            if (accessTokenEntity.getErrcode()==0){

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo")
                        .queryParam("access_token", accessTokenEntity.getAccess_token())
                        .queryParam("code", code);

                Map<String,Object> map = (Map<String, Object>) restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.GET,
                        null,
                        Object.class).getBody();

                //平台返回的某些字段首字母是大写，需要处理
               UserIdEntity userIdEntity = getUserIdEntityByMap(map);
               if (userIdEntity.getErrcode() == 0) {

                   //需要获取用户信息，注释解开line 91-101,注释line 103-107,否反之

                   /**
                     * 通过access_token和code获取用户信息
                     */
                    UriComponentsBuilder builderUserInfo = UriComponentsBuilder.fromHttpUrl("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                            .queryParam("access_token",accessTokenEntity.getAccess_token())
                            .queryParam("userid", userIdEntity.getUserId());

                    //直接使用平台返回数据,根据需要处理
                    Map<String,Object> userInfoMap = (Map<String, Object>) restTemplate.exchange(builderUserInfo.build().encode().toUri(), HttpMethod.GET,null, Object.class).getBody();
                    if ((int)userInfoMap.get("errcode")==0){
                        return new  ResponseVo<Map>().ok(userInfoMap);
                    }else {
                        return new ResponseVo<Map>().error(null, userInfoMap.get("errmsg").toString(), (int)userInfoMap.get("errcode"));
                    }

                       //获取userId后返回
//                    Map<String,String> resultMap = new HashMap<>();
//                    resultMap.put("userId", userIdEntity.getUserId());
//                    resultMap.put("deviceId", userIdEntity.getDeviceId());
//                    return new  ResponseVo<Map>().ok(resultMap);
                }else {
                    return new ResponseVo<Map>().error(null, userIdEntity.getErrmsg(), userIdEntity.getErrcode());
                }
            }else {
                return new ResponseVo<Map>().error(null,accessTokenEntity.getErrmsg(),accessTokenEntity.getErrcode());
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVo<Map>().error(null);
        }
    }

    /**
     * 获取userId接口返回示例
     * {
     *    "errcode": 0,
     *    "errmsg": "ok",
     *    "UserId":"USERID",
     *    "DeviceId":"DEVICEID"
     * }
     * 由于部分字段首字母大写导致实体类失败，需做处理
     * @param map
     * @return
     */
    private UserIdEntity getUserIdEntityByMap(Map<String, Object> map) {
        if (map.isEmpty()){
            return  null;
        }
        Map<String,Object> resultMap = new LinkedHashMap<>();
        map.forEach((key,value)->{
            resultMap.put(toLowerCaseFirstOne(key),value);
        });
        UserIdEntity userIdEntity = JsonUtils.jsonToPojo(JsonUtils.objectToJson(resultMap),UserIdEntity.class);
        System.out.println(userIdEntity);
        return userIdEntity;
    }
    /**
     * 首字母转小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 可以新增通过UserId获取用户信息接口
     * 或者在此接口中新增通过userId获取用户信息
     */

}
