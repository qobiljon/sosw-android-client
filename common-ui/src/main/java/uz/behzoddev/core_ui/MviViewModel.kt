package uz.behzoddev.core_ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class MviViewModel<VS : MviViewState, SE : MviSideEffect>
    (initialState: VS) : ViewModel() {

    private var _uiState: MutableStateFlow<VS> = MutableStateFlow(initialState)
    val uiState: StateFlow<VS> = _uiState.asStateFlow()

    private var _uiSideEffect = Channel<SE>(capacity = Channel.BUFFERED)
    val uiSideEffect = _uiSideEffect.receiveAsFlow()

    val currentState = uiState.value

    protected fun reduceState(action: (VS) -> VS): VS {
        return _uiState.updateAndGet(action)
    }

    private fun send(sideEffect: SE) {
        _uiSideEffect.trySend(sideEffect)
    }

}