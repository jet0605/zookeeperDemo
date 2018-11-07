package ZkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 使用ZkClient获取节点数据内容
 */
public class Get_Data_Sample {
    public static void main(String[] args) throws Exception{

        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("localhost:2181",5000);
        zkClient.createEphemeral(path,"123");

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("Node " + dataPath + " changed, new data: " + data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("Node " + dataPath + " deleted ");
            }
        });

        System.out.println(zkClient.readData(path));
        zkClient.writeData(path,"456");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
