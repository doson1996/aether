package com.ds.aether.core.model.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutorInfo {

    private String name;

    private String host;

    private String contextPath;

}
