package dev.costas.vertext.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ContentViewModel : ViewModel() {
    var title by mutableStateOf("")
    var content by mutableStateOf("")
}