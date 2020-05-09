package com.huchx.wx;

import com.alibaba.fastjson.JSON;
import com.huchx.wx.pojo.AccessTokenEntity;
import com.huchx.wx.pojo.UserIdEntity;
import com.huchx.wx.pojo.UserInfoEntity;
import com.huchx.wx.utils.JsonUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class RestTest {
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
    public static void main(String[] args) throws Exception{
//        Map<String,Object> map = new LinkedHashMap<>();
//        map.put("UserId","HuZhangXin");
//        map.put("DeviceId","10000568005448EE");
//        map.put("errcode",0);
//        map.put("errmsg","ok");
//        Map<String,Object> resultMap = new LinkedHashMap<>();
//        map.forEach((key,value)->{
//            resultMap.put(toLowerCaseFirstOne(key),value);
//        });
//        UserIdEntity userIdEntity =JsonUtils.jsonToPojo(JsonUtils.objectToJson(resultMap),UserIdEntity.class);
//        System.out.println(userIdEntity);



        RestTemplate restTemplate =new RestTemplate();
        String token = "F8PGgYYc3EBWwWODWitLbjZ1Q4jWke3agVB4RXQ3EdyF8X1n97_n_0Fv-txbcx0QdUNnfmc9Kn8aeQaLabnjo6jTCYi7dNGTE7BFZsfIpDqIWQA4fOICQ31E-QDGHNeY8gtP7CvSCLvRYM5CbQvXy6uf8eKTQyPi9CWzOlNFZRNdk9x-21fO8VSMRTcPyUNwWZRcMn4pWlKgVHW_Ptxm4w";
        String code = "RKk908IxpQQUDyFPbPj1MG3NuzUSxLkl3gSNFsTWFPk";
        String userId = "HuZhangXin";
        UriComponentsBuilder builderUserInfo = UriComponentsBuilder.fromHttpUrl("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .queryParam("access_token",token)
                .queryParam("code", code);

        Map<String,Object> map = (Map<String, Object>) restTemplate.exchange(builderUserInfo.build().encode().toUri(), HttpMethod.GET,null, Object.class).getBody();
        Map<String,Object> resultMap = new LinkedHashMap<>();
        map.forEach((key,value)->{
            resultMap.put(toLowerCaseFirstOne(key),value);
        });
        UserIdEntity userIdEntity = JsonUtils.jsonToPojo(JsonUtils.objectToJson(resultMap),UserIdEntity.class);
        System.out.println(userIdEntity);
    }
}
