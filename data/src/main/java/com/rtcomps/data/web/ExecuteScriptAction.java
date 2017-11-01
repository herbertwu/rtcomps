package com.rtcomps.data.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtcomps.data.def.ActionRunResult;
import com.rtcomps.data.def.ActionRunStatus;
import com.rtcomps.data.def.Param;
import com.rtcomps.data.web.base.WebActionsBase;

public class ExecuteScriptAction extends WebActionsBase {
	protected static final Logger logger =  LoggerFactory.getLogger(ExecuteScriptAction.class);
	private String script;

	public ExecuteScriptAction(@Param(name = "script")String script) {
		this.script = script;
	}

	@Override
	public ActionRunResult execute() {
		try {
			Object result = getBrowser().executeScript(script);
			String objName = createContextObject("executeScript", result);
			logger.info("executeScript result==" + result);
			return new ActionRunResult(ActionRunStatus.COMPLETED,0, "",objName,true);
		} catch (Exception e) {
			return fail(e);
		}
	}

}
