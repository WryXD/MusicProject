package com.example.musicproject.ui.custom_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicproject.R
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.ui.theme.RobotoFont

@Composable
fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = RobotoFont.bodyLarge,
        modifier = modifier,
        color = Color.White
    )
}

@Composable
fun Email(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    placeholder: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        shape = RoundedCornerShape(15.dp),
        placeholder = placeholder,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}

@Composable
fun Password(
    modifier: Modifier = Modifier,
    value: String,
    isShowingPassword: Boolean ,
    onTogglePasswordVisibility: () -> Unit,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    placeholder: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        shape = RoundedCornerShape(15.dp),
        placeholder = placeholder,
        maxLines = 1,
        trailingIcon = {
            val icon = if (isShowingPassword) {
                painterResource(id = R.drawable.visibility)
            } else {
                painterResource(id = R.drawable.visibility_off)
            }
            IconButton(
                onClick = {
                    onTogglePasswordVisibility()
                }
            ) {
                Icon(
                    painter = icon,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (isShowingPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier
    )
}

@Composable
fun NameOrSurName(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    placeholder: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        shape = RoundedCornerShape(15.dp),
        placeholder = placeholder,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}


@Composable
fun Placeholder(text: String) {
    Text(text = text, color = Color.White)
}

@Composable
fun AppPolicy(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = Color.DarkGray,
    colorPolicy: Color = Color.White,
    policy1: String? = null,
    policy2: String? = null,
) {
    Text(
        text = buildAnnotatedString {

            withStyle(style = SpanStyle(color = color, fontSize = 14.sp)) {
                append(title)
            }

            withStyle(
                style = SpanStyle(color = colorPolicy, fontSize = 14.sp)
            ) {
                append(policy1)
            }

            withStyle(style = SpanStyle(color = color, fontSize = 14.sp)) {
                append("và ")
            }

            withStyle(style = SpanStyle(color = colorPolicy, fontSize = 14.sp)) {
                append(policy2)
            }
        },
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
fun AppButton(
    onClick: () -> Unit,
    title: String,
    isEnable: Boolean = true,
    colors: ButtonColors,
    style: TextStyle = RobotoFont.bodySmall,
    modifier: Modifier,
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier,
        enabled = isEnable,
        shape = RoundedCornerShape(15.dp),
    ) {
        Text(
            text = title,
            style = style
        )
    }
}

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.DarkGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Color.White
            )
        }
    }

}

@Composable
fun PasswordHint(
    modifier: Modifier = Modifier,
    title: String,
    hint: String,
    hintColor: Color = Color.Yellow,
    color: Color = DarkWhite,
) {
    Text(
        text = buildAnnotatedString {
            append(title)
            withStyle(
                style = SpanStyle(
                    color = hintColor,
                    fontSize = 16.sp,
                )
            ) {
                append(hint)
            }
        },
        color = color,
        modifier = modifier
    )
}

