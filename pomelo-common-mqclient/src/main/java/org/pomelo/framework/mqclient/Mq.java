package org.pomelo.framework.mqclient;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class Mq {
    static MQQueueManager qMgr;
    static int CCSID = 1381;
    static String queueString = "LIYYQUEUE";

    public static void connect() throws MQException {
        MQEnvironment.hostname = "127.0.0.1";
        MQEnvironment.channel = "SERVERCON";
        MQEnvironment.port = 1414;
        MQEnvironment.CCSID = CCSID;
        //MQ中拥有权限的用户名
        MQEnvironment.userID = "MUSR_MQADMIN";
        //用户名对应的密码
        MQEnvironment.password = "liyy123456";

        qMgr = new MQQueueManager("liyytest");
        

    }

    public static void sendMsg(String msgStr) {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = qMgr.accessQueue(queueString, openOptions, null, null, null);
            MQMessage msg = new MQMessage();// 要写入队列的消息
            msg.format = MQC.MQFMT_STRING;
            msg.characterSet = CCSID;
            msg.encoding = CCSID;
            // msg.writeObject(msgStr); //将消息写入消息对象中
            msg.writeString(msgStr);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            msg.expiry = -1; // 设置消息用不过期
            queue.put(msg, pmo);// 将消息放入队列
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void receiveMsg() {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            queue = qMgr.accessQueue(queueString, openOptions, null, null, null);
            System.out.println("该队列当前的深度为:" + queue.getCurrentDepth());
            System.out.println("===========================");
            int depth = queue.getCurrentDepth();
            // 将队列的里的消息读出来
            while (depth-- > 0) {
                MQMessage msg = new MQMessage();// 要读的队列的消息
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                queue.get(msg, gmo);
                System.out.println("消息的大小为：" + msg.getDataLength());
                System.out.println("消息的内容：\n" + msg.readStringOfByteLength(msg.getDataLength()));
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws MQException {
        connect();
        sendMsg("我来测试一下");
        receiveMsg();
    }
}