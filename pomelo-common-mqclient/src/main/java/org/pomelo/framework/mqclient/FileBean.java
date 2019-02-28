package org.pomelo.framework.mqclient;

import java.io.Serializable;  
/** 
 *  
 * <p> 
 * Title: FileBean.java 
 * </p> 
 * <p> 
 * Description: 
 * </p> 
 * <p> 
 * Copyright: Copyright (c) 2009 
 * </p> 
 * <p> 
 * Company: shunde 
 * </p> 
 *  
 * @author: listening 
 * @create date Nov 8, 2009 
 */  
public class FileBean implements Serializable {  
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
  
    private String fileName = "";// 文件名  
  
    private String fileContent = "";// 文件内容（BASE64Encoder编码之后的）  
  
    public String getFileName() {  
        return fileName;  
    }  
  
    public void setFileName(String fileName) {  
        this.fileName = fileName;  
    }  
  
    public String getFileContent() {  
        return fileContent;  
    }  
  
    public void setFileContent(String fileContent) {  
        this.fileContent = fileContent;  
    }  
  
}  
