package com.rtcomps.data.web.def;

import java.io.IOException;

import com.rtcomps.data.FileStorageService;

public interface BrowserTools {
	Screenshot takeScreenshot(ScreenOptions options) throws Exception;
	void saveDownloadFile(String fileName, FileStorageService fileStorageService) throws Exception;
	void cleanDownloadFolder() throws IOException;
	void removeDownloadFolder() throws IOException;
}

