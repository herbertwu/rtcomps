package com.rtcomps.data.web.selenium.ie;

import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.rtcomps.data.web.selenium.WebDriverBuilder;

public class IEWebDriverBuilder implements WebDriverBuilder {
	public static final String DOWNLOAD_MIME_TYPES = "text/comma-separated-values, text/plain, text/csv, " +
    		"application/octet-stream, application/pdf, application/zip, application/csv, application/excel, " +
    		"image/png, image/gif, image/jpg";
public static final int MAX_SCREEN_WIDTH = 1080;
public static final int MAX_SCREEN_HEIGHT = 786;
private String firefoxPath;
private int proxyType = ProxyType.SYSTEM.ordinal();
private boolean isMaxSized = true;
private final String downloadFolderBase = initTmpDownloadBaseFolder();
private String downloadFolder;

private String initTmpDownloadBaseFolder() {
	String tmpDir="webdownload";
	String workDir=System.getProperty("java.io.tmpdir");
	return (workDir.endsWith("/") || workDir.endsWith("\\"))? workDir + tmpDir : workDir +"/" + tmpDir;
}

public IEWebDriverBuilder(String firefoxPath, int proxyType, boolean isMaxSized,String downloadFolder) {
	this.firefoxPath = firefoxPath;
	this.proxyType = proxyType;
	this.isMaxSized = isMaxSized;
	this.downloadFolder = downloadFolder;
}

@Override
public WebDriver build() {
	System.setProperty("webdriver.ie.driver", "C:/apps/IEDriverServer_x64_3.5.1/IEDriverServer.exe");
	DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	caps.setCapability("ignoreZoomSetting", true);
	//caps.setCapability("acceptInsecureCerts", true);
	WebDriver ieDriver =  new InternetExplorerDriver(caps);
	setUpDriverOptions(ieDriver);
	return ieDriver;
}



private void setUpDriverOptions(WebDriver ffDriver) {
	try {
		//ffDriver.manage().timeouts().implicitlyWait(IMPLICIT_PAGE_WAIT_SECONDS, TimeUnit.SECONDS);
		//TODO new driver not working anymore ffDriver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
		if (isMaxSized) {
			//Notice linux headless FF does not handle maxSize() (no windows manager)
			//TODO new driver not working ffDriver.manage().window().setSize(new Dimension(MAX_SCREEN_WIDTH, MAX_SCREEN_HEIGHT));
		}
    } catch (Exception e) {
    	e.printStackTrace();
    }
}

}
