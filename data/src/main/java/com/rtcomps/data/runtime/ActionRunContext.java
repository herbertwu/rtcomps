package com.rtcomps.data.runtime;

import com.rtcomps.data.runtime.session.Session;
/**
 * Action runtime information holder. Starting with a session object 
 * and more can be added, such as  authenticator etc.
 * 
 * @author T06KNDC
 *
 */

public class ActionRunContext {
	
	private final Session session;
	private final String actionName;
	private final String id;
	
	public ActionRunContext(Session session, String actionName, String id) {
		this.session = session;
		this.actionName = actionName;
		this.id = id;
	}

	public Session getSession() {
		return session;
	}

	public String getActionName() {
		return actionName;
	}

	public String getId() {
		return id;
	}

}
