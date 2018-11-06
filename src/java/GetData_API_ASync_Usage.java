import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper API获取节点数据内容,使用异步（async）接口
 */
public class GetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception{

        String path = "/zk-book3";
        zk = new ZooKeeper("localhost:2181",
                5000,
                new GetData_API_ASync_Usage());
        connectedSemaphore.await();

        zk.create(path,
                "123".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        zk.getData(path,true,new IDataCallback(),null);
        zk.setData(path,"123".getBytes(),-1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                try{
                    zk.getData(watchedEvent.getPath(),true,new IDataCallback(),null);
                }catch (Exception e){ }
            }
        }
    }
}
class IDataCallback implements AsyncCallback.DataCallback{

    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println(rc + ", " + path + ", " + new String(data));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
    }
}
