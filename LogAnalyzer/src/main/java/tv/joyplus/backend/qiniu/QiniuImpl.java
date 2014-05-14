package tv.joyplus.backend.qiniu;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.Entry;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rs.URLUtils;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;

public class QiniuImpl implements QiniuDao{
	private final static int LIMIT = 10;
	private String accessKey;
	private String secretKey;
	private String bucket;
	private String domain;
	
	public static void main(String[] args) throws Exception {
		QiniuImpl q = new QiniuImpl();
		q.setAccessKey("8u_HvPsdYCou2rzClcxDotMR_Qm669cBRveVZVQv");
		q.setSecretKey("cmLJwA2hwcQDejRgzFLRQIigwevCgYrk30EEiMjs");
		q.setDomain("zinotest.qiniudn.com");
		q.download("infopush.debug.log.2014-05-05-08", "/data/1.log");
	}
	
	@Override
	public void download(String key, String filepath) throws Exception {
		String url = downloadUrl(key);
		File file = new File(filepath);
		FileUtils.copyURLToFile(new URL(url), file);
	}

	@Override
	public List<QiniuItem> list() {
		Mac mac = new Mac(accessKey, secretKey);
		RSFClient client = new RSFClient(mac);
		String marker = "";
		List<ListItem> all = new ArrayList<ListItem>();
		ListPrefixRet ret = null;
		while (true) {
			ret = client.listPrifix(bucket, "", marker, LIMIT);
			marker = ret.marker;
			all.addAll(ret.results);
			if (!ret.ok()) {
				// no more items or error occurs
				break;
			}
		}
		
		List<QiniuItem> list = new ArrayList<QiniuItem>();
		for(ListItem item : all) {
			QiniuItem qiniuItem = new QiniuItem(item.key, item.hash, item.fsize, 
					item.putTime, item.mimeType, item.endUser);
			list.add(qiniuItem);
		}
		return list;
	}

	@Override
	public QiniuItem stat(String key) {
		Mac mac = new Mac(accessKey, secretKey);
        RSClient client = new RSClient(mac);
        Entry statRet = client.stat(bucket, key);
        if(statRet.ok()) {
        	return new QiniuItem(key, statRet.getHash(), statRet.getFsize(), 
        			statRet.getPutTime(), statRet.getMimeType(), "");
        }
		return null ;
	}

	private String downloadUrl(String key) throws Exception {
		Mac mac = new Mac(accessKey, secretKey);
		String baseUrl = URLUtils.makeBaseUrl(domain, key);
		GetPolicy getPolicy = new GetPolicy();
        String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
        return downloadUrl;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	
}
