import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper API 获取子节点列表,使用异步（async）接口
 */
public class ZooKeeper_GetChildren_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{

        String path = "/zk-book2";
        zk = new ZooKeeper("localhost:2181",
                5000,
                new ZooKeeper_GetChildren_API_ASync_Usage());
        connectedSemaphore.await();
        zk.create(path,
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create(path+"/c1",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        zk.getChildren(path,true,new IChildren2Callback(),null);

        zk.create(path+"/c2",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && watchedEvent.getPath() == null){
                connectedSemaphore.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try{
                    System.out.println("ReGetChild: " + zk.getChildren(watchedEvent.getPath(),true));
                }catch (Exception e){}
            }
        }
    }
}
class IChildren2Callback implements AsyncCallback.Children2Callback{

    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Get Children znode result : [response code: " + rc + ", param path: " + path + ", ctx: " + ctx + ", children list: " + children + ", stat: " + stat);
    }
}
