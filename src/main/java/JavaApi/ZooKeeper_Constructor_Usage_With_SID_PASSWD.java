package JavaApi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建一个复用sessionId 和 sessionPasswd的例子
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        connectedSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        //Use illegal sessionId and sessionPassWd
        //使用不正确的sessionId和sessionPassWd
        zooKeeper = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                1l,
                "test".getBytes());
        //Use correct sessionId and SessionPassWd
        //使用正确的sessionId和SessionPassWd
        zooKeeper = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                sessionId,
                passwd);
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event: " + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectedSemaphore.countDown();
        }
    }
}
