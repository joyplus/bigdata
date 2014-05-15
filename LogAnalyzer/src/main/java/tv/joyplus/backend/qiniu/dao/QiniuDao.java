package tv.joyplus.backend.qiniu.dao;

import java.io.File;
import java.util.List;

import tv.joyplus.backend.qiniu.QiniuItem;

public interface QiniuDao {
	/**
	 * 下载单个文件,保存到file
	 * @param key
	 */
	public void download(String url, File file) throws Exception;
	/**
	 * 获取文件列表信息
	 * @return
	 */
	public List<QiniuItem> list();
	/**
	 * 获取单个文件信息
	 * @param Key
	 * @return
	 */
	public QiniuItem stat(String key);
	
	public String formatFilename(String key);
	
	public String downloadUrl(String key) throws Exception;
	
}
