package hu.ma.charts.sample

import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController

enum class Screen(val route: String) {
  Root("root"),
}

@Composable
fun RootScreen() {
  val controller = rememberNavController()

  Scaffold(
    topBar = {
      TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = {
          Text("Charts Sample")
        }
      )
    },
    bodyContent = {
      NavHost(navController = controller, startDestination = Screen.Root.route) {
        composable(Screen.Root.route) {
          Surface {

          }
        }
      }
    }
  )
}
