package com.yongzhiai.metadata.impl;

/**
 * @ClassName DefaultRaftMetaStorage
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/11 19:00
 * @Version 1.0
 **/

import cn.hutool.core.io.FileUtil;
import com.yongzhiai.core.node.Node;
import com.yongzhiai.election.VoteInfo;
import com.yongzhiai.metadata.RaftMetaStorage;
import com.yongzhiai.metadata.RaftMetaStorageOptions;
import com.yongzhiai.metadata.RaftMetadataInfo;
import com.yongzhiai.proto.JRaftMetadataProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * 本地磁盘存储Raft的元数据信息
 */
public class LocalRaftMetaStorage implements RaftMetaStorage {
    private static final Logger LOG= LoggerFactory.getLogger(LocalRaftMetaStorage.class);
    /**
     * 配置参数
     */
    private RaftMetaStorageOptions raftMetaStorageOptions;

    /**
     * 是否初始化
     */
    private boolean initialized;

    /**
     * 存储目录
     */
    private final String path;

    /**
     * 内存存储的元数据
     */
    private RaftMetadataInfo metadata;

    /**
     * 当前元数据存储器所服务的节点
     */
    private Node node;


    public LocalRaftMetaStorage(String path, RaftMetadataInfo metadata, Node node) {
        this.path = path;
        this.metadata = metadata;
        this.node = node;
    }


    public LocalRaftMetaStorage(String path, Node node) {
        this(path,null,node);
    }

    @Override
    public boolean initialize(RaftMetaStorageOptions metaStorageOptions) {

        if(isInitialized()){
            LOG.warn("Raft metadata storage has already been initialized.");
            return true;
        }

        setRaftMetaStorageOptions(metaStorageOptions);

        File localMetaFile = new File(path + File.separator + RaftMetaStorage.FILE_NAME);
        //本地不存在此元数据文件
        /**
         * 1. 创建文件
         * 2. 初始化默认的元数据信息
         *
         */
        if(!localMetaFile.exists()){
            //1. 创建本地元数据文件
            //如果文件的父目录不存在，则创建所有必要的父目录
            if(!localMetaFile.getParentFile().exists() && !localMetaFile.getParentFile().mkdirs()){

                LOG.error("Could not create local meta dir {}", localMetaFile.getParentFile());
                return false;
            }

            try {
                if(localMetaFile.createNewFile()){
                    LOG.info("Create local meta file {} successfully", localMetaFile);
                }else{
                    LOG.error("Could not create local meta file {}", localMetaFile);

                    return false;
                }
            }catch (IOException ex){
                LOG.error("Could not create local meta file {},ex {}", localMetaFile,ex.getMessage());
            }

            //2. 初始化默认的元数据信息
            setMetadata(new RaftMetadataInfo(new VoteInfo()));
        }else{
            //本地元数据存储文件存在，尝试加载本地元数据文件到内存
            //填充RaftMetadataInfo对象

            if(!load()){
                LOG.error("Could not load local meta data from file {}", localMetaFile);
                return false;
            }
        }

        //设置初始化成功标志位
        setInitialized(true);
        LOG.info("Loading local meta data from file {} successfully", localMetaFile);
        return true;
    }


    @Override
    public boolean load() {
        File localMetaFile= new File(this.path + File.separator + RaftMetaStorage.FILE_NAME);

        //文件不存在，加载失败
        if(!localMetaFile.exists()){
            LOG.error("File does not exist: " + localMetaFile);
            return false;
        }
        //文件输入流
        try(BufferedInputStream inputStream = FileUtil.getInputStream(localMetaFile)) {
            //通过protobuf协议反序列化jraft元数据信息
            JRaftMetadataProto.JRaftMetadata raftMetadata=
                    JRaftMetadataProto.JRaftMetadata.parseFrom(inputStream);

            if(raftMetadata==null || raftMetadata== JRaftMetadataProto.JRaftMetadata.getDefaultInstance()){
                LOG.warn("proto metadata is null");
                //设置默认的raft元数据信息
                setMetadata(new RaftMetadataInfo(new VoteInfo()));
                return true;
            }

            RaftMetadataInfo metadataInfo = RaftMetadataInfo.parseFrom(raftMetadata);

            setMetadata(metadataInfo);

            LOG.info("proto metadata successfully parsed");
            return true;

        } catch (IOException e) {
            LOG.error("Deserialization of protobuf failed due to " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean save() {
        /**
         * 将内存中的raft元数据信息持久化到磁盘文件中
         */
        File localMetaFile = new File(path + File.separator + RaftMetaStorage.FILE_NAME);

        //如果文件不存在，创建文件，如果目录也不存在先创建目录层级

        if(!localMetaFile.exists()){

            if(!localMetaFile.getParentFile().exists() && !localMetaFile.getParentFile().mkdirs()){
                LOG.error("Could not create local meta dir {}", localMetaFile.getParentFile());
                return false;
            }

            try {
                if(!localMetaFile.createNewFile()){
                    LOG.error("Could not create local meta file {}", localMetaFile);
                    return false;
                }
            } catch (IOException ex) {
                LOG.error("Could not create local meta file {} ,ex {}", localMetaFile,ex.getMessage());

                return false;
            }


            LOG.info("create local meta file {} successfully", localMetaFile);
        }


        //持久化内存中的RaftMetadataInfo对象到磁盘
        boolean saved = getMetadata().writeToFile(localMetaFile);

        if(saved){
            LOG.info("Save local meta data to file {} successfully", localMetaFile);
            return true;
        }else{
            LOG.error("Could not save local meta data to file {}", localMetaFile);
            return false;
        }

    }

    @Override
    public void shutdown() {
        //TODO: 目前暂时没有需要关闭的资源和需要关闭前执行的钩子函数

        LOG.info("Shutting down local raft metadata storage");
    }




    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }


    @Override
    public RaftMetadataInfo getMetadata() {
        return this.metadata;
    }

    @Override
    public void setMetadata(RaftMetadataInfo metadata) {
        this.metadata=metadata;
    }

    @Override
    public Node getNode() {
        return this.node;
    }


    public RaftMetaStorageOptions getRaftMetaStorageOptions() {
        return raftMetaStorageOptions;
    }

    public void setRaftMetaStorageOptions(RaftMetaStorageOptions raftMetaStorageOptions) {
        this.raftMetaStorageOptions = raftMetaStorageOptions;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
