package com.yongzhiai.core.node;

/**
 * @ClassName NodeOptions
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:54
 * @Version 1.0
 **/

import cn.hutool.core.util.StrUtil;
import com.yongzhiai.core.config.Configuration;
import com.yongzhiai.factory.JRaftServiceFactory;
import com.yongzhiai.factory.impl.DefaultJRaftServiceFactory;

/**
 * 节点的配置选项
 * 各种key-value键值对
 *
 */
public class NodeOptions {

    /**
     * 默认的超时选举时间为3000ms
     */
    public static final int DEFAULT_ELECTION_TIMEOUT_MILLS=3000;

    /**
     * 日志本地持久化路径
     */
    private final String logPath;

    /**
     * raft算法元数据本地持久化目录
     */
    private final String raftMetaPath;

    /**
     * leader心跳超时，从节点进入超时选举流程
     */
    private final int electionTimeoutMills;

    /**
     * 初始化配置参数
     */
    private final Configuration initialConf;


    private JRaftServiceFactory raftServiceFactory;


    public NodeOptions(final String logPath,final String raftMetaPath,final int electionTimeoutMills,final Configuration initialConf) {
        if(StrUtil.isBlank(logPath) || StrUtil.isBlank(raftMetaPath) || initialConf==null ||
        initialConf.getPeers().isEmpty()){
            throw new IllegalArgumentException("参数不能为空");
        }

        this.logPath = logPath;
        this.raftMetaPath = raftMetaPath;
        this.electionTimeoutMills = electionTimeoutMills;
        this.initialConf = initialConf;


        //创建默认的raft服务工厂
        setRaftServiceFactory(DefaultJRaftServiceFactory.newInstance());
    }





    public String getLogPath() {
        return logPath;
    }

    public String getRaftMetaPath() {
        return raftMetaPath;
    }

    public int getElectionTimeoutMills() {
        return electionTimeoutMills;
    }

    public Configuration getInitialConf() {
        return initialConf;
    }


    public JRaftServiceFactory getRaftServiceFactory() {
        return raftServiceFactory;
    }

    public void setRaftServiceFactory(JRaftServiceFactory raftServiceFactory) {
        this.raftServiceFactory = raftServiceFactory;
    }
}
