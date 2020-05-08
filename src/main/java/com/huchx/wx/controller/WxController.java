package com.huchx.wx.controller;

import com.huchx.wx.pojo.AccessTokenEntity;
import com.huchx.wx.pojo.UserIdEntity;
import com.huchx.wx.pojo.UserInfoEntity;
import com.huchx.wx.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("wx")
public class WxController {


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

            //微信传参时，使用map对象当作参数不行，拼接在url中可以，未找到原因
            //获取access_token
            AccessTokenEntity accessTokenEntity = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret,AccessTokenEntity.class);
            Map<String,String> resultMap = new HashMap<>();
            if (accessTokenEntity.getErrcode()==0){

                //获取UserId时，参数拼接在url中返回userId为null，需使用map作为参数
                //获取userId
                Map<String,Object> paramsIdMap = new HashMap<>();
                paramsIdMap.put("access_token",accessTokenEntity.getAccess_token());
                paramsIdMap.put("code",code);
                UserIdEntity userIdEntity = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo", UserIdEntity.class,paramsIdMap);

                //成功返回
                if (userIdEntity.getErrcode() == 0) {


//                    //通过userid获取用户信息//
//                    UserInfoEntity userInfoEntity = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+accessTokenEntity.getAccess_token()+"&userid="+userIdEntity.getUserId(), UserInfoEntity.class);
//                    if (userInfoEntity.getErrcode()==0){
//                        resultMap.put("userId", userInfoEntity.getUserid());
//                        resultMap.put("deviceId", userInfoEntity.getOpen_userid());
//                        resultMap.put("name", userInfoEntity.getName());
//                        resultMap.put("department", userInfoEntity.getDepartment());
//                        resultMap.put("position", userInfoEntity.getPosition());
//                        resultMap.put("mobile", userInfoEntity.getMobile());
//                        resultMap.put("gender", userInfoEntity.getGender());
//                        resultMap.put("email", userInfoEntity.getEmail());
//                        resultMap.put("status", userInfoEntity.getStatus());
//                        return new  ResponseVo<Map>().ok(resultMap);
//                    }else {
//                        return new ResponseVo<Map>().error(null, userInfoEntity.getErrmsg(), userInfoEntity.getErrcode());
//                    }

//                    //userId 返回
                    resultMap.put("userId", userIdEntity.getUserId());
                    resultMap.put("deviceId", userIdEntity.getDeviceId());
                    return new  ResponseVo<Map>().ok(resultMap);
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
     * 可以新增通过UserId获取用户信息接口
     * 或者在此接口中新增通过userId获取用户信息
     */

}
