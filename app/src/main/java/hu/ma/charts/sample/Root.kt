package hu.ma.charts.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.ma.charts.sample.bars.BarsSimpleScreen
import hu.ma.charts.sample.bars.BarsStyledScreen
import hu.ma.charts.sample.lines.LinesSimpleScreen
import hu.ma.charts.sample.pie.PieSimpleScreen
import hu.ma.charts.sample.pie.PieStyledScreen
import hu.ma.charts.sample.table.TableSimpleScreen
import hu.ma.charts.sample.table.TableStyledScreen

val ChartTypes = Screen.values().filter { it != Screen.Root }

@Composable
fun RootScreen() {
  val controller = rememberNavController()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Charts Sample") }
      )
    },
    content = {
      NavHost(navController = controller, startDestination = Screen.Root.route) {
        composable(Screen.Root.route) {
          RootContent(controller)
        }

        composable(Screen.TableSimple.route) {
          TableSimpleScreen()
        }

        composable(Screen.TableStyled.route) {
          TableStyledScreen()
        }

        composable(Screen.PieSimple.route) {
          PieSimpleScreen()
        }

        composable(Screen.PieStyled.route) {
          PieStyledScreen()
        }

        composable(Screen.BarsSimple.route) {
          BarsSimpleScreen()
        }

        composable(Screen.BarsStyled.route) {
          BarsStyledScreen()
        }

        composable(Screen.LinesSimple.route) {
          LinesSimpleScreen()
        }
      }
    }
  )
}

@Composable
private fun RootContent(controller: NavHostController) {
  Column {
    ChartTypes.forEach {
      ChartItem(controller = controller, item = it)

      Divider(startIndent = 24.dp)
    }
  }
}

@Composable
private fun ChartItem(
  controller: NavHostController,
  item: Screen
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        onClick = {
          controller.navigate(item.route)
        }
      )
      .padding(horizontal = 24.dp, vertical = 16.dp)
  ) {
    Text(item.title, style = MaterialTheme.typography.h6)
  }
}
