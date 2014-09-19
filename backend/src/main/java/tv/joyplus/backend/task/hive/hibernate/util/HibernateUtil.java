package tv.joyplus.backend.task.hive.hibernate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.ConfigHelper;

public class HibernateUtil {
	static SessionFactory   sessionFactory  = null;
	
	public static void initSessionFactory(){
		try{
			final Properties properties = new Properties();  
            final InputStream fis = ConfigHelper.getResourceAsStream( "config.properties" ); //config.properties 文件对象，里面有数据库的连接信息，  
            properties.load(fis);  
            fis.close(); //关流  
            final String dbdriver = properties.getProperty("addata.jdbc.driverClassName"); //密码(从config.properties读) 
            final String dburl = properties.getProperty("addata.jdbc.url"); //数据库路径(从config.properties读)  
            final String dbuser = properties.getProperty("addata.jdbc.username"); //用户名(从config.properties读)  
            final String dbpw = properties.getProperty("addata.jdbc.password"); //密码(从config.properties读)  
  
            final Properties extraProperties = new Properties();  
            extraProperties.setProperty("hibernate.connection.driver_class", dbdriver);
            extraProperties.setProperty("hibernate.connection.url", dburl);  
            extraProperties.setProperty("hibernate.connection.username", dbuser);  
            extraProperties.setProperty("hibernate.connection.password", dbpw);  
  
            final Configuration cfg = new Configuration();  
            cfg.configure("hibernate.cfg.xml"); //路径可以改变  
            cfg.addProperties(extraProperties);  
            sessionFactory = cfg.buildSessionFactory();  
        }  
        catch (final UnsupportedEncodingException e)  
        {  
            //不支持字符编码。  
            e.printStackTrace();  
        }  
        catch (final FileNotFoundException e)  
        {  
            //config.properties文件没找到  
            e.printStackTrace();  
        }  
        catch (final HibernateException e)  
        {  
            //cfg.configure("hibernate.cfg.xml");时异常  
            e.printStackTrace();  
        }  
        catch (final Exception e)  
        {  
            //创建SessionFactory 异常  
            e.printStackTrace();  
        }  
	}
	
	public static SessionFactory getSessionFactory()  
    {  
        return sessionFactory;  
    }
	
	/** 
     * 取得session 
     *  
     * @return session 
     */  
    public static Session getSeesion()  
    {  
    	if(sessionFactory != null){
    		return sessionFactory.openSession();  
    	}else{
    		return null;
    	}
        
    }
    
    public static void closeFactory(){
    	if(sessionFactory != null){
    		sessionFactory.close();
    	}
    }
}
