package ru.alex.danilov.findafilm

import android.os.Parcelable
import kotlinx.parcel.Parcelize

class Film(val drawableId: Int, val descriptionId: Int, val titleId: Int) : Parcelable
