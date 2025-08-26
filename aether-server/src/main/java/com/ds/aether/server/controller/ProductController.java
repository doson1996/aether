package com.ds.aether.server.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/8/26
 * @description
 */
@Slf4j
@RestController
@RequestMapping("product")
public class ProductController {

    /**
     * 获取商品信息接口
     */
    @GetMapping("info/{productId}")
    @ResponseBody
    public Map<String, Object> productInfo(@PathVariable String productId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里应该从数据库或其他服务获取商品信息
            // 示例数据，实际应该根据productId查询真实数据
            Map<String, Object> product = new HashMap<>();

            String orderNo = genOrderNo();
            if ("1".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether基础版服务");
                product.put("description", "Aether任务调度系统基础版服务费用");
                product.put("price", "99.00");
                product.put("orderNo", orderNo);
            } else if ("2".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether专业版服务");
                product.put("description", "Aether任务调度系统专业版服务费用");
                product.put("price", "299.00");
                product.put("orderNo", orderNo);
            } else if ("3".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether企业版服务");
                product.put("description", "Aether任务调度系统企业版服务费用");
                product.put("price", "999.00");
                product.put("orderNo", orderNo);
            } else {
                // 默认商品信息
                product.put("id", productId);
                product.put("name", "Aether基础版服务");
                product.put("description", "Aether任务调度系统基础版服务费用");
                product.put("price", "99.00");
                product.put("orderNo", orderNo);
            }

            result.put("success", true);
            result.put("data", product);
            result.put("message", "获取商品信息成功");
        } catch (Exception e) {
            log.error("获取商品信息失败", e);
            result.put("success", false);
            result.put("message", "获取商品信息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    private String genOrderNo() {
        return "ORDER" + System.currentTimeMillis();
    }

}
