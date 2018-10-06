package com.alexzfx.easyrpc.commom.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.options.GetOption;
import com.coreos.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Author : Alex
 * Date : 2018/10/1 19:16
 * Description :
 * etcd本质上是一个有序的K-V存储。
 */
@Slf4j
public class EtcdRegistry implements IRegistry {

    private static final String ROOTPATH = "easyrpc";
    private static final String DEFAULT_ADDRESS = "http://127.0.0.1:2379";
    private static final int LeaseTTL = 60;

    // 理解为租约，用于设置超时时间
    private Lease lease;
    //
    private KV kv;
    //租约对应的id
    private long leaseId;

    public EtcdRegistry() {
        this(DEFAULT_ADDRESS);
    }

    public EtcdRegistry(String registryAddress) {
        registryAddress = registryAddress != null ? registryAddress : DEFAULT_ADDRESS;
        Client client = Client.builder().endpoints(registryAddress).build();
        this.lease = client.getLeaseClient();
        this.kv = client.getKVClient();
        try {
            this.leaseId = lease.grant(LeaseTTL).get().getID();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        keepAlive();
    }

    //发送心跳到ETCD，表明该host的存活状态
    private void keepAlive() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Lease.KeepAliveListener listener = lease.keepAlive(leaseId);
                listener.listen();
                log.info("KeepAlive lease:" + leaseId + "; Hex format:" + Long.toHexString(leaseId));
            } catch (InterruptedException e) {
                log.error("KeepAlive lease disconnected,leaseId : " + leaseId);
                e.printStackTrace();
            }
        });
    }

    //注册类名，一个类对应一个client
    @Override
    public void register(String serviceName, int port) throws Exception {
        String strKey = MessageFormat.format("/{0}/{1}/{2}:{3}", ROOTPATH, serviceName, getHostIp(), String.valueOf(port));
        ByteSequence key = ByteSequence.fromString(strKey);
        // 目前只需要创建这个key,对应的value暂不使用,先留空
        ByteSequence val = ByteSequence.fromString("");
        //等待put结束之后继续运行
        kv.put(key, val, PutOption.newBuilder().withLeaseId(leaseId).build()).get();
        log.info("Register a new service at :" + strKey);
    }

    private String getHostIp() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostAddress();
    }

    @Override
    public List<EndPoint> find(String serviceName) throws Exception {
        String strkey = MessageFormat.format("/{0}/{1}", ROOTPATH, serviceName);
        log.info("start to find service, Name :" + strkey);
        ByteSequence key = ByteSequence.fromString(strkey);
        GetResponse response = kv.get(key, GetOption.newBuilder().withPrefix(key).build()).get();
        List<EndPoint> list = new ArrayList<>();
        response.getKvs().forEach(kv -> {
            String s = kv.getKey().toStringUtf8();
            int index = s.lastIndexOf("/");
            String endPointStr = s.substring(index + 1, s.length());
            String host = endPointStr.split(":")[0];
            int post = Integer.parseInt(endPointStr.split(":")[1]);
            list.add(new EndPoint(host, post));
        });
        return list;
    }
}
