package org.ms.neural.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class WebController {

    @RequestMapping("web")
    public Object web() {
        return new Date();
    }

}
