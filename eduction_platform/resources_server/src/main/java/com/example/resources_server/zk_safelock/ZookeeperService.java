package com.example.resources_server.zk_safelock;

import com.example.resources_server.tool.CloseTool;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZookeeperService {

    @Autowired
    OperationZkClient operationZkClient;

    /**
     * 在zookeeper中添加读节点
     * @param pathName
     * @return
     */
    public String addReadLock(String pathName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pathName).append(LockConfig.READ);
        String path = null;
        try {
            path = operationZkClient.creatBriefNode(stringBuilder.toString(),"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 在zookeeper中添加写节点
     * @param pathName
     * @return
     */
    public String addWriteLock(String pathName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pathName).append(LockConfig.WRITE);
        String path = null;
        try {
            path = operationZkClient.creatBriefNode(stringBuilder.toString(),"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取读锁
     * @param pathName
     * @return
     */
    public String getReadLock(String pathName){
        boolean isSuccess = false;
        String lockName = addReadLock(pathName);
        try {
            while (!isSuccess){
                List<String> childrenList = operationZkClient.getChildren(lockName.substring(0,lockName.lastIndexOf("/")));
                int lockIndex = -1;
                for (lockIndex = childrenList.size()-1; lockIndex>=0&&childrenList.get(lockIndex).equals(lockName); lockIndex--);
                isSuccess = lockIndex == 0;
                if (!isSuccess){
                    int writeLockIndex = -1;
                    for (writeLockIndex = childrenList.size()-1; writeLockIndex>=0&&childrenList.get(writeLockIndex).contains("write"); writeLockIndex--);
                    //判断该读锁前面是否有写锁
                    if (writeLockIndex < 0){
                        isSuccess = true;
                    }else {
                        CountDownLatch countDownLatch = new CountDownLatch(1);


                        //可能有问题!!
                        NodeCache nodeCache = operationZkClient.addWatch(childrenList.get(writeLockIndex),countDownLatch);
                        if (nodeCache != null){
                            countDownLatch.await();
                            CloseTool.Close(nodeCache);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lockName;
    }

    /**
     * 获取写锁
     * @param pathName
     * @return
     */
    public String getWriteLock(String pathName){
        boolean isSuccess = false;
        String lockName = addWriteLock(pathName);
        try {
            while (!isSuccess){
                List<String> childrenList = operationZkClient.getChildren(lockName.substring(0,lockName.lastIndexOf("/")));
                int lockIndex = -1;

                for (lockIndex = childrenList.size()-1; lockIndex>=0&&childrenList.get(lockIndex).equals(lockName); lockIndex--);
                isSuccess = lockIndex == 0;
                if (!isSuccess){
                    CountDownLatch countDownLatch = new CountDownLatch(1);


                    //可能有问题!!
                    NodeCache nodeCache = operationZkClient.addWatch(childrenList.get(lockIndex-1),countDownLatch);
                    if (nodeCache != null){
                        countDownLatch.await();
                        CloseTool.Close(nodeCache);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lockName;
    }

    /**
     * 释放锁
     * @param pathName
     */
    public void deleteLock(String pathName){
        try {
            operationZkClient.deleteBriefNode(pathName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}