package ZkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 使用ZKclient创建节点
 */
public class Create_Node_Sample {
    public static void main(String[] args) throws Exception{
        ZkClient zkClient = new ZkClient("localhost:2181",5000);
        String path = "/zk-book/c1";
        zkClient.createPersistent(path,true);
    }
}
