package ru.otus.danilov.findafilm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Film (val drawableId: Int, val descriptionId: Int, val titleId: Int) : Parcelable