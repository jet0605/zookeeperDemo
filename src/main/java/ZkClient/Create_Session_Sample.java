package ZkClient;

import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;

/**
 * 使用ZKClient传来创建一个Zookeeper客户端
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws IOException,InterruptedException{
        ZkClient zkClient = new ZkClient("localhost:2181",5000);
        System.out.println("ZooKeeper session established");
    }
}
