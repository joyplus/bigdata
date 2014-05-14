package tv.joyplus.backend.qiniu;

import java.util.List;

public interface QiniuDao {
	/**
	 * 下载单个文件,保存到filepath
	 * @param key
	 */
	public void download(String key, String filepath) throws Exception;
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
}
