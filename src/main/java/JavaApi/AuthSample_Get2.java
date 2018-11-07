package JavaApi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用错误的权限信息的ZooKeeper会话访问含权限信息的数据节点
 */
public class AuthSample_Get2 {

    final static String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper1 = new ZooKeeper("localhost:2181",
                5000,
                null);
        zooKeeper1.addAuthInfo("digest","fool:true".getBytes());
        zooKeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper2 = new ZooKeeper("localhost:2181",
                5000,
                null);
        zooKeeper2.addAuthInfo("digest","fool:true".getBytes());
        System.out.println(zooKeeper2.getData(PATH,false,null));

        ZooKeeper zooKeeper3 = new ZooKeeper("localhost:2181",
                5000,
                null);
        zooKeeper3.addAuthInfo("digest","fool:false".getBytes());
        zooKeeper3.getData(PATH,false,null);
    }
}
