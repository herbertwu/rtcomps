package com.rtcomps.data.web;

import org.apache.commons.lang3.StringUtils;

import com.rtcomps.data.def.ActionRunResult;
import com.rtcomps.data.def.ActionRunStatus;
import com.rtcomps.data.def.Param;
import com.rtcomps.data.web.base.WebActionsBase;

public class OpenBrowserAction extends WebActionsBase {
	private String url;
	private String browserName=null;
	private Integer pageWaitSecs=null;
	private String proxy=null;

	public OpenBrowserAction(@Param(name = "url") String url) {
		this.url=url;
	}
	
	public OpenBrowserAction(@Param(name = "url") String url,
			@Param(name = "browserName") String browserName) {
		this.url=url;
		this.browserName=browserName;
	}
	
	public OpenBrowserAction(@Param(name = "url") String url,
			@Param(name = "browserName") String browserName,
			@Param(name = "pageWaitSecs") String pageWaitSecs) {
		this.url=url;
		this.browserName=browserName;
		this.pageWaitSecs=convertToInt(pageWaitSecs);
	}
		
    private Integer convertToInt(String pageWaitSecs) {
	     try {
	    	 return StringUtils.isEmpty(pageWaitSecs)? null : Integer.parseInt(pageWaitSecs);
	     } catch (Exception e ) {
	    	 throw new RuntimeException("Invalid 'pageWaitSecs' parameter value:  " +pageWaitSecs);
	     }
	}

	public OpenBrowserAction(@Param(name = "url") String url,
			@Param(name = "browserName") String browserName,  
			@Param(name = "pageWaitSecs") String pageWaitSecs,
			@Param(name = "proxy") String proxy) {
		this.url=url;
		this.browserName=browserName;
		this.pageWaitSecs=convertToInt(pageWaitSecs);
		this.proxy=proxy;
	}

	@Override
	public ActionRunResult execute() {
		try {
			openUrlInBrowser(url,browserName,pageWaitSecs,proxy);
			//getBrowser().close();
			return new ActionRunResult(ActionRunStatus.COMPLETED,0, "", "",false);
		} catch (Exception e) {
			return new ActionRunResult(ActionRunStatus.FAILED,1, "OpenBrowserAction error: " + e.getLocalizedMessage(), null,false);
		}
	}
	
	public void close() {
		try {
			getBrowser().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
