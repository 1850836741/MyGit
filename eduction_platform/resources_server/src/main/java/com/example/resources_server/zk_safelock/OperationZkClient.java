package com.example.resources_server.zk_safelock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OperationZkClient {

    @Autowired
    CuratorFramework zkClient;

    /**
     * 创建临时顺序节点
     * @param nodePath
     * @param nodeValue
     * @return
     * @throws Exception
     */
    public String creatBriefNode(String nodePath, String nodeValue) throws Exception {
        String pathName = zkClient.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(nodePath,nodeValue.getBytes());
        return pathName;
    }

    /**
     * 强制删除节点
     * @param nodePath
     * @throws Exception
     */
    public void deleteBriefNode(String nodePath) throws Exception {
        zkClient.delete()
                .guaranteed()
                .forPath(nodePath);
    }

    /**
     * 获取节点内容
     * @param nodePath
     * @return
     * @throws Exception
     */
    public String getNodeValue(String nodePath) throws Exception {
        return new String(zkClient.getData().forPath(nodePath),"utf-8");
    }

    /**
     * 修改节点内容
     * @param nodePath
     * @param nodeValue
     * @throws Exception
     */
    public void updateNode(String nodePath, String nodeValue) throws Exception {
        zkClient.setData().forPath(nodePath, nodeValue.getBytes());
    }


    /**
     * 确认节点是否存在
     * @param nodePath
     * @return
     * @throws Exception
     */
    public boolean checkNodeExist(String nodePath) throws Exception {
        return zkClient.checkExists().forPath(nodePath)==null?false:true;
    }

    /**
     * 获取路径下的所有节点名字
     * @param nodePath
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String nodePath) throws Exception {
    return zkClient.getChildren().forPath(nodePath);
}

    /**
     * 对指定节点进行监听
     * @param nodePath
     * @throws Exception
     */
    public NodeCache addWatch(String nodePath, CountDownLatch countDownLatch) throws Exception {
        if (!checkNodeExist(nodePath)){
            return null;
        }
        NodeCache nodeCache = new NodeCache(zkClient,nodePath);
        NodeCacheListener listener = ()->{
            ChildData data = nodeCache.getCurrentData();
            if (data == null){
                countDownLatch.countDown();
            }
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
        return nodeCache;
    }
}