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
import java.util.Timer;
import java.util.TimerTask;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
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
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.launcher.R;

/**
The Launcher Disk is central component to the Stark theme. It is used to scroll through apps and files depending on the screen you are at.
**/
public class LauncherDisk extends ImageView{

	private static final String TAG = "Launcher.LauncherDisk";
	private static final float ACCELERATION = 0.5f;
	private VelocityTracker mTracker;
	//The period of time in milliseconds before the timer fires the spinTimerTask
	final int TIMER_PERIOD = 250;
	//private Timer timer;
	//private TimerTask spin = new SpinTimer(this);
	private int timeCounter;

	private final int CENTER_BUTTON_SIZE = 50;
	private ArrayList<DiskSection> mSections = new ArrayList<DiskSection>();
	private HandleView all_apps_button;
	private boolean mSpinRight;
	double differenceX;
	private float mCurrentRotation = 0;
	

	public LauncherDisk(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSections();
	}
	
	public void initSections() {
		for (int i = 45; i <= 360; i+=45) {
			//Creates the button in the outermost disk at that angle
			double firstSize = getWidth() - CENTER_BUTTON_SIZE + .5; //.5 rounds it off correctly
			mSections.add(new DiskSection(getContext(), Math.toRadians(i), firstSize));
			//Creates the button in the middle disk at that angle
			double otherSize = getWidth() - (firstSize + CENTER_BUTTON_SIZE);
			mSections.add(new DiskSection(getContext(), Math.toRadians(i), otherSize));
		}
		//Creating the center button
		mSections.add(0, new DiskSection(getContext(), 0, CENTER_BUTTON_SIZE, Math.PI*2));
	}

	public HandleView getAppsButton() {
		return mSections.get(0).getAppsButton();
	}

	public boolean onTouchEvent(MotionEvent ev) {
		
		final int action = ev.getAction();
		
		//Used to determine/change rotation
		double r = Math.atan2(ev.getX() - getWidth() / 2, getHeight() / 2 - ev.getY());
            	int rotation = (int) Math.toDegrees(r);

		//The button that was pressed
		DiskSection selectedButton;
		
		double x =  ev.getX() - getWidth() / 2.0;
		double y = - (ev.getY() - getHeight() / 2.0);        
		// Compare this to the radii that mark the rings
		double distFromOrig = Math.sqrt( x*x + y*y );
		// Compare this to the angles of your slices (in radians)
		double angle = Math.atan2(y, x);

		switch (action) {
			case MotionEvent.ACTION_DOWN: break;
			//If a button is pushed and released, find what button was pushed
			case MotionEvent.ACTION_UP:
				for(DiskSection ds : mSections) {
					if ((angle >= ds.getAngle() && angle <= ds.getEndAngle()) && distFromOrig >= ds.getDistance()) {
						Log.d(TAG, "Found button "+ds.toString());
						selectedButton = ds;
						selectedButton.clicked(ev);
						break;
					}

				}
				break;
			//If the finger is dragged, SPIN IT
			case MotionEvent.ACTION_MOVE:
				updateRotation(rotation);
				break;
		}
				
		return true;        
	}

	//Sets the rotation, somehow (http://goo.gl/rrDBo)
	private void updateRotation(double rot)
    	{
		float newRot = new Float(rot);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stark_full);

		Matrix matrix = new Matrix();
		matrix.postRotate(newRot - 50);

		Bitmap redrawnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		setScaleType(ScaleType.CENTER);
		setImageBitmap(redrawnBitmap);
	}
}
