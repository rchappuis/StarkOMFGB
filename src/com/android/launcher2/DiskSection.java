/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher2;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.launcher.R;

/** A single piece to the launcher disk **/
public class DiskSection extends ImageButton {
	
	private double radAngle;
	private final double angleRange;
	private double distance;
	private boolean isCenter;
	private HandleView appsButton;
	
	//Constructor for the main sectors of the disks - 45 degrees each section
	public DiskSection(Context context, double iAngle, double iDistance) {
		super(context);
		radAngle = iAngle;
		distance = iDistance;
		angleRange = Math.PI / 4;
		isCenter = false;
	}
	
	//Constructor for the center button - 360 degrees inputtedd
	public DiskSection(Context context, double iAngle, double iDistance, double iRange) {
		super(context);
		radAngle = iAngle;
		distance = iDistance;
		angleRange = iRange;
		isCenter = true;
		appsButton = new HandleView(getContext());
	}

	public void clicked(MotionEvent ev) {
		if(isCenter)
			appsButton.onTouchEvent(ev);
	}

	public HandleView getAppsButton() {
		return appsButton;
	}
	
	public double getAngle() {
		return radAngle;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getRange() {
		return angleRange;
	}
	
	public double getEndAngle() {
		return radAngle + angleRange;
	}

	public String toString(){
		return "Distance: "+distance+"\nAngle: "+radAngle+"\nisCenter: "+isCenter;
	}
}
		
