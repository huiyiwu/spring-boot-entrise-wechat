package com.huchx.wx;

import com.huchx.wx.pojo.UserIdEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(classes={WxApplication.class})
class WxApplicationTests {

    @Test
    void contextLoads() {
        RestTemplate restTemplate =new RestTemplate();
        String token = "F8PGgYYc3EBWwWODWitLbjZ1Q4jWke3agVB4RXQ3EdyF8X1n97_n_0Fv-txbcx0QdUNnfmc9Kn8aeQaLabnjo6jTCYi7dNGTE7BFZsfIpDqIWQA4fOICQ31E-QDGHNeY8gtP7CvSCLvRYM5CbQvXy6uf8eKTQyPi9CWzOlNFZRNdk9x-21fO8VSMRTcPyUNwWZRcMn4pWlKgVHW_Ptxm4w";
        String code = "knfe7HisJ4guS7HF5eZHIhY4NiiNC8tJo3sJXawsrKs";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo")
                .queryParam("access_token",token)
                .queryParam("code", code);

        ResponseEntity<UserIdEntity> userIdEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,null, UserIdEntity.class);
    }

}
