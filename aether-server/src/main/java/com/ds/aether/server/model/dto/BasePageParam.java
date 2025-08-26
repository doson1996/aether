package com.ds.aether.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ds
 * @date 2025/8/11
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageParam {

    private Integer pageNum;

    private Integer pageSize;

}
