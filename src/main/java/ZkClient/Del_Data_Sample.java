package ZkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * ZkClient删除节点数据
 */
public class Del_Data_Sample {
    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("localhost:2181",2000);
        zkClient.deleteRecursive(path);
    }
}
