package com.yongzhiai.metadata;

/**
 * @ClassName RaftMetaStorage
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/11 18:43
 * @Version 1.0
 **/

import com.yongzhiai.core.node.Node;
import com.yongzhiai.core.node.lifecycle.Lifecycle;

/**
 * jraft的元数据存储器
 *
 */
public interface RaftMetaStorage extends Lifecycle<RaftMetaStorageOptions> {


    public static final String FILE_NAME="jraft_metadata.proto.bin";

    /**
     * 文件路径
     * @return
     */
    String getPath();

    /**
     * 是否已经初始化
     * @return
     */
    boolean isInitialized();


    /**
     * 从元数据存储文件加载元数据信息到内存中
     * @return
     */
    boolean load();

    /**
     * 保存当前节点的raft元数据信息
     * @return
     */
    boolean save();

    /**
     * 获取内存缓存的元数据信息
     * @return
     */
    RaftMetadataInfo getMetadata();


    /**
     * 设置存储器内存级别的元数据信息
     * @param metadata
     */
    void setMetadata(RaftMetadataInfo metadata);

    /**
     * 每个Node节点都有一个唯一的RaftMetaStorage
     * @return
     */
    Node getNode();

}
