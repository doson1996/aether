package com.ds.aether.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ds
 * @date 2025/8/15
 * @description
 */
@Controller
public class IndexController {

    /**
     * 重定向到主页
     *
     * @return 重定向视图
     */
    @GetMapping("/")
    public String redirectToHome() {
        System.out.println("redirect to home");
        return "redirect:/page/";
    }

}
