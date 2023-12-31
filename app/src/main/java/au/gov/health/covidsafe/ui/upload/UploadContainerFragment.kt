package au.gov.health.covidsafe.ui.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import au.gov.health.covidsafe.R
import au.gov.health.covidsafe.ui.base.PagerContainer
import au.gov.health.covidsafe.ui.base.UploadButtonLayout
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_under_sixteen.*
import kotlinx.android.synthetic.main.fragment_upload_master.*
import kotlinx.android.synthetic.main.fragment_upload_master.root
import kotlinx.android.synthetic.main.fragment_upload_master.toolbar

class UploadContainerFragment : Fragment(), PagerContainer {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_upload_master, container, false)

    override fun onResume() {
        super.onResume()
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_RTL) {
            toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_rtl)
        }
    }

    override fun onPause() {
        super.onPause()
        toolbar.setNavigationOnClickListener(null)
    }

    override fun updateSteps(step: Int?, totalSteps: Int) {
        // do nothing
    }

    override fun setNavigationIcon(navigationIcon: Int?) {
        if (navigationIcon == null) {
            toolbar.navigationIcon = null
        } else {
            activity?.let {
                toolbar.navigationIcon = ContextCompat.getDrawable(it, navigationIcon)
            }
        }
    }

    override fun refreshButton(updateButtonLayout: UploadButtonLayout) {
        when (updateButtonLayout) {
            is UploadButtonLayout.ContinueLayout -> {
                upload_continue.setOnClickListener {
                    updateButtonLayout.buttonListener?.invoke()
                }
                upload_continue.setText(updateButtonLayout.buttonText)
                upload_continue.visibility = VISIBLE
                upload_answerNo.setOnClickListener(null)
                upload_answerYes.setOnClickListener(null)
                upload_answerNo.visibility = GONE
                upload_answerYes.visibility = GONE
            }
            is UploadButtonLayout.QuestionLayout -> {
                upload_continue.setOnClickListener(null)
                upload_continue.visibility = GONE
                upload_answerNo.setOnClickListener {
                    updateButtonLayout.buttonNoListener.invoke()
                }
                upload_answerYes.setOnClickListener {
                    updateButtonLayout.buttonYesListener.invoke()
                }
                upload_answerNo.visibility = VISIBLE
                upload_answerYes.visibility = VISIBLE
            }
        }
    }

    override fun visibleConsentButton() {
        under_sixteen_text.visibility = View.GONE
    }

    override fun invisibleConsentButton() {
    }

    override fun enableNextButton() {
        upload_continue.isEnabled = true
    }

    override fun disableNextButton() {
        upload_continue.isEnabled = false
    }

    override fun showLoading() {
        upload_continue.showProgress {
            progressColorRes = R.color.slack_black_2
        }
    }

    override fun hideLoading(@StringRes stringRes: Int?) {
        if (stringRes == null) {
            upload_continue.hideProgress()
        } else {
            upload_continue.hideProgress(newTextRes = stringRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        root.removeAllViews()
    }
}