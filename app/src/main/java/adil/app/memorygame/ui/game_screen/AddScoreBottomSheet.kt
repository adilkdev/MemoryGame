package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import adil.app.memorygame.databinding.BottomSheetScoreBinding
import adil.app.memorygame.utils.AppConstants
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class AddScoreBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetScoreBinding
    lateinit var listener: AddUserListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_score, container, false)
        binding = BottomSheetScoreBinding.bind(view)

        binding.btnDone.setOnClickListener {
            if (binding.etName.text.toString().trim().isNotEmpty()) {
                listener.onAddUserButtonClicked(binding.etName.text.toString().trim())
                dismiss()
            } else {
                showSnackbarAtTop(getString(R.string.fill_name))
            }
        }
        return view
    }

    /**
     * Method to show the snackbar at the top of the bottom sheet dialog.
     * @param text is the message to be displayed by the snackbar.
     */
    private fun showSnackbarAtTop(text: String) {
        val snackBarView =
            Snackbar.make(binding.coordinatorLayout, text, AppConstants.SNACKBAR_DURATION)
        val view = snackBarView.view
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        view.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.snackbar_bg
        )
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

}

interface AddUserListener {
    fun onAddUserButtonClicked(name: String)
}