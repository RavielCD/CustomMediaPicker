package com.ravielcd.mediapicker.mediapickerbottom.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.ravielcd.mediapicker.mediapickerbottom.models.Media
import com.ravielcd.mediapicker.mediapickerbottom.repositories.MediaRepository
import javax.inject.Inject

class MediasViewModel : ViewModel {
    private var medias: LiveData<List<Media>>? = null
    private var selectedMedia: LiveData<Media>? = null
    private var repository: MediaRepository

    @Inject // MediaRepository parameter is provided by Dagger 2
    constructor(repository: MediaRepository) {
        this.repository = repository

        medias = this.repository.getAllMedias()
    }
}