package com.bestypie.jettipapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.FlutterDash
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestypie.jettipapp.components.InputField
import com.bestypie.jettipapp.ui.theme.JetTipAppTheme
import com.bestypie.jettipapp.widgets.RoundedButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit)  {
    // A surface container using the 'background' color from the theme
    JetTipAppTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content();
        }
    }

}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
 Surface(modifier = Modifier
     .fillMaxWidth()
     .height(150.dp)
     .background(
         Color(0xFFE9D7F7)
     )
     .clip(shape = RoundedCornerShape(CornerSize(12.dp)))) {
        Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            val total = "%.2f".format(totalPerPerson);
            Text(text = "Total Per Person",  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, ))
            Spacer(modifier = Modifier.height(height = 10.dp))
            Text(text = "$$total", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp, ))
        }
    }
}

@Preview
@Composable
fun MainContent() {
    BillForm() {billAmount ->
        Log.d(TAG, "MainContent: $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier, onChangeValue: (String) -> Unit = {}) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val valueState = remember {
        mutableStateOf(0)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(), shape = RoundedCornerShape(corner = CornerSize(12.dp)), border = BorderStroke(width = 2.dp, color = Color.LightGray)) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
            InputField(valueState = totalBillState, labelId = "Enter Bill", enabled =  true, isSingleLine = true, onAction = KeyboardActions {
                if(!validState) return@KeyboardActions
                onChangeValue(totalBillState.value.trim())
                //
                keyboardController?.hide()
            })
            if (validState) {
                    Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Split", modifier= Modifier.align(alignment = Alignment.CenterVertically))
                        Spacer(modifier = modifier.width(150.dp))
                        Row(modifier=Modifier.padding(horizontal = 18.dp), horizontalArrangement = Arrangement.Center) {
                            RoundedButton(imageVector = Icons.Default.Remove, onClick = {
                                if (valueState.value > 1) {
                                    valueState.value -=1;
                                } else 1;
                            }, modifier = Modifier)
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "${valueState.value}", style = MaterialTheme.typography.h4)
                            Spacer(modifier = Modifier.width(20.dp))
                            RoundedButton(imageVector = Icons.Default.Add, onClick = {
                                valueState.value += 1;
                            }, modifier = Modifier)
                        }

                    }
            } else {
                Box(modifier = Modifier) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        Text(text = "My App")
    }
}