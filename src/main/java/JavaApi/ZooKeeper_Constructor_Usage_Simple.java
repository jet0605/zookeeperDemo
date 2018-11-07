package JavaApi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper客户端连接服务端简单例子
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher {
    private static CountDownLatch connectedSemaphora = new CountDownLatch(1);
    //线程计数器工具类
    //调用awaait方法直到计数为指定的值的时候取消阻塞
    //countDown方法相当于计数器++

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_Constructor_Usage_Simple());
        System.out.println(zooKeeper.getState());
        try{
            connectedSemaphora.await();
        }catch (InterruptedException e){}
        System.out.println("ZooKeeper session established.");
    }
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event: " + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectedSemaphora.countDown();
        }
    }
}
