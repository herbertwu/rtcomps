package com.rtcomps.data.def;

import com.rtcomps.data.runtime.ActionRunContext;

public interface ContextSensitive extends Action {
	void setContext(ActionRunContext context);
}
