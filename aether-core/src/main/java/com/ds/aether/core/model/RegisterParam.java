package com.ds.aether.core.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Data
public class RegisterParam implements Serializable {

    private static final long serialVersionUID = 1350156985574491056L;

    private String name;

    private String host;

    private String contextPath;

}
