package com.wanmi.sbc.common.util.auth;


import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

@Slf4j
public class SecretClassLoader extends ClassLoader{

	 public SecretClassLoader(){
	        
	    }
	    
	    public SecretClassLoader(ClassLoader parent){
	        super(parent);
	    }

	    @Override
	    protected Class<?> findClass(String name) throws ClassNotFoundException{
			try {
				if(!name.equals(new String(Type.type,"UTF-8"))){
				   return Class.forName(name);
			   }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try{
	            byte[] bytes = getClassBytesFromEncode();
	            Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
	            return c;
	        } 
	        catch (Exception e){
	            e.printStackTrace();
	        }
	        
	        return super.findClass(name);
	    }
	    
	    private byte[] getClassBytesFromEncode() throws Exception{
			InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream("auth_wanmi_110.pin");
			byte[] data = AuthHelp.getByte(fileStream);
			fileStream.close();
			byte[] processData = AuthHelp.getProcessByte(data);
			Class<?> c = this.defineClass(new String(Type.sName,"UTF-8"), processData, 0, processData.length);
			int size = processData.length+AuthHelp.size;
			byte[] secretData = new byte[data.length-size];
			System.arraycopy(data,size,secretData,0,secretData.length);
			AbstractSecret abstractSecret = (AbstractSecret) c.newInstance();
			return abstractSecret.action(secretData);

	    }



}
