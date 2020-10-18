package xyz.klinker.messenger.shared.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.View
import android.widget.TextView
import xyz.klinker.android.floating_tutorial.FloatingTutorialActivity
import xyz.klinker.android.floating_tutorial.TutorialPage
import xyz.klinker.messenger.api.implementation.Account
import xyz.klinker.messenger.shared.R
import xyz.klinker.messenger.shared.data.Settings
import xyz.klinker.messenger.shared.util.DensityUtil

class AppTransferDialog : FloatingTutorialActivity() {
    override fun getPages(): List<TutorialPage> = if (Account.exists()) {
        listOf(AccountUserPager(this))
    } else {
        listOf(NoAccountUserPager(this))
    }
}

private abstract class AppTransferPage(private val activity: AppTransferDialog) : TutorialPage(activity) {

    abstract val bottomTextRes: Int

    private val topContainer: View by lazy { findViewById<View>(R.id.top_container) }
    private val bottomText: TextView by lazy { findViewById<TextView>(R.id.bottom_text) }

    private val nextButton: View by lazy { findViewById<View>(R.id.continue_button) }
    private val cancelButton: View by lazy { findViewById<View>(R.id.cancel_button) }
    private val bottomButtons: View by lazy { findViewById<View>(R.id.bottom_buttons) }
    private val originalButtons: View by lazy { findViewById<View>(R.id.tutorial_progress).parent as View }

    override fun initPage() {
        setContentView(R.layout.page_account_transfer)
        originalButtons.visibility = GONE

        topContainer.backgroundTintList = ColorStateList.valueOf(Settings.mainColorSet.color)
        bottomText.text = resources.getString(bottomTextRes)

        nextButton.backgroundTintList = ColorStateList.valueOf(Settings.mainColorSet.color)
        nextButton.setOnClickListener {
            activity.finishAnimated()
        }

        cancelButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://messenger.klinkerapps.com/privacy.html")
            context.startActivity(intent)
        }
    }

    override fun animateLayout() {
        quickViewReveal(bottomText, 300)
        quickViewReveal(bottomButtons, 450)
    }

    private fun quickViewReveal(view: View, delay: Long) {
        view.translationX = (-1 * DensityUtil.toDp(activity, 16)).toFloat()
        view.alpha = 0f
        view.visibility = View.VISIBLE

        view.animate()
                .translationX(0f)
                .alpha(1f)
                .setStartDelay(delay)
                .start()
    }

}

@SuppressLint("ViewConstructor")
private class AccountUserPager(activity: AppTransferDialog) : AppTransferPage(activity) {
    override val bottomTextRes = R.string.new_owner_with_account
}

@SuppressLint("ViewConstructor")
private class NoAccountUserPager(activity: AppTransferDialog) : AppTransferPage(activity) {
    override val bottomTextRes = R.string.new_owner_no_account
}