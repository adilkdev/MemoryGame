package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.data.repository.UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddScoreBottomSheet : BottomSheetDialogFragment() {

    lateinit var btnDone: Button
    lateinit var etName: EditText
    lateinit var listener: AddUserListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_score, container, false)
        btnDone = view.findViewById(R.id.btnDone)
        etName = view.findViewById(R.id.etName)

        btnDone.setOnClickListener {
            if (etName.text.toString().isNotEmpty())
                listener.onUserAdded(etName.text.toString())
            dismiss()
        }
        return view
    }

}

interface AddUserListener {
    fun onUserAdded(name: String)
}