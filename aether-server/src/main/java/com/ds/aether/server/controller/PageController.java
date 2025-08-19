package com.ds.aether.server.controller;

import com.ds.aether.server.util.WeatherUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ds
 * @date 2025/8/11
 * @description
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("title", "主页");
        model.addAttribute("weatherInfo", WeatherUtil.getLiveWeather("重庆市"));
        return "index";
    }

    @GetMapping("/add-job")
    public String addJobPage(Model model) {
        model.addAttribute("title", "添加任务");
        return "add-job";
    }

    @GetMapping("/edit-job")
    public String editJob(Model model) {
        model.addAttribute("title", "编辑任务");
        return "edit-job";
    }

}
