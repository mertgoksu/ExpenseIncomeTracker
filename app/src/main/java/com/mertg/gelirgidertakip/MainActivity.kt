package com.mertg.gelirgidertakip

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.model.GiderModel
import com.mertg.gelirgidertakip.navigation.Screen
import com.mertg.gelirgidertakip.ui.theme.CustomKirmizi
import com.mertg.gelirgidertakip.ui.theme.CustomYesil
import com.mertg.gelirgidertakip.ui.theme.GelirGiderTakipTheme
import com.mertg.gelirgidertakip.view.*
import com.mertg.gelirgidertakip.viewmodel.AuthViewModel
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GelirGiderTakipTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    StartNavigation(navController = navController)
                }
            }
        }
    }
}

@Composable
fun StartNavigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = remember { AuthViewModel() }

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController,authViewModel)
        }
        composable(Screen.MainScaffold.route) {
            MainScaffold()
        }
        composable(Screen.LoginPage.route) {
            LoginScreen(navController = navController, authViewModel)
        }
        composable(Screen.RegisterPage.route) {
            RegisterScreen(navController = navController, authViewModel)
        }
    }
}


@Composable
fun MainScaffold() {
    val gelirGiderViewModel: GelirGiderViewModel = remember { GelirGiderViewModel() }
    val navController = rememberNavController()
    val toplamBakiye by gelirGiderViewModel.toplamBakiye

    val currentDestination by remember {
        derivedStateOf { navController.currentDestination?.route }
    }

    Scaffold(
        topBar = { TopBar(navController, toplamBakiye, gelirGiderViewModel) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AllAppNavigation(navController, gelirGiderViewModel)
        }
    }
}

@Composable
fun TopBar(navController: NavHostController, toplamBakiye: Int, viewModel: GelirGiderViewModel) {
    val bakiyeColor = when {
        toplamBakiye > 0 -> CustomYesil
        toplamBakiye < 0 -> CustomKirmizi
        else -> Color.Black // toplamBakiye 0 ise siyah renk
    }
    val bakiyeText = when {
        toplamBakiye > 0 -> "+$toplamBakiye"
        toplamBakiye < 0 -> "-${toplamBakiye * -1}"
        else -> "$toplamBakiye" // toplamBakiye 0 ise işaretsiz
    }

    var selectedFilter by remember { mutableStateOf("Bu Ay") }
    val filters = listOf("Bu Ay", "Geçen Ay", "Tümü")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$bakiyeText₺", fontSize = 24.sp, color = bakiyeColor, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Toplam Bakiye", fontSize = 16.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            Column(
                Modifier.padding(5.dp).padding(top = 5.dp, bottom = 5.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.hesaplaToplamlar()
                        navController.navigate(Screen.GelirleriGosterPage.route)
                    },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        text = "Gelirler",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                Modifier.padding(5.dp).padding(top = 5.dp, bottom = 5.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.hesaplaToplamlar()
                        navController.navigate(Screen.GiderleriGosterPage.route)
                    },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        text = "Giderler",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                Modifier.padding(5.dp).padding(top = 5.dp, bottom = 5.dp)
            ) {
                Button(
                    onClick = { /*Do nothing for dropdown*/ },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Box {
                        var expanded by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .clickable { expanded = true }
                        ) {
                            Text(
                                text = selectedFilter,
                                modifier = Modifier
                                    .clickable { expanded = true },
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Arrow",
                                tint = Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filters.forEach { filter ->
                                DropdownMenuItem(
                                    text = { Text(filter) },
                                    onClick = {
                                        selectedFilter = filter
                                        viewModel.applyFilter(filter)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}




@Composable
fun AllAppNavigation(navController: NavHostController, gelirGiderViewModel: GelirGiderViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainPage.route,
        modifier = Modifier.padding()
    ) {
        composable(Screen.MainPage.route) { MainPage(navController, gelirGiderViewModel) }
        composable(Screen.IstatistikPage.route) { IstatistikPage(navController, gelirGiderViewModel) }
        composable(Screen.ProfilePage.route) { ProfilePage(navController) }
        composable(Screen.GelirleriGosterPage.route) { GelirleriGosterPage(navController, gelirGiderViewModel) }
        composable(Screen.GiderleriGosterPage.route) { GiderleriGosterPage(navController, gelirGiderViewModel) }
        composable(Screen.GelirEklePage.route) { GelirEklePage(navController, gelirGiderViewModel) }
        composable(Screen.GiderEklePage.route) { GiderEklePage(navController, gelirGiderViewModel) }
        composable(Screen.TumunuGosterPage.route) { TumunuGosterPage(navController, gelirGiderViewModel) }
    }
}



var bottomDescription : String? = null

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.MainPage,
        Screen.IstatistikPage,
        Screen.ProfilePage
    )
    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when(screen) {
                        Screen.MainPage -> {
                            Icon(Icons.Default.Home, contentDescription = "Main Page")
                            bottomDescription = "Anasayfa"
                        }
                        Screen.IstatistikPage -> {
                            Icon(Icons.Default.BarChart, contentDescription = "Istatistik Page")
                            bottomDescription = "İstatistik"
                        }
                        Screen.ProfilePage -> {
                            Icon(Icons.Default.Person, contentDescription = "Profile Page")
                            bottomDescription = "Profil"
                        }
                        else -> Icon(Icons.Default.Home, contentDescription = null)
                    }
                },
                label = { bottomDescription?.let { Text(text = it) } },
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        /* popUpTo(navController.graph.startDestinationId) {
                             saveState = true
                         }
                         launchSingleTop = true
                         restoreState = true*/
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black, // Seçili ikon rengi
                    unselectedIconColor = Color.Black, // Seçili olmayan ikon rengi
                    selectedTextColor = Color.Black, // Seçili etiket rengi
                    unselectedTextColor = Color.Black, // Seçili olmayan etiket rengi
                    indicatorColor = Color.Transparent // Seçili öğe arka plan rengi
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainScaffold() {
    GelirGiderTakipTheme {
        MainScaffold()
    }
}

fun calculateToplamBakiye(gelirler: List<GelirModel>, giderler: List<GiderModel>): Int {
    val toplamGelir = gelirler.sumOf { it.amount.toInt() }
    val toplamGider = giderler.sumOf { it.amount.toInt() }
    return toplamGelir - toplamGider
}

