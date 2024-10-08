package dev.elbullazul.linkguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.ROUTE_DASHBOARD
import dev.elbullazul.linkguardian.navigation.ROUTE_LOGIN
import dev.elbullazul.linkguardian.navigation.ROUTE_SETTINGS
import dev.elbullazul.linkguardian.navigation.ROUTE_SUBMIT_LINK
import dev.elbullazul.linkguardian.navigation.destinations
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current
    val prefs = PreferencesManager(context)
    val navController = rememberNavController()

    val loggedIn = rememberSaveable { (mutableStateOf(false)) }
    val displayBottomBar = rememberSaveable { (mutableStateOf(false)) }
    val displayBackButton = rememberSaveable { (mutableStateOf(false)) }
    val displayFloatingButton = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        ROUTE_LOGIN -> {
            displayBottomBar.value = false
            displayFloatingButton.value = false
            displayBackButton.value = false
        }
        ROUTE_DASHBOARD -> {
            displayBottomBar.value = true
            displayFloatingButton.value = true
            displayBackButton.value = false
        }
        ROUTE_SETTINGS -> {
            displayBottomBar.value = true
            displayFloatingButton.value = false
            displayBackButton.value = false
        }
        ROUTE_SUBMIT_LINK -> {
            displayBottomBar.value = true
            displayFloatingButton.value = false
            displayBackButton.value = true
        }
    }

    if (prefs.load())
        loggedIn.value = true

    LinkGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        if (displayBackButton.value) {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (displayBottomBar.value) {
                    BottomAppBar {
                        for (dest in destinations) {
                            NavigationBarItem(
                                selected = (navController.currentDestination?.route == dest.route),
                                onClick = { navController.navigate(dest.route) },
                                icon = { Icon(imageVector = dest.icon, contentDescription = "") },
                                label = { Text(text = stringResource(id = dest.label)) }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (displayFloatingButton.value) {
                    FloatingActionButton(
                        onClick = { navController.navigate(ROUTE_SUBMIT_LINK) },
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                val startDestination = if (loggedIn.value)
                    ROUTE_DASHBOARD
                else
                    ROUTE_LOGIN

                AppNavController(navController = navController, startDestination = startDestination)
            }
        }
    }
}