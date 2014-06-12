package tv.joyplus.backend.report.memcache;


import java.util.Date;
import java.util.List;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;


public class MemCached
{
    // 创建全局的唯一实例
	private MemCachedClient mcc;
	
	private int cache_time;
    

    public MemCached(String[] servers, Integer[] weights, int time)
    {
//        // 获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();

        // 设置服务器信息   
        pool.setServers(servers);
        pool.setWeights(weights);
        // 初始化连接池
        pool.initialize();

        this.cache_time = time;
        mcc = new MemCachedClient();
        // 压缩设置，超过指定大小（单位为K）的数据都会被压缩
        mcc.setCompressEnable( true );
        mcc.setCompressThreshold( 64 * 1024 );
    }

    
    /**
     * 添加一个指定的值到缓存中.
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value)
    {
        return mcc.add(key, value, new Date(System.currentTimeMillis()+cache_time));
    }
    
    public boolean add(String key, Object value, Date expiry)
    {
        return mcc.add(key, value, expiry);
    }
    
    public boolean replace(String key, Object value)
    {
        return mcc.replace(key, value, new Date(System.currentTimeMillis()+cache_time));
    }
    
    public boolean replace(String key, Object value, Date expiry)
    {
        return mcc.replace(key, value, expiry);
    }
    
    /**
     * 根据指定的关键字获取对象.
     * @param key
     * @return
     */
    public Object get(String key)
    {
        return mcc.get(key);
    }
    
//    public static void main(String[] args)
//    {
//    	String[] server = {"localhost:11211"};
//    	Integer[] w = {3};
//        MemCached cache = new MemCached(server, w, 1);
////        cache.add("hello3", 234, new Date(System.currentTimeMillis()+5000));
//        System.out.println("get value hello: " + cache.get("hello3"));
////        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
////        Map<String, Object> map = new HashedMap();
////        map.put("a", "abcdefg11111111111");
////        list.add(map);
////        cache.add("list", list);
////        System.out.println("get value hello: " + cache.get("hello"));
////        List<Map<String, Object>> list_1 = (List<Map<String, Object>>) cache.get("list");
////        System.out.println("get value list.a " + list_1.get(0).get("a"));
//    }


	public int getCache_time() {
		return cache_time;
	}


	public void setCache_time(int cache_time) {
		this.cache_time = cache_time;
	}
}