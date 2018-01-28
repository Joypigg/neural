package io.neural.limiter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.neural.common.Identity;

import java.util.Map;

/**
 * @author lry
 **/
public class LimiterConfigTest {

    public static void main(String[] args) {
        LimiterConfig.Config config = new LimiterConfig.Config();
        config.setConcurrencyTimeout(1000L);
        config.setConcurrency(100L);
        config.setRateTimeout(1000L);
        config.setRate(2000L);
        config.setStrategy(LimiterConfig.Strategy.EXCEPTION);
        config.setName("标题");
        config.setRemarks("备注信息");
        config.setEnable(Identity.Switch.OFF);
        String json = JSON.toJSONString(config);
        System.out.println(json);
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        System.out.println(map.toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
        System.out.println("===========");

        String json2 = JSON.toJSONString(map);
        System.out.println(json2);
        LimiterConfig.Config config1 = JSONObject.parseObject(json2, LimiterConfig.Config.class);
        System.out.println(config1);
    }

}
