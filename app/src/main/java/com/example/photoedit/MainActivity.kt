package com.example.photoedit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.photoedit.ui.theme.PhotoEditTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoEditTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        SelectImage()
                }
            }
        }
    }
}



@Composable
fun SelectImage() {

    val ctx = LocalContext.current
    val imageUri = remember{mutableStateOf<Uri?>(null)}
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            imageUri.value = uri
            if (uri != null) {
                launchHandlingActivity(ctx, uri)
            }
        })

    //Launch image app
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {success ->
            if (success && imageUri.value != null)
                launchHandlingActivity(ctx, imageUri.value!!)
        })

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.edit)
            , contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
            )
    }

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
            /*Text(text = "Import an Image", fontSize = 25.sp, fontWeight = FontWeight.Bold)*/

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.1.em)) {
                    append("Photo")
                }
                withStyle(style = SpanStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.em, color = Color.Red)) {
                    append("E")
                }
                withStyle(style = SpanStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.1.em)) {
                    append("ditor")
                }
            }
        )


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            /*Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(120.dp)
                    .height(120.dp)
                    .clip(RectangleShape),
                onClick = { imagePicker.launch("image/*") }
            ) {
                Text(text = "Picker")
            }*/

             */
            StylishButton(
                text = "Picker",
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )


            StylishButton(
                text = "Camera",
                onClick = {
                    val uri = ComposeFileProvider.getImageUri(ctx)
                    imageUri.value = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )


            /*Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(120.dp)
                    .height(120.dp)
                    .clip(RectangleShape),
                onClick = {
                    //Where image has to stored
                    val uri = ComposeFileProvider.getImageUri(ctx)
                    imageUri.value = uri
                    cameraLauncher.launch(uri)
                }
            ) {
                Text(text = "Camera")
            }*/
        }
    }
}

//Extra photo on screen
fun launchHandlingActivity(ctx: Context, uri: Uri) {
    val intent = Intent( ctx, EditActivity::class.java)
    intent.putExtra("imageUri", uri.toString())
    ctx.startActivity(intent)
}

@Composable
fun StylishButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}


