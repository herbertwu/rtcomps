package com.rtcomps.core.template;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.rtcomps.core.util.CSVHelper;


public class SimpleNameValueListParser {
	private static String VAL_DELIM="=";
	private static String LIST_VAL_START_FLAG="{";
	private static String LIST_VAL_END_FLAG="}";
	
	public Map<String, Object> getModelFromStr(String strVals) {
		verify(strVals);
		Map<String, Object> varValues = new HashMap<String, Object>();
		List<String> entries = parserVarEntries(strVals);
		for (String entryStr : entries) {
			if (!entryStr.trim().isEmpty()) {
				int loc = entryStr.indexOf(VAL_DELIM);
				if (loc<1) {
					throw new RuntimeException("Invalid var value entry:" + entryStr);
				}
				String varName = entryStr.substring(0,loc).trim();
				String value = entryStr.substring(loc+1);
				if (value.startsWith(LIST_VAL_START_FLAG)&&value.endsWith(LIST_VAL_END_FLAG)) {
					String trimedListVal = StringUtils.stripEnd(StringUtils.stripStart(value, LIST_VAL_START_FLAG),LIST_VAL_END_FLAG);
					varValues.put(varName,parserVarEntries(trimedListVal));
				} else {
					varValues.put(varName,value);
				}
			}
		}
		return varValues;
	}

	private List<String> parserVarEntries(String strVals) {
		try {
			return CSVHelper.parseLine(new StringReader(strVals));
		} catch (Exception e) {
			throw new RuntimeException("Invalid jclVars:" +strVals);
		}
	}

	private void verify(String strVals) {
		if (strVals == null ) {
			throw new RuntimeException("Variable value not found");
		}
		
		if (strVals.indexOf(VAL_DELIM)<0) {
			throw new RuntimeException("Invalid Variable value format: "+strVals);
		}
	}

}
