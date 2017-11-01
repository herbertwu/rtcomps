package com.rtcomps.data.web.selenium;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

public class DownloadedFileRetriever {

	private static final Logger log = Logger.getLogger(DownloadedFileRetriever.class);
	
	private static final String IN_PROGRESS_FILE_SUFFIX = ".part";

	private final File downloadFolder;
	
	
	public DownloadedFileRetriever(File downloadFolder) {
		this.downloadFolder = downloadFolder;
	}
    
	public Optional<File> retrieveDownloadedFile() {
		File[] files = downloadFolder.listFiles();
		if (files == null) {
			String err = "Browser failed to start the downloading process: folder=" + downloadFolder;
			throw new RuntimeException(err);
		}
		
		if (files.length == 0) {
			log.debug("Download not started yet...: folder=" + downloadFolder);
			return Optional.absent();
		}

		for (File file : files) {
			if (file.isFile()) {
				if (file.getName().endsWith(IN_PROGRESS_FILE_SUFFIX)) {
					log.debug("Download in progress...: fileName=" + file.getName());
					return Optional.absent();
				}
			}
		}
		
		return Optional.of(files[0]);
    }

}
