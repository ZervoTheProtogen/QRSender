import android.app.Activity
import android.content.Intent
import com.journeyapps.barcodescanner.CaptureActivity

class QRCodeScanner {

    fun initiateScan(activity: Activity) {
        val intent = Intent(activity, CaptureActivity::class.java)
        activity.startActivityForResult(intent, SCAN_REQUEST_CODE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): String? {
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("SCAN_RESULT")
            return result
        }
        return null
    }

    companion object {
        private const val SCAN_REQUEST_CODE = 123
    }
}