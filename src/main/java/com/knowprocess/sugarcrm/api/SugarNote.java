package com.knowprocess.sugarcrm.api;

import com.knowprocess.crm.CrmRecord;

public class SugarNote extends CrmRecord {

	private static final long serialVersionUID = 6967638999389747515L;

	public SugarNote() {
		super();
	}

	public SugarNote(String subject, String description) {
		this();
		setCustom("name", subject);
		setCustom("description", description);
	}

	public SugarNote(CrmRecord crmRecord) {
		super(crmRecord);
	}


}
