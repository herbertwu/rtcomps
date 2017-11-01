package com.rtcomps.data.web.selenium;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.rtcomps.data.FileStorageService;
import com.rtcomps.data.web.def.BrowserTools;
import com.rtcomps.data.web.def.ScreenOptions;
import com.rtcomps.data.web.def.Screenshot;

public class WebDriverTools implements BrowserTools {
	private static final Logger log = Logger.getLogger(WebDriverTools.class);
	
	private static final int ERR_NAME_LEN = 16;
	private static final String FILE_DESC_TAG = "description";
	private static final int DOWNLOAD_POLLING_INTEVAL_SECS = 1;

	private final WebDriver driver;
	private ToolsOptions toolsOptions;

	
	public WebDriverTools(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebDriverTools(WebDriver driver, ToolsOptions toolsOptions) {
		this.driver = driver;
		this.toolsOptions = toolsOptions;
	}

	@Override
	public Screenshot takeScreenshot(ScreenOptions options) throws Exception {
		byte[] rawScreen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		return saveToCloud(rawScreen, options);
	}
	
	private Screenshot saveToCloud(byte[] rawScreen, ScreenOptions options) {
		String filename = getScreenshotFileName(options);
		options.getFileStorageService().createAndUploadFile(filename, addFileDescription(rawScreen.length,options), new ByteArrayInputStream(rawScreen));
		Screenshot shot = new Screenshot(filename, options.getScreenshotImgType(), rawScreen.length,new Date());
		return shot;
	}
	
	private Map<String, String> addFileDescription(int fileSize, ScreenOptions options) {
		String desc = "Browser screenshot image: size="+fileSize +", type=" +options.getScreenshotImgType();
		return ImmutableMap.of(FILE_DESC_TAG, desc);
	}
	
	private String getScreenshotFileName(ScreenOptions options) {
		String errName = getErrName(options.getException());
		return "Screenshot-for-sessionID-" + options.getSessionId() + "__" + errName + "." + options.getScreenshotImgType();
	}
	
	private String getErrName(Exception e) {
		String errName = (e==null)? "none\n" : e.getMessage();
        int i = errName.indexOf('\n');
        i = (i < 0)? ERR_NAME_LEN : i;
        errName = errName.substring(0, i).replaceAll("\\s", "_").replaceAll(":", "");
        return errName;
	}
	
	
	@Override
	public void saveDownloadFile(String storageFileName, FileStorageService fileStorageService) throws Exception {
		File file = getDownloadedFileWithWait();
		saveToFileStorage(file, fileStorageService, storageFileName);
	}
	
	@Override
	public void cleanDownloadFolder() throws IOException {
		File downloadFolder = toolsOptions.getDownloadFolder();
		if (downloadFolder.exists()) {
			FileUtils.cleanDirectory(downloadFolder);
		}
	}
	
	@Override
	public void removeDownloadFolder() throws IOException {
		cleanDownloadFolder();
		File folder = toolsOptions.getDownloadFolder();
		if (folder.exists()) {
			folder.delete();
		}
	}
	
	private File getDownloadedFileWithWait() throws FileNotFoundException {
		long start = System.currentTimeMillis();
		DownloadedFileRetriever retriever = createDownloadedFileRetriever();
		
		try {
			File file = new FluentWait<DownloadedFileRetriever>(retriever)
					.withTimeout(toolsOptions.getDownloadTimeoutSecs(), TimeUnit.SECONDS)
					.pollingEvery(DOWNLOAD_POLLING_INTEVAL_SECS, TimeUnit.SECONDS)
					.until(new Function<DownloadedFileRetriever, File>() {
						
						@Override
						public File apply(DownloadedFileRetriever retriever) {
							log.debug("_seleniumFindElementWithWait()... locator=" + retriever);
							return retriever.retrieveDownloadedFile().orNull();
						}
						
					});

			log.info("_downloadTime=" + (System.currentTimeMillis() - start) + "ms for file=" + file.getAbsolutePath());
			return file;
		} catch (TimeoutException e) {
			log.error("_downloadFailed: downloadFolder=" + getDownloadFolderDetails());
			throw new FileNotFoundException("Download failed in folder=" + toolsOptions.getDownloadFolder()
					+ ", timeout(s)=" + toolsOptions.getDownloadTimeoutSecs());
		}
	}

	@VisibleForTesting
	DownloadedFileRetriever createDownloadedFileRetriever() {
		return new DownloadedFileRetriever(toolsOptions.getDownloadFolder());
	}
	
	private String getDownloadFolderDetails() {
		String lineBrk="\r\n";
		StringBuffer buf = new StringBuffer();
		File[] files = toolsOptions.getDownloadFolder().listFiles();
		buf.append(lineBrk + "Files=" + files.length);
		for (File f : files) {
			buf.append(lineBrk+"name=" + f.getName() +", length="+FILE_DESC_TAG.length());
		}
		buf.append(lineBrk);
		return buf.toString();
	}
	
	private void saveToFileStorage(File file, FileStorageService fileStorageService, String storageFileName)
			throws FileNotFoundException, IOException {
		String fileDesc = createDownloadedFileDesc(file, storageFileName);
		InputStream uploadFileStream = new FileInputStream(file);
		try {
			fileStorageService.createAndUploadFile(storageFileName, ImmutableMap.of(FILE_DESC_TAG, fileDesc),  uploadFileStream);
		} catch (Exception e) {
			log.error("Sorry, the downloaded file can not be saved to minion storage", e);
			throw new RuntimeException(e);
		} finally {
			uploadFileStream.close();
		}
		log.info("downloadedFile is saved: " +fileDesc);
		cleanDownloadFolder();
	}
	
	private String createDownloadedFileDesc(File file, String storageName) {
		return "filepath: " + file.getAbsolutePath() + ", length: " + file.length() + ", storageName:" + storageName;
	}
	
	public static class ToolsOptions {
		private File downloadFolder;
		private int pageWaitSecs;
		private int downloadTimeoutSecs;
		
		public ToolsOptions(File downloadFolder, int pageWaitSecs,
				int downloadTimeoutSecs) {
			this.downloadFolder = downloadFolder;
			this.pageWaitSecs = pageWaitSecs;
			this.downloadTimeoutSecs = downloadTimeoutSecs;
		}

		public File getDownloadFolder() {
			return downloadFolder;
		}

		public int getPageWaitSecs() {
			return pageWaitSecs;
		}

		public int getDownloadTimeoutSecs() {
			return downloadTimeoutSecs;
		}
	}
}
