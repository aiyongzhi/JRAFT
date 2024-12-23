package com.yongzhiai.core.node;

/**
 * @ClassName Endpoint
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2024/12/23 16:35
 * @Version 1.0
 **/

/**
 * 存储节点地址信息
 */
public class Endpoint {
    private String host;
    private int port;

    public Endpoint() {
    }

    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "Endpoint{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
