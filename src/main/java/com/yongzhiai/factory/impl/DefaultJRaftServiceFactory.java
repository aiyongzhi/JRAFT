package com.yongzhiai.factory.impl;

import com.yongzhiai.core.node.Node;
import com.yongzhiai.factory.JRaftServiceFactory;
import com.yongzhiai.metadata.RaftMetaStorage;
import com.yongzhiai.metadata.impl.LocalRaftMetaStorage;

/**
 * @ClassName DefaultJRaftServiceFactory
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/13 23:00
 * @Version 1.0
 **/

/**
 * 默认的raft组件服务工厂
 *
 * 此服务工厂应当提供SPI机制，让用户可以注入自己的服务工厂，从而将
 * 系统默认的组件替换为用户自定义的组件
 *
 */
public class DefaultJRaftServiceFactory implements JRaftServiceFactory {

    public static DefaultJRaftServiceFactory newInstance(){
        return new DefaultJRaftServiceFactory();
    }


    @Override
    public RaftMetaStorage createRaftMetaStorage(String path, Node node) {
        //本地元数据存储器
        return new LocalRaftMetaStorage(path, node);
    }
}
