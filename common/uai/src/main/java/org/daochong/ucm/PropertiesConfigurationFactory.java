package org.daochong.ucm;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.daochong.lang.JdbcTemplate;
import org.daochong.lang.Properties;
import org.daochong.lang.StringUtils;

public class PropertiesConfigurationFactory extends BasePropertiesConfigurationFactory {
	private DataSource dataSource;
	private String sql;
	private String file;
	private List<String> files;

	private long lastLoad;
	private int interval;
	private Timer timer;

	public String getFile() {
		return file;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
		if (this.interval > 0) {
			if (this.timer != null) {
				timer.cancel();
			}
			this.timer = new Timer();
			WatchTimerTask task = new WatchTimerTask();
			task.setFactory(this);
			this.timer.schedule(task, this.getInterval() * 1000, this.getInterval() * 1000);
		}
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public void load() {
		super.load();
		if (this.file != null) {
			try {
				Properties prop = new Properties(this.getClass().getResourceAsStream(this.file));
				Configuration config = config(prop);
				if (config != null) {
					updateConfiguration(config);
				}
			} catch (Throwable e) {

			}
		}
		if (this.files != null) {
			for (String file : this.files) {
				try {
					Properties prop = new Properties(this.getClass().getResourceAsStream(file));
					Configuration config = config(prop);
					if (config != null) {
						updateConfiguration(config);
					}
				} catch (Throwable e) {

				}
			}
		}
		loadDataSourceConfig();
	}

	protected boolean isReferesh() {
		if (this.getDataSource() != null && this.getSql() != null) {
			JdbcTemplate jdbc = new JdbcTemplate(this.getDataSource());
			try {
				List<Map<String, Object>> list = jdbc.listObjects(getSql());
				for (Map<String, Object> map : list) {
					Date configTime = (Date) map.get("CONFIG_TIME");
					if (configTime == null)
						continue;
					if (configTime.getTime() > this.lastLoad) {
						return true;
					}
				}
			} catch (Throwable e) {
				return false;
			}
		}
		return false;
	}

	protected synchronized void loadDataSourceConfig() {
		if (this.getDataSource() != null && this.getSql() != null) {
			JdbcTemplate jdbc = new JdbcTemplate(this.getDataSource());
			try {
				List<Map<String, Object>> list = jdbc.listObjects(getSql());
				for (Map<String, Object> map : list) {
					String groupId = (String) map.get("GROUP_ID");
					String configId = (String) map.get("CONFIG_ID");
					String prop = (String) map.get("DATA");
					Date configTime = (Date) map.get("CONFIG_TIME");
					if (configId == null || prop == null)
						continue;
					if (StringUtils.isEmpty(groupId))
						groupId = "default";
					if (configTime != null) {
						Configuration cfg = this.getConfigurationContainer().getConfiguration(configId, groupId);
						if (cfg != null && configTime.getTime() == cfg.getConfigTime()) {
							continue;
						}
					}
					prop = prop + "\r\nUAI_groupId=" + groupId + "\r\nUAI_configId=" + configId + "\r\n";
					Properties p = new Properties();
					p.setSplit("\n");
					p.load(prop);
					Configuration config = config(p);
					config.setConfigTime(configTime.getTime());
					if (config != null) {
						updateConfiguration(config);
					}
				}
				lastLoad = System.currentTimeMillis();
			} catch (SQLException e) {
				throw new RuntimeException("sql error", e);
			}
		}
	}
}

class WatchTimerTask extends TimerTask {
	private PropertiesConfigurationFactory factory;

	public void run() {
		if (this.factory.isReferesh()) {
			this.factory.loadDataSourceConfig();
		}
	}

	public PropertiesConfigurationFactory getFactory() {
		return factory;
	}

	public void setFactory(PropertiesConfigurationFactory factory) {
		this.factory = factory;
	}

}