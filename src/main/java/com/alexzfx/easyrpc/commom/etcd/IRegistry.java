package com.alexzfx.easyrpc.commom.etcd;

import java.util.List;

public interface IRegistry {

    // 注册服务
    void register(String serviceName, int port) throws Exception;

    List<EndPoint> find(String serviceName) throws Exception;
}
