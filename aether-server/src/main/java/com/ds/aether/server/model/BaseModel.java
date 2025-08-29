package com.ds.aether.server.model;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/29
 * @description
 */
@Data
public class BaseModel {

    /**
     * 主键
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 删除标识
     */
    private String deleteFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 租户ID
     */
    private String tenantId;

}
