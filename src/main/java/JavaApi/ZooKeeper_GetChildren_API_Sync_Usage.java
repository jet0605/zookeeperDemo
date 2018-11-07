package JavaApi;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper获取子节点列表,使用同步(sync)接口
 */
public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zk = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_GetChildren_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create(path,
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create(path+"/c1",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        List<String> childrenList = zk.getChildren(path,true);
        System.out.println(childrenList);

        zk.create(path+"/c2",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try{
                    System.out.println("ReGetChild: " + zk.getChildren(watchedEvent.getPath(),true));
                }catch (Exception e){ }
            }
        }
    }
}
