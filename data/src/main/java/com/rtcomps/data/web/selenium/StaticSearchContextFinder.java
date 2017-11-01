package com.rtcomps.data.web.selenium;

import java.util.List;

import org.openqa.selenium.SearchContext;

import com.rtcomps.data.web.selenium.QueryFinder.SearchContextFinder;

public class StaticSearchContextFinder implements SearchContextFinder {

	private final List<? extends SearchContext> contexts;

	public StaticSearchContextFinder(List<? extends SearchContext> contexts) {
		this.contexts = contexts;
	}

	@Override
	public List<? extends SearchContext> findSearchContexts() {
		return contexts;
	}

}