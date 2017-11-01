package com.rtcomps.data;

import java.io.InputStream;
import java.util.Map;


public interface FileStorageService {

	/* Common file metadata keys. One-off keys should not be added here. */
	static final String META_DESCRIPTION = "description";
	static final String META_ORIGIN_SERVER = "origin-server";
	static final String META_ORIGIN_PATH = "origin-path";
	static final String META_LAST_MODIFIED = "last-modified";
	static final String META_TIMESTAMP = "timestamp";

	/**
	 * Store a file. The caller is responsible for closing the data InputStream.
	 * The optionalMetadata parameter may be null if there is no additional
	 * metadata.
	 * @return 
	 */
	StorageFileReference createAndUploadFile(String filename, Map<String, String> optionalMetadata, InputStream data);

	InputStream openFile(StorageFileReference storageFileReference);
}
