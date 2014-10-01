package com.ztilde.predictor.models;

import android.content.Context;
import com.orm.SugarRecord;

public class Classifier extends SugarRecord<Classifier> {
	public String name;

	public Classifier(Context ctx) {
		super(ctx);
	}

}
