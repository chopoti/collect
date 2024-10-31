package org.fsr.collect.android.widgets.support

import android.graphics.BitmapFactory
import android.widget.ImageView
import org.fsr.collect.imageloader.GlideImageLoader
import org.fsr.collect.imageloader.ImageLoader
import java.io.File

class SynchronousImageLoader(private val fail: Boolean = false) : ImageLoader {
    override fun loadImage(
        imageView: ImageView,
        imageFile: File?,
        scaleType: ImageView.ScaleType,
        requestListener: GlideImageLoader.ImageLoaderCallback?
    ) {
        if (fail) {
            requestListener?.onLoadFailed()
        } else {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile?.absolutePath))
            requestListener?.onLoadSucceeded()
        }
    }
}
