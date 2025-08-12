package com.ds.aether.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ds
 * @date 2025/8/11
 * @description
 */
@Data
@AllArgsConstructor
public class Page {

    /**
     * 总数
     */
    Long total;

    /**
     * 数据
     */
    Object data;

    /**
     * 当前页码
     */
    private int page;
    /**
     * 每页大小
     */
    private int size;

    /**
     * 总页数
     */
    private int totalPages;

    public Page(Long total, Object data, int page, int size) {
        this.total = total;
        this.data = data;
        this.page = page;
        this.size = size;
        totalPages = (int) (total / size) + (total % size == 0 ? 0 : 1);
    }
}
