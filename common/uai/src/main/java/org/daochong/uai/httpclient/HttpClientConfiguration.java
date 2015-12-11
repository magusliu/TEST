package org.daochong.uai.httpclient;

public class HttpClientConfiguration {
	private int connectTimeout;
	private boolean userCaches;
	private String defaultEncoding;
	private String root;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public boolean isUserCaches() {
		return userCaches;
	}

	public void setUserCaches(boolean userCaches) {
		this.userCaches = userCaches;
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

}
