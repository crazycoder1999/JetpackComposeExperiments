package com.andrea.degaetano.adoptapuppy

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.andrea.degaetano.adoptapuppy.ui.theme.AdoptAPuppyTheme
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    val puppiesNames = listOf("Fuffy", "Black","Lilli","Kitty","Nerone","Happy","Ludvic", "Mercury","Venus","Saturn","Mars","Jupiter","Moon")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainApp {
                MyNavController(puppiesNames)
            }
        }
    }
}

@Composable
fun MyNavController(puppyList : List<String>) {
    val navController = rememberNavController()
    val puppyPhotos = listOf(R.drawable.p1,R.drawable.p2,R.drawable.p3);
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainScreenOfPuppies(navController,puppyList)
        }
        composable( "showPuppy/{pname}") { bstackEntry ->
            MyPuppyPage(navController = navController,
                bstackEntry.arguments?.getString("pname")?:"None",
                puppyPhotos[Random(System.nanoTime()).nextInt(0,3)],
                Random(System.nanoTime()).nextInt(1,4),
                pedigree = Random(System.nanoTime()).nextInt(0,2)%2 == 0,
                kidFriendly = Random(System.nanoTime()).nextInt(0,2)%2 == 0,
                trained = Random(System.nanoTime()).nextInt(0,2)%2 == 0)
            //just the name passed.. too lazy and tired to handle all the params now :D
        }

    }

}

@Composable
fun MainApp(content: @Composable () -> Unit){
    AdoptAPuppyTheme {
        Surface(color = Color.LightGray) {
            content()
        }
    }
}

@Composable
fun MyPuppyPage(navController: NavHostController?,name: String,
                resImage : Int,
                age : Int,
                pedigree: Boolean,
                kidFriendly: Boolean,
                trained: Boolean
) {
    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()){
            PuppyName(name,age)
            PuppyPhoto(resImage = resImage)
            Text("These are the characteristics of this puppy:",modifier = Modifier.padding(8.dp))
            PuppyProp("Pedigree",pedigree)
            PuppyProp("Kid Friendly",kidFriendly)
            PuppyProp("Trained",trained)

            Button(onClick = { navController?.popBackStack() }, Modifier.fillMaxWidth(1f).padding(8.dp)) {
                Text(text = "Back", color = Color.White)
            }
    }

}

@Composable
fun PuppyPhoto(resImage : Int) {
    val image: Painter = painterResource(id = resImage)
    Image(painter = image,contentDescription = "puppy",modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(0.4f))
}

@Composable
fun PuppyName(name: String,age: Int) {
    Text(text = "This puppy is: $name. Age: $age",
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun PuppyProp(propName: String,isPresent: Boolean) {
    if ( isPresent ) {
        Text(
            text = "- $propName",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun MainScreenOfPuppies(navController: NavHostController?,names: List<String>) {

    Column(modifier = Modifier.fillMaxHeight()){
        ListOfNames({ fuffyId ->
            Log.d("clicked..","on $fuffyId")
            navController?.navigate("showPuppy/"+names[fuffyId]) },names = names,modifier = Modifier.weight(1.0f))
    }
}

@Composable
fun ListOfNames(onClickedName: (Int) -> Unit,names: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(names) { idx,oneName ->
            PuppyCell(onClickedName,oneName,idx)
        }
    }
}

@Composable
fun PuppyCell(onClickedName: (Int) -> Unit,name : String,idx : Int) {
    PuppyElement(onClickedName,name,idx)
    Divider(color = Color.Black)
}

@Composable
fun PuppyElement(onClickedName: (Int) -> Unit,name: String, idx : Int) {

    Text(text = "$name!",
        modifier = Modifier
            .padding(18.dp)
            .clickable(onClick = {
                onClickedName(idx)
            })
    )
}

@Preview("Puppy Preview")
@Composable
fun PuppyPreview() {
    MainApp {
        MyPuppyPage(null,"brian",R.drawable.p3,3,true,false,trained = true)
    }
}

@Preview("MyScreen preview")
@Composable
fun DefaultPreview() {
    MainApp {
        MainScreenOfPuppies(null,listOf("One","Two","Three"))
    }
}
