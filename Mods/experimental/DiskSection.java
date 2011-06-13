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

/** A single piece to the launcher disk **/
public DiskSection {
	
	//Variables to set the icon for the disk
	private final int imageResId;
	private final Bitmap image;
	
	private double radAngle;
	private final double angleRange;
	private double distance;
	
	//Constructor for the main sectors of the disks - 45 degrees each section
	public DiskSection(int resImage, double iAngle, double iDistance) {
		radAngle = iAngle;
		distance = iDistance;
		angleRange = Math.PI / 4;
	}
	
	//Constructor for the center button - 360 degrees inputtedd
	public DiskSection(int resImage, double iAngle, double iDistance, double iRange) {
		radAngle = iAngle;
		distance = iDistance;
		angleRange = iRange;
	}
	
	//Method that's called when this "button" is "pushed"
	public void clicked() {
		
	
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
}
		