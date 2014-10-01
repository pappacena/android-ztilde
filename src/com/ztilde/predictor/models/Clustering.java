package com.ztilde.predictor.models;

import android.content.Context;
import com.orm.SugarRecord;

public class Clustering extends SugarRecord<Clustering> {
	public String name;
	public int clusters;
	
	public Clustering(Context ctx) {
		super(ctx);
	}
}
