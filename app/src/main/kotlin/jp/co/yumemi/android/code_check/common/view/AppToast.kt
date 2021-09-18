package jp.co.yumemi.android.code_check.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import jp.co.yumemi.android.code_check.R

class AppToast {
    private var contentResId = 0
    private var idImgToast = 0
    private var backGroundColor = 0
    private var colorText = 0
    private val timeShow = 0
    private val textFont = 0
    private var isHtml = false
    private var text: String? = null

    var toast: Toast? = null

    var duration = Toast.LENGTH_SHORT
        private set

    fun setHtml(html: Boolean): AppToast {
        isHtml = html
        return this
    }

    fun setImgToast(idImgToast: Int): AppToast {
        this.idImgToast = idImgToast
        return this
    }

    fun setColorTextToast(colorText: Int): AppToast {
        this.colorText = colorText
        return this
    }

    fun setText(text: String?): AppToast {
        this.text = text
        return this
    }

    fun setText(contentResId: Int): AppToast {
        this.contentResId = contentResId
        return this
    }

    fun setBackGroundColor(backGroundColor: Int): AppToast {
        this.backGroundColor = backGroundColor
        return this
    }

    fun show(context: Context?) {
        try {
            apToast?.toast?.let { toast ->
                toast.cancel()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            apToast!!.build(context).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun build(context: Context?): Toast {
        val toast = Toast(context)
        this.toast = toast
        toast.duration = duration
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.view_custom_toast, null)
        val customToastContainer =
            view.findViewById<View>(R.id.custom_toast_container)
        val background = customToastContainer.background
        if (background is ShapeDrawable) {
            background.paint.color = backGroundColor
        } else if (background is GradientDrawable) {
            background.setColor(backGroundColor)
        } else if (background is ColorDrawable) {
            background.color = backGroundColor
        }
        val textView = view.findViewById<TextView>(R.id.text)
        textView.setTextColor(colorText)
        if (contentResId != 0) {
            textView.setText(contentResId)
        } else {
            textView.text =
                if (isHtml) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
                    text,
                    Html.FROM_HTML_MODE_COMPACT
                ) else Html.fromHtml(text) else text
        }
        val img =
            view.findViewById<ImageView>(R.id.img_toast)
        img.setImageResource(idImgToast)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
        toast.setView(view)
        return toast
    }

    fun setDuration(duration: Int): AppToast {
        this.duration = duration
        return this
    }

    companion object {
        var toast: Toast? = null
        var apToast: AppToast? = null
        fun createToast(toastStyle: ToastStyle?): AppToast {
            when (toastStyle) {
                ToastStyle.ERROR -> return createToastError()
                ToastStyle.WARNING -> return createToastWarning()
            }
            return createToastLiked()
        }

        fun showToastNetworkErrorWithTitle(
            context: Context?,
            title: String?
        ) {
            createToast(ToastStyle.ERROR).setText(title).show(context)
        }

        private fun createToastLiked(): AppToast {
            val appToast = AppToast()
            apToast = appToast
            return appToast.setImgToast(R.drawable.ic_toast_done)
                .setBackGroundColor(Color.parseColor("#EAFAED"))
                .setColorTextToast(Color.parseColor("#079B20"))
        }

        private fun createToastWarning(): AppToast {
            val appToast = AppToast()
            apToast = appToast
            return appToast.setImgToast(R.drawable.ic_toast_warning)
                .setBackGroundColor(Color.parseColor("#FFFAE6"))
                .setColorTextToast(Color.parseColor("#C49C00"))
        }

        private fun createToastError(): AppToast {
            val appToast = AppToast()
            apToast = appToast
            return appToast.setImgToast(R.drawable.ic_toast_cancel)
                .setBackGroundColor(Color.parseColor("#FEEBE8"))
                .setColorTextToast(Color.parseColor("#F43319"))
        }
    }
}

enum class ToastStyle {
    WARNING, DONE, ERROR, DOWNLOADING, DOWNLOAD_COMPLETE
}