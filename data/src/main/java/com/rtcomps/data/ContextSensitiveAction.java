package com.rtcomps.data;

import com.rtcomps.data.def.ContextSensitive;
import com.rtcomps.data.runtime.ActionRunContext;
import com.rtcomps.data.runtime.session.Session;

public abstract class ContextSensitiveAction implements ContextSensitive {
	protected ActionRunContext context;

	@Override
	public void setContext(ActionRunContext context) {
		this.context = context;
	}

	protected Session getSession() {
		return this.context.getSession();
	}
	
}
