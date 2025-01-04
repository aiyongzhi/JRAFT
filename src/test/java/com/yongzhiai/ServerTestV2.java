package com.yongzhiai;

import cn.hutool.core.collection.CollectionUtil;
import com.yongzhiai.core.config.Configuration;
import com.yongzhiai.core.node.*;
import com.yongzhiai.core.node.impl.NodeImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName ServerTestV2
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/4 12:10
 * @Version 1.0
 **/


public class ServerTestV2 {

    private static final String groupName="cluster:1";


    private NodeOptions nodeOptions;


    private Configuration conf;

    @Before
    public void initConf(){
        PeerId node1=new PeerId();
        node1.setEndpoint(new Endpoint("127.0.0.1",8001));
        node1.setGroupName(groupName);

        PeerId node2=new PeerId();
        node2.setEndpoint(new Endpoint("127.0.0.1",8002));
        node2.setGroupName(groupName);

        PeerId node3=new PeerId();
        node3.setEndpoint(new Endpoint("127.0.0.1",8003));
        node3.setGroupName(groupName);

        conf=new Configuration(groupName);

        conf.addPeer(node1);
        conf.addPeer(node2);
        conf.addPeer(node3);
    }


    @Before
    public void initNodeOptions(){

        String logPath="/jraft/data/log";
        String raftMetaPath="/jraft/data/meta";


        nodeOptions=new NodeOptions(logPath,raftMetaPath,NodeOptions.DEFAULT_ELECTION_TIMEOUT_MILLS,
                conf);
    }


    @Test
    public void testServer(){
        PeerId serverId=new PeerId(new Endpoint("127.0.0.1",8001),new NodeId(1,groupName),groupName,true);

        Node node1=new NodeImpl(groupName,serverId);

        boolean initialize = node1.initialize(nodeOptions);
        if(initialize){
            System.out.println("当前节点初始化成功");
        }else {
            System.out.println("当前节点初始化失败");
        }

        System.out.println("-----------------------");
        System.out.println(node1);


    }
}
