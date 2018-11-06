import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper API 获取节点数据,使用同步(sync)接口
 */
public class GetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception{

        String path = "/zk-book3";
        zk = new ZooKeeper("localhost:2181",
                5000,
                new GetData_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create(path,
                "123".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println(new String(zk.getData(path,true,stat)));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());

        zk.setData(path,"123".getBytes(),-1);
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                try{
                    System.out.println(new String(zk.getData(watchedEvent.getPath(),true,stat)));
                    System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
                }catch (Exception e) {}
            }
        }
    }
}
