package com.rtcomps.data.web.selenium.chrome;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.rtcomps.data.ActionsConfig;
import com.rtcomps.data.web.selenium.WebDriverBuilder;

public class ChromeWebDriverBuilder implements WebDriverBuilder {
	public static final String DOWNLOAD_MIME_TYPES = "text/comma-separated-values, text/plain, text/csv, " +
    		"application/octet-stream, application/pdf, application/zip, application/csv, application/excel, " +
    		"image/png, image/gif, image/jpg";
private String proxyUrl;
private boolean isMaxSized = true;
private final String downloadFolderBase = initTmpDownloadBaseFolder();
private String downloadFolder;

private String initTmpDownloadBaseFolder() {
	String tmpDir="webdownload";
	String workDir=System.getProperty("java.io.tmpdir");
	return (workDir.endsWith("/") || workDir.endsWith("\\"))? workDir + tmpDir : workDir +"/" + tmpDir;
}

public ChromeWebDriverBuilder(String proxy, boolean isMaxSized,String downloadFolder) {
	this.proxyUrl = proxy;
	this.isMaxSized = isMaxSized;
	this.downloadFolder = downloadFolder;
}

@Override
public WebDriver build() {
	System.setProperty("webdriver.chrome.driver", ActionsConfig.getString("webdriver.chrome.driver"));
	DesiredCapabilities capabilities = desiredChromeCapabilities();
	return new ChromeDriver(capabilities);
}



private DesiredCapabilities desiredChromeCapabilities() {
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	addProxy(capabilities);
	return capabilities;
}

private void addProxy(DesiredCapabilities capabilities) {
	if (!StringUtils.isEmpty(proxyUrl) && proxyUrl.indexOf(":")>0) {
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(proxyUrl);
		capabilities.setCapability("proxy", proxy);
	}
}

}
