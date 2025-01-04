package com.yongzhiai.core.config;

/**
 * @ClassName ConfigurationSnapshot
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 22:54
 * @Version 1.0
 **/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Raft集群的配置信息会时常变更
 * 通过集群配置快照来记录当前生效的配置和上一个配置
 */
public class ConfigurationSnapshot {

    private static final Logger LOG= LoggerFactory.getLogger(ConfigurationSnapshot.class);

    /**
     * 当前生效的集群配置
     */
    private Configuration activeConf;

    /**
     * 旧的配置项
     */
    private Configuration oldConf;


    public ConfigurationSnapshot(Configuration activeConf) {
        this.activeConf = activeConf;
    }

    public ConfigurationSnapshot(Configuration activeConf, Configuration oldConf) {
        this.activeConf = activeConf;
        this.oldConf = oldConf;
    }


    public Configuration getActiveConf() {
        return activeConf;
    }

    public Configuration getOldConf() {
        return oldConf;
    }

    public void setOldConf(Configuration oldConf) {
        this.oldConf = oldConf;
    }

    public void setActiveConf(Configuration activeConf) {
        this.activeConf = activeConf;
    }


    @Override
    public String toString() {
        return "ConfigurationSnapshot{" +
                "activeConf=" + activeConf +
                ", oldConf=" + oldConf +
                '}';
    }
}
