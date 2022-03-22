package tk.mohithaiyappa.wallela.ui.home

import android.os.AsyncTask
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import tk.mohithaiyappa.wallela.DataBaseHelper
import tk.mohithaiyappa.wallela.data.UrlDataStorage
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(val dataBaseHelper: DataBaseHelper) : ViewModel() {

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private val wallpaperUrlListMutableLiveData: MutableLiveData<ArrayList<UrlDataStorage>> = MutableLiveData()

    val urlListLiveData: LiveData<ArrayList<UrlDataStorage>>
        get() = wallpaperUrlListMutableLiveData
    var inFavorites = false

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("Flat-Art/")
        fetchData()
    }

    private fun fetchData() {
        val urlArrayList = arrayListOf<UrlDataStorage>()
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                urlArrayList.clear()
                for (data in dataSnapshot.children) {
                    val urlDataStorage = UrlDataStorage(
                        data.child("midResUrl").getValue(
                            String::class.java
                        ),
                        data.child("Url").getValue(String::class.java),
                        data.child("lowResUrl").getValue(String::class.java)
                    )
                    urlArrayList.add(urlDataStorage)
                }
                urlArrayList.shuffle()
                wallpaperUrlListMutableLiveData.postValue(urlArrayList)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun fetchDataForLabel(label: String){
        inFavorites = false
        databaseReference = firebaseDatabase!!.getReference(label)
        fetchData()
    }

    fun loadFavorites() {
        inFavorites = true
        loadAsycFavorites().execute()
    }

    private inner class loadAsycFavorites : AsyncTask<Any?, Any?, Any?>() {
        val arrayList = arrayListOf<UrlDataStorage>()
        private var aBoolean = false
        override fun doInBackground(objects: Array<Any?>): Any? {
            val db = dataBaseHelper
            aBoolean = false
            val cursor = db.data
            arrayList.clear()
            if (cursor == null) {
                aBoolean = true
            } else {
                while (cursor.moveToNext()) {
                    val urlDataStorage = UrlDataStorage(
                        cursor.getString(2),
                        cursor.getString(3), cursor.getString(1)
                    )
                    arrayList.add(urlDataStorage)
                }
            }
            return null
        }

        override fun onPostExecute(o: Any?) {
            super.onPostExecute(o)
            wallpaperUrlListMutableLiveData.postValue(arrayList)
        }
    }
}