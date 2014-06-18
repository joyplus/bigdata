package tv.joyplus.backend.appinfo.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;

public class AppLogAnalyzeDaoImpl extends JdbcDaoSupport implements
		AppLogAnalyzeDao {

	@Override
	public void save(AppLogAnalyzeInfo instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AppLogAnalyzeInfo> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppLogAnalyzeInfo> listUnAnalyzed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(long id, int status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatus(AppLogAnalyzeInfo instance) {
		// TODO Auto-generated method stub

	}

}
