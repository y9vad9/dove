package com.dove.android.screens.authorization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dove.android.locale.Strings
import com.dove.frontend.common.viewmodel.AuthorizationViewModel
import com.dove.frontend.common.viewmodel.integrated.AuthorizationScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AuthorizationScreen(viewModel: AuthorizationViewModel = AuthorizationScreenViewModel()) = Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = { Toolbar() }
) {
    when (viewModel.stage.collectAsState().value) {
        AuthorizationViewModel.Stage.EmailInput -> EmailSendingStageContent(
            viewModel.email,
            viewModel::processEmail,
            {})
        AuthorizationViewModel.Stage.CodeInput -> CodeSendingStageContent(viewModel.code, viewModel::processCode, {})
    }
}

@Composable
private fun EmailSendingStageContent(
    email: StateFlow<String>,
    onEmailChanged: (String) -> Unit,
    onButtonClicked: () -> Unit
) = Box(
    modifier = Modifier.fillMaxSize().padding(8.dp)
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email.collectAsState().value,
        onValueChange = onEmailChanged,
        placeholder = { Text(Strings.email) },
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = Strings.email) }
    )
    Button(
        onClick = onButtonClicked,
    ) {
        Text(text = Strings.send)
    }
}

@Composable
private fun CodeSendingStageContent(
    code: StateFlow<String>,
    onCodeChanged: (String) -> Unit,
    onButtonClicked: () -> Unit
) = Box(
    modifier = Modifier.fillMaxSize().padding(8.dp)
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = onCodeChanged,
        placeholder = { Text(Strings.code) },
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.VpnKey, contentDescription = Strings.code) }
    )
    Button(
        onClick = onButtonClicked,
    ) {
        Text(Strings.send)
    }
}

@Composable
private fun Toolbar() = TopAppBar(
    modifier = Modifier.fillMaxWidth(),
    title = { Text(Strings.authorization) }
)