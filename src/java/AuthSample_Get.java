import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 使用无权限信息的ZooKeeper会话访问含权限信息的数据节点
 */
public class AuthSample_Get {

    final static String PATH = "/zk-book-auth-test";

    public static void main(String[] args) throws Exception{

        ZooKeeper zooKeeper1 = new ZooKeeper("localhost:2181",
                5000,
                null);
        zooKeeper1.addAuthInfo("digest","fool:true".getBytes());
        zooKeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper2 = new ZooKeeper("localhost:2181",
                50000,
                null);
        String str = new String(zooKeeper2.getData(PATH,false,null));
        System.out.println(str);
    }
}
