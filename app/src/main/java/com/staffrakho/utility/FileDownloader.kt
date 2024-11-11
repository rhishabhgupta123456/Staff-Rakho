package com.staffrakho.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object FileDownloader {

    fun downloadFile(fileUrl: String, context: Context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            Toast.makeText(context, "Feature not supported on this version of Android", Toast.LENGTH_SHORT).show()
            return
        }

        // Default file extension and MIME type
        var fileExtension = "docx" // Default to .doc
        var mimeType = "application/msword"

        // Create the download directory
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "MeraJobs"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Build the download request
        val downloadRequest = DownloadManager.Request(Uri.parse(fileUrl))
            .setTitle("Downloading File")
            .setDescription("Downloading file from MeraJobs")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true) // Allow over metered network
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "MeraJobs/Resume_${System.currentTimeMillis()}.$fileExtension"
            )

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Execute a HEAD request to determine the file type based on the Content-Type header
        Thread {
            try {
                val urlConnection = URL(fileUrl).openConnection() as HttpURLConnection
                urlConnection.requestMethod = "HEAD"
                urlConnection.connect()

                // Get the Content-Type from the response headers
                val contentType = urlConnection.contentType
                if (contentType != null) {
                    when {
                        contentType.contains("pdf") -> {
                            fileExtension = "pdf"
                            mimeType = "application/pdf"
                        }

                        contentType.contains("msword") -> {
                            fileExtension = "docx"
                            mimeType = "application/msword"
                        }

                        contentType.contains("jpeg") || contentType.contains("jpg") -> {
                            fileExtension = "jpg"
                            mimeType = "image/jpeg"
                        }

                        contentType.contains("png") -> {
                            fileExtension = "png"
                            mimeType = "image/png"
                        }
                    }
                }

                // Update the download request with the correct file extension and MIME type
                downloadRequest.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "MeraJobs/Resume_${System.currentTimeMillis()}.$fileExtension"
                )
                downloadRequest.setMimeType(mimeType)

                // Start the download
                val downloadId = downloadManager.enqueue(downloadRequest)

                // Monitor the download progress
                monitorDownloadProgress(downloadManager, downloadId, context)

            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            }
        }.start()
    }

    @SuppressLint("Range", "UnspecifiedRegisterReceiverFlag")
    private fun monitorDownloadProgress(downloadManager: DownloadManager, downloadId: Long, context: Context) {
        // Register a broadcast receiver to monitor the download progress
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    // Download complete, you can update the UI or notify the user
                    Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
                    context.unregisterReceiver(this)
                }
            }
        }

        // Register the receiver to listen for completion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(
                    onComplete,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    Context.RECEIVER_NOT_EXPORTED
                )
            } else {
                context.registerReceiver(
                    onComplete,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                )
            }
        } else {
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }

        // Monitor the progress
        Thread {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                    }

                    val bytesDownloaded =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val bytesTotal =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                    if (bytesTotal > 0) {
                        val progress = (bytesDownloaded * 100L) / bytesTotal
                        // Update progress notification or UI here
                        Log.d("Download Progress", "Progress: $progress%")
                    }
                }
                cursor.close()
            }
        }.start()
    }
}
