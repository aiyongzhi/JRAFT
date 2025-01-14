package com.yongzhiai.factory;

/**
 * @ClassName JRaftServiceFactory
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/13 22:55
 * @Version 1.0
 **/

import com.yongzhiai.core.node.Node;
import com.yongzhiai.metadata.RaftMetaStorage;

/**
 * jraft框架中的各个模块称为组件
 *
 * 此类就是提供获取各个模块组件实例的工厂
 */
public interface JRaftServiceFactory {

    /**
     * 创建一个raft元数据存储器
     * @param path 元数据文件存储路径
     * @param node 节点本身
     * @return
     */
    RaftMetaStorage createRaftMetaStorage(String path, Node node);
}
