package com.rtcomps.data.web.selenium.firefox;

import java.io.File;

import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.google.common.annotations.VisibleForTesting;
import com.rtcomps.data.web.selenium.ElementInspector;
import com.rtcomps.data.web.selenium.WebDriverBuilder;

public class FirefoxWebDriverBuilder implements WebDriverBuilder {
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

public FirefoxWebDriverBuilder(String firefoxPath, int proxyType, boolean isMaxSized,String downloadFolder) {
	this.firefoxPath = firefoxPath;
	this.proxyType = proxyType;
	this.isMaxSized = isMaxSized;
	this.downloadFolder = downloadFolder;
}

@Override
public WebDriver build() {
	System.setProperty("webdriver.gecko.driver", "C:/apps/gecko/geckodriver.exe");
//	FirefoxOptions ffOptions =  createFirefoxOptions();
//    FirefoxBinary binary = createFirefoxBinary();
//	FirefoxProfile profile = createFirefoxProfile();
//	setUpProfileProperties(profile);
//	ffOptions.setBinary(binary);
//	ffOptions.setProfile(profile);
	//TODO WebDriver ffDriver = createFirefoxDriver(ffOptions);
	WebDriver ffDriver = new FirefoxDriver();
	setUpDriverOptions(ffDriver);
	return ffDriver;
}

@VisibleForTesting
FirefoxBinary createFirefoxBinary() {
	return new FirefoxBinary(new File(firefoxPath));
}

@VisibleForTesting
FirefoxProfile createFirefoxProfile() {
	return new FirefoxProfile();
}

@VisibleForTesting
FirefoxOptions createFirefoxOptions() {
	return new FirefoxOptions();
}

@VisibleForTesting
FirefoxDriver createFirefoxDriver(FirefoxOptions ffOptions) {
	return new FirefoxDriver(ffOptions);
}

private void setUpProfileProperties(FirefoxProfile profile) {
	disableDownloadDialogBox(profile);
	setUpProxyType(profile);
	//setUpTimeouts(profile);
}

private void disableDownloadDialogBox(FirefoxProfile profile)  {
	downloadFolder = downloadFolderBase + "/" + System.currentTimeMillis(); //maybe UUID here if needed
	new File(downloadFolder).mkdirs();
	String downloadFolderNativeFormat = ElementInspector.getFileElementSystemAbsPath(downloadFolder);

	profile.setPreference("plugin.disable_full_page_plugin_for_types", DOWNLOAD_MIME_TYPES);
	profile.setPreference("pdfjs.disabled", true); //disable built-in PDF viewer - since FF-19.0
	profile.setPreference("plugin.scan.plid.all",false);
	profile.setPreference("plugin.scan.Acrobat", "99.0"); // blocks all versions of Acrobat readers
	profile.setPreference("browser.download.folderList",2);
	profile.setPreference("browser.download.manager.showWhenStarting",false);
	profile.setPreference("browser.download.dir", downloadFolderNativeFormat);
	profile.setPreference("browser.download.downloadDir",downloadFolderNativeFormat);
	profile.setPreference("browser.download.defaultFolder",downloadFolderNativeFormat);
	profile.setPreference("browser.helperApps.alwaysAsk.force", false);
	profile.setPreference("browser.helperApps.neverAsk.saveToDisk",DOWNLOAD_MIME_TYPES);
}

private void setUpProxyType(FirefoxProfile profile) {
	profile.setPreference("network.proxy.type", proxyType);
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
