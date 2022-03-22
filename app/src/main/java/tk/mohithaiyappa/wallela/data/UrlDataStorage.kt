package tk.mohithaiyappa.wallela.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UrlDataStorage(
    val midResUrl: String?,
    val hiResUrl: String?,
    val lowResUrl: String?
): Parcelable