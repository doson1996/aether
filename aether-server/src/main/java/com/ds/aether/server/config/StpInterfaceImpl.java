package com.ds.aether.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dev33.satoken.model.wrapperInfo.SaDisableWrapperInfo;
import cn.dev33.satoken.stp.StpInterface;

/**
 * @author ds
 * @date 2025/8/28
 * @description 自定义权限加载接口实现类，完成 Sa-Token 的自定义权限验证扩展
 */
public class StpInterfaceImpl implements StpInterface  {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<String>();
        list.add("**");
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        list.add("admin");
        list.add("super-admin");
        return list;
    }

    @Override
    public SaDisableWrapperInfo isDisabled(Object loginId, String service) {
        return StpInterface.super.isDisabled(loginId, service);
    }
}
