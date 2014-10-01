package com.ztilde.predictor.models;

import android.content.Context;
import com.orm.SugarRecord;

public class Clustering extends SugarRecord<Clustering> {
	public int strength;
	public String wifiName;
	public int measure;
	public Classifier spot;
	
	public Clustering(Context ctx) {
		super(ctx);
	}
}
