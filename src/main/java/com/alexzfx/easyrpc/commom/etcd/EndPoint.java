package com.alexzfx.easyrpc.commom.etcd;

import lombok.Data;

/**
 * Author : Alex
 * Date : 2018/10/1 19:12
 * Description :
 */
@Data
public class EndPoint {
    private final String host;
    private final int port;

    public EndPoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EndPoint)) {
            return false;
        } else {
            return ((EndPoint) o).host.equals(host) && ((EndPoint) o).port == port;
        }
    }

    @Override
    public int hashCode() {
        return host.hashCode() + port;
    }

}
