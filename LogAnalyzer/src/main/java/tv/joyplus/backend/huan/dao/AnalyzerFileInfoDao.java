package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;

public interface AnalyzerFileInfoDao {
	public void save(AnalyzerFileInfo instance);
	public List<AnalyzerFileInfo> listAll();
	public List<AnalyzerFileInfo> listUnAnalyzed();
	public void updateStatus(long id, byte status);
}
