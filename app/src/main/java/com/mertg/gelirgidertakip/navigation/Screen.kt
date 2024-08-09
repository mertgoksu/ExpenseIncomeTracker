package com.mertg.gelirgidertakip.navigation

sealed class Screen(val route: String) {
    data object MainPage : Screen(route = "route_main_page")
    data object GelirleriGosterPage : Screen(route = "route_gelirleri_goster_page")
    data object GiderleriGosterPage : Screen(route = "route_giderleri_goster_page")
    data object GelirEklePage : Screen(route = "route_gelir_ekle_page")
    data object GiderEklePage : Screen(route = "route_gider_ekle_page")
    data object IstatistikPage : Screen(route = "route_istatistik_page")
    data object ProfilePage : Screen(route = "route_profile_page")
    data object LoginPage : Screen(route = "login_route")
    data object RegisterPage : Screen(route = "register_route")
    data object TumunuGosterPage : Screen(route = "tumunu_goster_route")
    data object SplashScreen : Screen(route = "splash_screen")
    data object MainScaffold : Screen(route = "main_scaffold_route")
}
