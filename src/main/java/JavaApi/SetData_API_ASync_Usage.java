package JavaApi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper API 跟新节点数据内容,使用异步(async)接口
 */
public class SetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception{

        String path = "/zk-book3";
        zk = new ZooKeeper("localhost:2181",
                5000,
                new SetData_API_ASync_Usage());
        connectedSemaphore.await();
        zk.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.setData(path,"456".getBytes(),-1,new IStatCallback(),null);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }
        }
    }
}
class IStatCallback implements AsyncCallback.StatCallback{

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(rc == 0){
            System.out.println("SUCCESS");
        }
    }
}
