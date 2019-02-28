package org.pomelo.framework.mqclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MQSendAndReceiveUtil {  
    private MQQueueManager qManager;  
    private MQQueue queue;  
  
    private static String qmManager = "liyytest";// 队列管理器名称  
    private static String remoteQName = "RQ_88888888";// 远程队列名称  
    private static String localQName = "LIYYQUEUE";// 本地队列  
    private static String hostname = "127.0.0.1";// 本机名称  
    private static String channel = "SERVERCON";// 服务器链接通道  
    private static int ccsid = 1381;  
    private static int port = 1414;  
  
    
    
    @SuppressWarnings("unchecked")  
    private MQSendAndReceiveUtil() {  
        MQEnvironment.hostname = hostname;  
        MQEnvironment.channel = channel;  
        MQEnvironment.CCSID = ccsid;  
        MQEnvironment.port = port;  


        //MQ中拥有权限的用户名
        MQEnvironment.userID = "MUSR_MQADMIN";
        //用户名对应的密码
        MQEnvironment.password = "liyy123456";

        
        
        
        MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,  
                MQC.TRANSPORT_MQSERIES);  
  
        try {  
            qManager = new MQQueueManager(qmManager);  
        } catch (MQException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     *  
     * Description:如果队列管理器为空，建立 
     *  
     * @param: 
     * @return: void 
     * @exception Exception. 
     * @author listening created at Nov 8, 2009 
     */  
    private void createConnection() {  
        if (qManager == null) {  
            new MQSendAndReceiveUtil();  
        }  
    }  
  
      
    /** 
     *  
     * Description:发送文件 
     *  
     * @param:String fileName -文件名 
     * @return: void 
     * @exception Exception. 
     * @author listening created at Nov 8, 2009 
     */  
    public void sendFileMessage(String fileName) {  
        this.createConnection();  
        try {  
            int openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;// 建立打开方式  
  
            queue = qManager.accessQueue(localQName, openOptions, null, null,  
                    null);// 连接队列（发送时此队列为发送方的远程队列）  
  
            MQPutMessageOptions pmo = new MQPutMessageOptions();// 创建消息放入方式实例  
  
            MQMessage message = new MQMessage();// 创建MQ消息实例  
  
            FileBean file = new FileBean();// 创建FileBean对象实例并赋值  
  
            file.setFileName(fileName);  
  
            InputStream in = new FileInputStream("C:\\" + fileName); // 输入流读取要发送的文件  
  
            BASE64Encoder encoder = new BASE64Encoder();// 创建BASE64Encoder编码实例  
  
            byte[] data = new byte[in.available()];  
  
            in.read(data);  
  
            String content = encoder.encode(data);// 编码文件 得到String  
  
            file.setFileContent(content);  
  
            message.writeObject(file);// 将FileBean实例 file放入消息中发送  
  
            queue.put(message, pmo);  
  
            qManager.commit();  
            this.logInfo("文件发送成功");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            this.closeAction();  
        }  
  
    }  
  
    public void receiveFileMessage() {  
        try {  
            int openOptions = MQC.MQOO_INPUT_SHARED  
                    | MQC.MQOO_FAIL_IF_QUIESCING;// 建立队列打开方式  
  
            queue = qManager.accessQueue(localQName, openOptions, null, null,  
                    null);// 连接队列（接收时队列名为接收方的本地队列）  
  
            MQGetMessageOptions gmo = new MQGetMessageOptions();  
  
            gmo.options = gmo.options + MQC.MQGMO_SYNCPOINT;// 同步接收  
  
            gmo.options = gmo.options + MQC.MQGMO_WAIT;// 没消息等待  
  
            gmo.options = gmo.options + MQC.MQGMO_FAIL_IF_QUIESCING;// 停顿则失败  
  
            gmo.waitInterval = 100;// 等待间隔  
  
            MQMessage inMsg = new MQMessage();// 创建消息实例  
  
            queue.get(inMsg, gmo);// 从队列中拿出消息  
  
            FileBean fileBean = new FileBean();  
  
            fileBean = (FileBean) inMsg.readObject(); // 读取消息强转为FileBean类型  
  
            String content = fileBean.getFileContent();// 取文件内容  
  
            BASE64Decoder decoder = new BASE64Decoder();// 建立解码类实例  
  
            byte[] contentArray = decoder.decodeBuffer(content);// 解码生成byte数组  
  
            String path = "C:\\WordFiles\\" + fileBean.getFileName();  
  
            FileOutputStream out = new FileOutputStream(new File(path));// 调动输出流把文件写到指定的位置  
  
            out.write(contentArray, 0, contentArray.length);  
  
            // System.out.print(fileBean.getFileName());  
  
            qManager.commit();// 提交事务  
  
            this.logInfo("文件接收成功，请注意查收");// 打印日志  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     *  
     * Description: 释放资源 
     *  
     * @param: 
     * @return: 
     * @exception Exception. 
     * @author listening created at Nov 8, 2009 
     */  
    public void closeAction() {  
        try {  
            if (queue != null) {  
                queue.close();  
                queue = null;  
            } else if (qManager != null) {  
                qManager.close();  
                qManager = null;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    /** 
     *  
     * Description:打印成功日志信息 
     *  
     * @param:String message-日志内容 
     * @return: void 
     * @exception Exception. 
     * @author listening created at Nov 8, 2009 
     */  
    public void logInfo(String message) {  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");  
        System.out.println(format.format(new Date()) + "-------" + message  
                + "+-------------");  
    }  
  
    /** 
     *  
     * Description:main函数测试 
     *  
     * @param: 
     * @return: void 
     * @exception Exception. 
     * @author listening created at Nov 8, 2009 
     */  
    public static void main(String[] args) {  
        new MQSendAndReceiveUtil().sendFileMessage("test.xml");  
        new MQSendAndReceiveUtil().receiveFileMessage();  
    }  
  
}  