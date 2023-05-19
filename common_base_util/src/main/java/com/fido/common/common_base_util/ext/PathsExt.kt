/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.fido.common.common_base_util.ext

import android.content.Context
import android.os.Environment
import com.fido.common.common_base_util.app

inline val cacheDirPath: String get() = app.cacheDirPath

/*inline val Context.cacheDirPath: String
  get() = if (isExternalStorageWritable || !isExternalStorageRemovable) {
    externalCacheDirPath.orEmpty()
  } else {
    internalCacheDirPath
  }*/

//inline val externalCacheDirPath: String? get() = app.externalCacheDirPath

//inline val Context.externalCacheDirPath: String? get() = externalCacheDir?.absolutePath

inline val externalFilesDirPath: String? get() = app.externalFilesDirPath

inline val Context.externalFilesDirPath: String? get() = getExternalFilesDir(null)?.absolutePath

inline val externalPicturesDirPath: String? get() = app.externalPicturesDirPath

inline val Context.externalPicturesDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath

inline val externalMoviesDirPath: String? get() = app.externalMoviesDirPath

inline val Context.externalMoviesDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath

inline val externalDownloadsDirPath: String? get() = app.externalDownloadsDirPath

inline val Context.externalDownloadsDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath

inline val externalDocumentsDirPath: String? get() = app.externalDocumentsDirPath

inline val Context.externalDocumentsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_DOCUMENTS)?.absolutePath

inline val externalMusicDirPath: String? get() = app.externalMusicDirPath

inline val Context.externalMusicDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath

inline val externalPodcastsDirPath: String? get() = app.externalPodcastsDirPath

inline val Context.externalPodcastsDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_PODCASTS)?.absolutePath

inline val externalRingtonesDirPath: String? get() = app.externalRingtonesDirPath

inline val Context.externalRingtonesDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_RINGTONES)?.absolutePath

inline val externalAlarmsDirPath: String? get() = app.externalAlarmsDirPath

inline val Context.externalAlarmsDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_ALARMS)?.absolutePath

inline val externalNotificationsDirPath: String? get() = app.externalNotificationsDirPath

inline val Context.externalNotificationsDirPath: String?
  get() = getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)?.absolutePath

inline val internalCacheDirPath: String get() = app.internalCacheDirPath

inline val Context.internalCacheDirPath: String get() = cacheDir.absolutePath

inline val internalFileDirPath: String get() = app.internalFileDirPath

inline val Context.internalFileDirPath: String get() = filesDir.absolutePath

inline val internalPicturesDirPath: String? get() = app.internalPicturesDirPath

inline val Context.internalPicturesDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_PICTURES)?.absolutePath

inline val internalMoviesDirPath: String? get() = app.internalMoviesDirPath

inline val Context.internalMoviesDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_MOVIES)?.absolutePath

inline val internalDownloadsDirPath: String? get() = app.internalDownloadsDirPath

inline val Context.internalDownloadsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_DOWNLOADS)?.absolutePath

inline val internalDocumentsDirPath: String? get() = app.internalDocumentsDirPath

inline val Context.internalDocumentsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_DOCUMENTS)?.absolutePath

inline val internalMusicDirPath: String? get() = app.internalMusicDirPath

inline val Context.internalMusicDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_MUSIC)?.absolutePath

inline val internalPodcastsDirPath: String? get() = app.internalPodcastsDirPath

inline val Context.internalPodcastsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_PODCASTS)?.absolutePath

inline val internalRingtonesDirPath: String? get() = app.internalRingtonesDirPath

inline val Context.internalRingtonesDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_RINGTONES)?.absolutePath

inline val internalAlarmsDirPath: String? get() = app.internalAlarmsDirPath

inline val Context.internalAlarmsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_ALARMS)?.absolutePath

inline val internalNotificationsDirPath: String? get() = app.internalNotificationsDirPath

inline val Context.internalNotificationsDirPath: String?
  get() = getFileStreamPath(Environment.DIRECTORY_NOTIFICATIONS)?.absolutePath

/**
 * Checks if a volume containing external storage is available for read and write.
 */
//inline val isExternalStorageWritable: Boolean
//  get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

/**
 * Checks if a volume containing external storage is available to at least read.
 */
inline val isExternalStorageReadable: Boolean
  get() = Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)

/**
 * Checks if a volume containing external storage is removable.
 */
inline val isExternalStorageRemovable: Boolean
  get() = Environment.isExternalStorageRemovable()
