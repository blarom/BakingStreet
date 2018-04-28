/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.bakingstreet.IdlingResource;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.bakingstreet.*;


public class FakeVideoDownloader {

    private static final int DELAY_MILLIS = 2000;

    public interface DelayerCallback{
        void onVideoDownloaded();
    }

    public static void downloadVideo(Context context, final StepDetailsFragment callback,
                                     @Nullable final TimeDelayIdlingResource idlingResource) {

        //Not yet idling...
        if (idlingResource != null) idlingResource.setIdleState(false);

        // Display loading video toast
        Toast.makeText(context, context.getString(R.string.loading_video), Toast.LENGTH_SHORT).show();

        //Create a "fake" video download delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {

                    //Send a video callback
                    callback.onVideoDownloaded();

                    //Tell Android that the app is currently idling
                    if (idlingResource != null) idlingResource.setIdleState(true);
                }
            }
        }, DELAY_MILLIS);
    }
}