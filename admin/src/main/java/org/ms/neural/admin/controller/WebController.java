package org.ms.neural.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class WebController {

    @RequestMapping("/")
    public String index(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "index";
    }

    @RequestMapping("main")
    public String main(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "main";
    }

}
