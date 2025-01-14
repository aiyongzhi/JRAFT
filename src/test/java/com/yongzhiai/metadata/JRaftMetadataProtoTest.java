package com.yongzhiai.metadata;

import com.yongzhiai.core.node.Endpoint;
import com.yongzhiai.core.node.PeerId;
import com.yongzhiai.election.VoteInfo;
import com.yongzhiai.proto.JRaftMetadataProto;
import org.junit.Test;

import java.io.*;

/**
 * @ClassName JRaftMetadataProtoTest
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/13 21:46
 * @Version 1.0
 **/
public class JRaftMetadataProtoTest {

    @Test
    public void testDeserialize() throws IOException {

        File file=new File("C:\\Users\\11425\\Desktop\\java-source-projects\\jraft\\src\\main\\resources\\metadata\\jraft_metadata");

        BufferedInputStream inputStream=new BufferedInputStream(new FileInputStream(file));

        JRaftMetadataProto.JRaftMetadata raftMetadata = JRaftMetadataProto.JRaftMetadata.parseFrom(inputStream);

        System.out.println(raftMetadata);
    }


    @Test
    public void testWriteToFile(){
        File file=new File("C:\\Users\\11425\\Desktop\\java-source-projects\\jraft\\src\\main\\resources\\metadata\\jraft_metadata");

        VoteInfo voteInfo=new VoteInfo();
        voteInfo.getCurrTerm().incrementAndGet();

        PeerId peerId=new PeerId(new Endpoint("localhost",8001),
                "test-group",false);

        voteInfo.setVoteId(peerId);

        RaftMetadataInfo raftMetadataInfo=new RaftMetadataInfo(voteInfo);

        boolean ans = raftMetadataInfo.writeToFile(file);

        System.out.println("写入文件结果:"+ans);
    }


}
