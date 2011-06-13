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
import android.widget.Scroller;
import android.widget.TextView;

import com.android.launcher.R;

/**
The Launcher Disk is central component to the Stark theme. It is used to scroll through apps and files depending on the screen you are at.
**/
public class LauncherDisk extends View{

	private static final String TAG = "Launcher.LauncherDisk";
	private static final float ACCELERATION = 0.5f;
	private VelocityTracker mTracker;
	private Timer time;
	private TimerTask spin = new SpinTimer();

	private Drawable mDisk;
	private final int CENTER_BUTTON_SIZE = 50;
	private ArrayList<DiskSection> mSections = new ArrayList<DiskSection>();
	private boolean mSpinRight;
	private double mCurrentRotation = 0;
	

	public LauncherDisk(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSections();
	}
	
	public void initSections() {
		for (int i = 45; i <= 360; i+=45) {
			//Creates the button in the outermost disk at that angle
			int firstSize = getWidth() - CENTER_BUTTON_SIZE + .5; //.5 rounds it off correctly
			mSections.add(new DiskSection(Math.toRadians(i), firstSize);
			//Creates the button in the middle disk at that angle
			int otherSize = getWidth() - (firstSize + CENTER_BUTTON_SIZE);
			mSections.add(new DiskSection(Math.toRadians(i), otherSize);
		}
		//Creating the center button
		mSections.add(0, new DiskSection(0, CENTER_BUTTON_SIZE), 2*Math.PI);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		
		final int action = ev.getAction();
		
		//Used for keeping track of the amount of milliseconds gone by
		int timeCounter = 0;
		//The period of time in milliseconds before the timer fires the spinTimerTask
		final int TIMER_PERIOD = 250;
		//The button that was pressed
		DiskSection selectedButton;
		
		double x =  event.getX() - getWidth() / 2.0;
		double y = - ( event.getY() - getHeight() / 2.0);        
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
						selectedButton = ds;
						break;
					}
				}
				break;
			//If the finger is drageed, SPIN IT
			case MotionEvent.ACTION_MOVE:
				if(differenceX < 0) 
					mSpinRight = false;
				else
					mSpinRight = true;
				break;
		}
		
		ds.clicked();
		timer.scheduleAtFixedRate(spin, 0, TIMER_PERIOD);		

		return true;        
	}

	//Spins the disk using an algorithm to predict when it should stop, and how much acceleration there is
	//http://stackoverflow.com/questions/1930963/rotating-a-view-in-android
	public void spinDisk() {
		//Find the range that was covered in the swipe- adjusts velocity of spin
		double differenceX = ev.getX() - ev.getRawX();
		//The amount of time passed since the start of the Timer
		double time = timeCounter / TIMER_PERIOD;

		if(mSpinRight)
			while(timeCounter < findGreatestRoot(differenceX) {
				mCurrentRotation += (-16 * timeCounter) + (ACCELERATION * timeCounter) + Math.abs(differenceX);
				setRotation(mCurrentRotation);
			}
		else
			while(timeCounter < findGreatestRoot(differenceX) {
				mCurrentRotation -= (-16 * timeCounter) + (ACCELERATION * timeCounter) + Math.abs(differenceX);
				setRotation(mCurrentRotation);
			}

		timeCounter++;		
	}

	//Helper method- returns the greatest root of the equation -16t+vt+|differenceX|
	public double findGreatestRoot(double differenceX) {
		
		//Running the quadratic equation
		double underRadical = Math.pow(ACCELERATION, 2) - (4*-16*Math.abs(differenceX));
	
		double firstRoot = (-1 * ACCELERATION + Math.sqrt(underRadical)) / 2 * -16;
		double secondRoot = (-1 * ACCELERATION - Math.sqrt(underRadical)) / 2 * -16;

		//Returns the greater of the two roots
		return Math.max(firstRoot, secondRoot);
	}

	private class SpinTimer extends TimerTask {
		public void run() {
			spinDisk();
		}
	}
