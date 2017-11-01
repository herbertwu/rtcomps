package com.rtcomps.data.web;

import com.rtcomps.data.def.ActionRunResult;
import com.rtcomps.data.def.ActionRunStatus;
import com.rtcomps.data.web.base.WebActionsBase;
import com.rtcomps.data.web.def.WebBrowser;

public class CloseBrowserAction extends WebActionsBase {
	

	public CloseBrowserAction() {
	}


	@Override
	public ActionRunResult execute() {
		try {
			WebBrowser browser = getBrowser();
			browser.close();
			return new ActionRunResult(ActionRunStatus.COMPLETED,0, "", "",false);
		} catch (Exception e) {
			return new ActionRunResult(ActionRunStatus.FAILED,1, "CloseBrowserAction error: " + e.getLocalizedMessage(), null,false);
		}
	}

}
