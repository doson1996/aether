package com.ds.aether.server.controller;

import com.ds.aether.server.log.AccessLog;
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

    @AccessLog(module = "页面控制", operation = "主页")
    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("title", "主页");
        model.addAttribute("weatherInfo", WeatherUtil.getLiveWeather("重庆市"));
        return "index";
    }

    @AccessLog(module = "页面控制", operation = "登录页")
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "auth/login";
    }

    @AccessLog(module = "页面控制", operation = "添加任务页")
    @GetMapping("/add-job")
    public String addJobPage(Model model) {
        model.addAttribute("title", "添加任务");
        return "job/add-job";
    }

    @AccessLog(module = "页面控制", operation = "编辑任务页")
    @GetMapping("/edit-job")
    public String editJob(Model model) {
        model.addAttribute("title", "编辑任务");
        return "job/edit-job";
    }

    @AccessLog(module = "页面控制", operation = "显示购买产品页面")
    @GetMapping("/products")
    public String showProductsPage() {
        return "products/products";
    }

    @AccessLog(module = "页面控制", operation = "显示支付页面")
    @GetMapping("/payment")
    public String showPaymentPage() {
        return "pay/payment";
    }

}
