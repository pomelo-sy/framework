package org.pomelo.framework.mqclient;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

public class WinLinuxPut {

	//private static final String HOSTNAME = "127.0.0.1";
	private static final String HOSTNAME = "192.168.1.117";

	private static final int PORT = 1414;

	//private static final String CHANNEL = "SYSTEM.DEF.SVRCONN";
	private static final String CHANNEL = "SYSTEM.ADMIN.SVRCONN";

	private static final int CCSID = 1208;

	//we can also specify FULL_QM2 as queue manager

	private static final String QM_NAME = "M2MQM2";

	private static final String Q_NAME = "SOAPIN";

	public static void main(String[] args) throws Exception {

	// Set up WebSphere MQ environment

	MQEnvironment.hostname = HOSTNAME;

	MQEnvironment.port = PORT;

	MQEnvironment.channel = CHANNEL;

	
	
	
	 //MQ中拥有权限的用户名
    MQEnvironment.userID = "mqm";
    //用户名对应的密码
    MQEnvironment.password = "liyy123456";
    
    
    
    
    
	// Create a connection to the QueueManager

	MQQueueManager qMgr = new MQQueueManager(QM_NAME);

	// Specify the queue that we wish to open and the open options.

	//MQOO_BIND_AS_Q_DEF option is specified here, so bind type is determined by

	//default queue bind type

	// We can also specify MQOO_BIND_ON_OPEN or

	//MQOO_BIND_NOT_FIXED to cover

	//default queue bind type

	MQQueue queue = qMgr.accessQueue(Q_NAME,

	MQConstants.MQOO_BIND_AS_Q_DEF

	+ MQConstants.MQOO_OUTPUT);

	// Define a simple WebSphere MQ Message and write some text in UTF8 format

	MQMessage msg = new MQMessage();
	
	msg.writeUTF("Hello IBM MQ stop service 22222!");

	// Put five messages to the cluster queue with default put message options

	queue.put(msg, new MQPutMessageOptions());

	queue.put(msg, new MQPutMessageOptions());

	queue.put(msg, new MQPutMessageOptions());

	queue.put(msg, new MQPutMessageOptions());

	queue.put(msg, new MQPutMessageOptions());

	// Close the queue and disconnect from the QueueManager

	queue.close();

	qMgr.disconnect();

	}

	}


