package tv.joyplus.backend.qiniu;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.Entry;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rs.URLUtils;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tv.joyplus.backend.exception.TaskException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by zino on 14-7-7.
 */
public class QiniuConfig {
    private static Log log = LogFactory.getLog(QiniuConfig.class);
    private final static int LIMIT = 0;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;

    public QiniuConfig() {

    }
    public QiniuConfig(String businessId, Properties prop) throws TaskException {
        log.debug("new qiniu config->"+businessId);
        if(prop!=null) {
            accessKey = prop.getProperty(businessId.toLowerCase() + ".qiniu.accessKey", null);
            secretKey = prop.getProperty(businessId.toLowerCase() + ".qiniu.secretKey", null);
            bucket = prop.getProperty(businessId.toLowerCase() + ".qiniu.bucket", null);
            domain = prop.getProperty(businessId.toLowerCase() + ".qiniu.domain", null);
        }
        log.debug(accessKey+secretKey+bucket+domain);
        if(prop==null || accessKey==null || secretKey==null || bucket==null || domain==null) {
            throw new TaskException("qiniu properties error!");
        }
    }
    public void download(String url, File file) throws Exception {
        log.debug("qiniu download url:"+url);
        FileUtils.copyURLToFile(new URL(url), file);
    }

    public List<QiniuItem> list(String prifix) {
        Mac mac = new Mac(accessKey, secretKey);
        RSFClient client = new RSFClient(mac);
        String marker = "";
        List<ListItem> all = new ArrayList<ListItem>();
        ListPrefixRet ret = null;
        while (true) {
            ret = client.listPrifix(bucket, prifix, marker, LIMIT);
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

    public String downloadUrl(String key) throws Exception {
        Mac mac = new Mac(accessKey, secretKey);
        String baseUrl = URLUtils.makeBaseUrl(domain, key);
        GetPolicy getPolicy = new GetPolicy();
        String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
        return downloadUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
