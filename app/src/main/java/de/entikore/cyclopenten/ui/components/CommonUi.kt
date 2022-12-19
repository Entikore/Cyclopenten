package de.entikore.cyclopenten.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.theme.ColorTheme

@Composable
fun ColoredButton(
    buttonText: String,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colors.primaryVariant,
    disabledColor: Color = color,
    borderStroke: BorderStroke = ButtonDefaults.outlinedBorder
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.elevation(),
        shape = RoundedCornerShape(25),
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            disabledBackgroundColor = disabledColor
        ),
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            color = textColor,
            style = MaterialTheme.typography.button,
            fontSize = MaterialTheme.typography.body1.fontSize
        )
    }
}

@Composable
fun Title(
    title: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = MaterialTheme.typography.h4.fontSize
) {
    Text(
        text = title,
        color = textColor,
        fontSize = fontSize,
        modifier = modifier.padding(8.dp)
    )
}

@Composable
fun ColoredTextInput(colorTheme: ColorTheme, onClick: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    TextField(
        value = textState.value,
        label = { Text(stringResource(R.string.txt_enter_name), color = colorTheme.accent) },
        onValueChange = {
            textState.value = it
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = colorTheme.dark,
            backgroundColor = colorTheme.accent,
            cursorColor = colorTheme.dark,
            focusedIndicatorColor = colorTheme.dark
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth()
    )
    ColoredButton(
        buttonText = stringResource(R.string.btn_ok),
        textColor = colorTheme.dark,
        onClick = {
            onClick(textState.value.text)
            textState.value = TextFieldValue("")
        },
        color = colorTheme.accent,
        borderStroke = BorderStroke(2.dp, colorTheme.dark),
        modifier = Modifier.fillMaxWidth()
    )
}
