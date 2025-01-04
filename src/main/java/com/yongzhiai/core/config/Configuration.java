package com.yongzhiai.core.config;

/**
 * @ClassName Configuration
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 22:15
 * @Version 1.0
 **/

import cn.hutool.core.util.StrUtil;
import com.yongzhiai.core.node.PeerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * 节点所属Raft集群的配置信息
 */
public class Configuration {

    private static final Logger LOG= LoggerFactory.getLogger(Configuration.class);

    /**
     * Raft集群的名称
     */
    private final String groupName;

    /**
     * Raft集群中领导者的信息
     */
    private PeerId leaderId=new PeerId();

    /**
     * 当前Raft集群中所有节点的信息
     */
    private final HashSet<PeerId> peers=new HashSet<>();


    public Configuration(String groupName, PeerId leaderId,Iterator<PeerId> iterator) {

        if(StrUtil.isBlank(groupName) || leaderId==null || iterator==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        this.groupName = groupName;
        this.leaderId = leaderId;

        while(iterator.hasNext()){
            this.peers.add(iterator.next());
        }
    }

    public Configuration(String groupName) {

        if(StrUtil.isBlank(groupName)){
            throw new IllegalArgumentException("参数不能为空");
        }
        this.groupName = groupName;
    }


    public Configuration(String groupName, Iterator<PeerId> iterator){
        if(StrUtil.isBlank(groupName)|| iterator==null){
            throw new IllegalArgumentException("参数不能为空");
        }

        this.groupName=groupName;

        while(iterator.hasNext()){
            this.peers.add(iterator.next());
        }
    }





    public String getGroupName() {
        return groupName;
    }

    public PeerId getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(PeerId leaderId) {
        this.leaderId = leaderId;
    }

    public HashSet<PeerId> getPeers() {
        return peers;
    }


    public void setPeers(HashSet<PeerId> peers) {
        this.peers.clear();
        this.peers.addAll(peers);
    }


    public boolean addPeer(PeerId serverId){
        return this.peers.add(serverId);
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "groupName='" + groupName + '\'' +
                ", leaderId=" + leaderId +
                ", peers=" + peers +
                '}';
    }
}
