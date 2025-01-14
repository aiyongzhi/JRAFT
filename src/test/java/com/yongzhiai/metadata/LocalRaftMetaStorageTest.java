package com.yongzhiai.metadata;

/**
 * @ClassName LocalRaftMetaStorageTest
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/13 21:17
 * @Version 1.0
 **/

import com.yongzhiai.core.node.Endpoint;
import com.yongzhiai.core.node.PeerId;
import com.yongzhiai.core.node.impl.NodeImpl;
import com.yongzhiai.election.VoteInfo;
import com.yongzhiai.metadata.impl.LocalRaftMetaStorage;
import org.junit.Before;
import org.junit.Test;

/**
 * raft本地元数据存储器测试
 */
public class LocalRaftMetaStorageTest {


    private RaftMetaStorage raftMetaStorage;


    @Before
    public void initialize(){

        PeerId serverId=new PeerId(new Endpoint("localhost",8002),
                "test-group");

        raftMetaStorage=new LocalRaftMetaStorage("C:\\Users\\11425\\Desktop\\java-source-projects\\jraft\\src\\main\\resources\\metadata",
                new NodeImpl("test-group",serverId));

        raftMetaStorage.initialize(new RaftMetaStorageOptions());
    }


    @Test
    public void testSave(){
        RaftMetadataInfo raftMetadataInfo = raftMetaStorage.getMetadata();


        VoteInfo voteInfo = raftMetadataInfo.getVoteInfo();
        voteInfo.getCurrTerm().incrementAndGet();

        PeerId peerId=new PeerId(new Endpoint("localhost",8001),
                "test-group",false);

        voteInfo.setVoteId(peerId);


        boolean saved = raftMetaStorage.save();
        System.out.println("save()方法返回结果:"+saved);

    }
}
