package com.ravielcd.mediapicker.mediapickerbottom.repositories

import android.arch.lifecycle.LiveData
import com.ravielcd.mediapicker.mediapickerbottom.models.Media
import javax.inject.Singleton
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.database.Cursor
import android.support.v4.content.CursorLoader
import android.provider.MediaStore
import android.support.annotation.NonNull
import com.ravielcd.mediapicker.mediapickerbottom.models.Picture
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import javax.inject.Inject


@Singleton // informs Dagger that this class should be constructed once
class MediaRepository {
    @Inject
    lateinit var context: Context

    fun getAllMedias(): LiveData<List<Media>> {
        val data = MutableLiveData<List<Media>>()

        loadDataAsync(data)

        return data
    }

    private fun loadDataAsync(@NonNull data: MutableLiveData<List<Media>>) = async(CommonPool) {
        try {
            val listOfAllMedia : ArrayList<Media> = ArrayList<Media>()

            val job = async(CommonPool) {
                val cursor : Cursor = initCursor()

                val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                while (cursor.moveToNext()) {
                    val absolutePathOfImage = cursor.getString(columnIndexData)
                    if (absolutePathOfImage != null) {
                        listOfAllMedia.add(Picture(absolutePathOfImage, absolutePathOfImage))
                    }
                }
                cursor.close()
            }
            job.await()
            data.value = listOfAllMedia

        }
        catch (e: Exception) {
        }
        finally {
        }
    }

    private fun initCursor() : Cursor {
        val loader: CursorLoader = getLoaderForPictures()

        return loader.loadInBackground()!!
    }

    private fun getLoaderForPictures() : CursorLoader {
        val uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Get relevant columns for use later.
        val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.SIZE,
                MediaStore.Images.ImageColumns.TITLE)

        return CursorLoader(
                this.context,
                uri,
                projection,
                null,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
         )
    }
}