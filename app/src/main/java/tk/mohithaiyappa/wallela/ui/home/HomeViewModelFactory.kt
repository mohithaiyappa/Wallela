package tk.mohithaiyappa.wallela.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tk.mohithaiyappa.wallela.DataBaseHelper

class HomeViewModelFactory(
    val context: Context
): ViewModelProvider.NewInstanceFactory() {

     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(DataBaseHelper(context)) as T
    }
}