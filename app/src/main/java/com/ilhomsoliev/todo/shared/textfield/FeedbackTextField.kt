package com.ilhomsoliev.todo.shared.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.theme.AppTheme
import com.ilhomsoliev.theme.TodoTheme

@Composable
@Preview
fun FeedbackTextFieldPreviewDarkTheme() {
    TodoTheme(true) {
        Column {
            FeedbackTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "123",
            ) {}
            HorizontalDivider()
            FeedbackTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
            ) {}
        }
    }
}

@Composable
@Preview
fun FeedbackTextFieldPreviewLightTheme() {
    TodoTheme(false) {
        Column {
            FeedbackTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "123",
            ) {}
            HorizontalDivider()
            FeedbackTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
            ) {}
        }
    }
}

@Composable
fun FeedbackTextField(
    modifier: Modifier = Modifier,
    value: String,
    innerPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
    onValue: (String) -> Unit,
) {

    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = AppTheme.typography.body.copy(
            color = AppTheme.colorScheme.labelPrimary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        minLines = 5,
        onValueChange = { newValue ->
            onValue(newValue)
        }) { textField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(AppTheme.colorScheme.backSecondary)
                .padding(innerPadding),
            contentAlignment = Alignment.TopStart
        ) {
            textField()
            if (value.isEmpty())
                Text(
                    text = stringResource(R.string.what_needs_to_be_done),
                    style = AppTheme.typography.body.copy(
                        color = AppTheme.colorScheme.labelPrimary,
                    )
                )
        }
    }
}
