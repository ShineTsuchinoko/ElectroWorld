package io.github.shinetsuchinoko.holyenglish.Model.Storage

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.shinetsuchinoko.holyenglish.R
import io.github.shinetsuchinoko.holyenglish.TestPageActivity
import com.google.firebase.firestore.FirebaseFirestore
import io.realm.Realm
import io.realm.kotlin.createObject
import java.io.File
import android.os.Environment.MEDIA_MOUNTED
import com.squareup.okhttp.internal.DiskLruCache
import android.graphics.Bitmap
import android.os.Environment
import android.support.v4.util.LruCache
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.DiskCache


/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Service to handle downloading files from Firebase Storage.
 */
class DownloadService : MyBaseTaskService() {

    private lateinit var mStorageRef: StorageReference
    private lateinit var mDB: FirebaseFirestore
    private lateinit var mRealm: Realm

    private val mDownloadPaths: MutableList<String> = mutableListOf<String>(
            "images/001.jpg",
            "images/002.jpg",
            "images/003.jpg",
            "images/004.jpg",
            "images/005.jpg",
            "images/006.jpg",
            "images/007.jpg",
            "images/008.jpg",
            "images/009.jpg",
            "images/010.jpg",
            "images/011.jpg",
            "images/012.jpg",
            "images/013.jpg",
            "images/014.jpg",
            "images/015.jpg",
            "images/016.jpg",
            "images/017.jpg",
            "images/018.jpg",
            "images/019.jpg",
            "images/020.jpg",
            "images/021.jpg",
            "images/022.jpg",
            "images/023.jpg",
            "images/024.jpg",
            "images/025.jpg",
            "images/026.jpg",
            "images/027.jpg",
            "images/028.jpg",
            "images/029.jpg",
            "images/030.jpg"
    )

    private lateinit var mDownloadIterator: MutableIterator<String>

    override fun onCreate() {
        super.onCreate()

        // Initialize Storage
        mStorageRef = FirebaseStorage.getInstance().reference
        mDB = FirebaseFirestore.getInstance()
        mRealm = Realm.getDefaultInstance()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:$intent:$startId")


        if (ACTION_DOWNLOAD == intent.action) {
            // Get the path to download from the intent
            val orgDownloadPath = intent.getStringExtra(EXTRA_DOWNLOAD_PATH)

            // forここでループ
            mDownloadIterator = mDownloadPaths.iterator()
            if(mDownloadIterator.hasNext()) {
                // Mark task started
                taskStarted()
                showProgressNotification(getString(R.string.progress_downloading), 0, 0)
                downloadFromPath(mDownloadIterator.next())
            }
        }

        return Service.START_REDELIVER_INTENT
    }

    private fun downloadFromPath(downloadPath: String) {
        Log.d(TAG, "downloadFromPath:$downloadPath")

        val localImage = File.createTempFile("DEng/images", "jpg")

        // Download and get total bytes
        val imageStorageRef = mStorageRef.child(downloadPath)
        imageStorageRef.getFile(localImage)
                .addOnSuccessListener { taskSnapshot ->
                    Log.d(TAG, "download:SUCCESS")

                    // Store datas
                    this.mRealm.let{
                        it.executeTransaction{
                            it.createObject<CardObject>().apply{
//                                this.path =
//                                this.id = count++
                            }
                        }
                    }

                    if(mDownloadIterator.hasNext()){
                        downloadFromPath(mDownloadIterator.next())
                    } else {
                        // Send success broadcast with number of bytes downloaded
                        broadcastDownloadFinished(downloadPath, taskSnapshot.totalByteCount)
                        showDownloadFinishedNotification(downloadPath, taskSnapshot.totalByteCount.toInt())

                        // Mark task completed
                        taskCompleted()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "download:FAILURE", exception)

                    // Send failure broadcast
                    broadcastDownloadFinished(downloadPath, -1)
                    showDownloadFinishedNotification(downloadPath, -1)

                    // Mark task completed
                    taskCompleted()
                }
    }

    /**
     * Broadcast finished download (success or failure).
     * @return true if a running receiver received the broadcast.
     */
    private fun broadcastDownloadFinished(downloadPath: String, bytesDownloaded: Long): Boolean {
        val success = bytesDownloaded != -1L
        val action = if (success) DOWNLOAD_COMPLETED else DOWNLOAD_ERROR

        val broadcast = Intent(action)
                .putExtra(EXTRA_DOWNLOAD_PATH, downloadPath)
                .putExtra(EXTRA_BYTES_DOWNLOADED, bytesDownloaded)
        return LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(broadcast)
    }

    /**
     * Show a notification for a finished download.
     */
    private fun showDownloadFinishedNotification(downloadPath: String, bytesDownloaded: Int) {
        // Hide the progress notification
        dismissProgressNotification()

        // Make Intent to MainActivity
        val intent = Intent(this, TestPageActivity::class.java)
                .putExtra(EXTRA_DOWNLOAD_PATH, downloadPath)
                .putExtra(EXTRA_BYTES_DOWNLOADED, bytesDownloaded)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val success = bytesDownloaded != -1
        val caption = if (success) getString(R.string.download_success) else getString(R.string.download_failure)
        showFinishedNotification(caption, intent, true)
    }

    companion object {

        private val TAG = "Storage#DownloadService"

        /** Actions  */
        val ACTION_DOWNLOAD = "action_download"
        val DOWNLOAD_COMPLETED = "download_completed"
        val DOWNLOAD_ERROR = "download_error"

        /** Extras  */
        val EXTRA_DOWNLOAD_PATH = "extra_download_path"
        val EXTRA_BYTES_DOWNLOADED = "extra_bytes_downloaded"


        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(DOWNLOAD_COMPLETED)
                filter.addAction(DOWNLOAD_ERROR)

                return filter
            }
    }
}
