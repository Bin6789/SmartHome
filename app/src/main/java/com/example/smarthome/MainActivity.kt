package com.example.smarthome

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(R.layout.activity_main)

        // Khởi tạo NavController từ NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment không tìm thấy. Kiểm tra activity_main.xml")
            return
        }
        navController = navHostFragment.navController
        Log.d("MainActivity", "NavController khởi tạo thành công")

        // Thiết lập điều hướng cho các tab và nút điều hướng
        setupTabNavigation()
        setupBottomNavigation()
        setupThemeToggle()
    }

    // Thiết lập điều hướng cho các tab (Dashboard, Devices, Alerts, History)
    private fun setupTabNavigation() {
        val tabDashboard = findViewById<TextView>(R.id.tab_dashboard)
        val tabDevices = findViewById<TextView>(R.id.tab_devices)
        val tabAlerts = findViewById<TextView>(R.id.tab_alerts)
        val tabHistory = findViewById<TextView>(R.id.tab_history)

        tabDashboard.setOnClickListener {
            updateTabColors(R.id.tab_dashboard)
            navController.navigate(R.id.mainFragment)
        }

        tabDevices.setOnClickListener {
            updateTabColors(R.id.tab_devices)
            navController.navigate(R.id.devicesFragment)
        }

        tabAlerts.setOnClickListener {
            updateTabColors(R.id.tab_alerts)
            navController.navigate(R.id.alertsFragment)
        }

        tabHistory.setOnClickListener {
            updateTabColors(R.id.tab_history)
            val bundle = Bundle().apply { putString("deviceId", "DEV001") }
            navController.navigate(R.id.historyFragment, bundle)
        }

        // Đặt tab Dashboard làm mặc định khi khởi động
        updateTabColors(R.id.tab_dashboard)
    }

    // Thiết lập điều hướng cho thanh dưới cùng (Files, Notifications, Search, Profile, Chat)
    private fun setupBottomNavigation() {
        val navFile = findViewById<ImageView>(R.id.nav_file)
        val navBell = findViewById<ImageView>(R.id.nav_bell)
        val navSearch = findViewById<ImageView>(R.id.nav_search)
        val navUser = findViewById<ImageView>(R.id.nav_user)
        val navChat = findViewById<ImageView>(R.id.nav_chat)

        navFile.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            Log.d("MainActivity", "Đích hiện tại: $currentDestination")
            if (currentDestination != R.id.filesFragment) {
                if (currentDestination != R.id.mainFragment) {
                    Log.d("MainActivity", "Quay lại mainFragment")
                    navController.popBackStack(R.id.mainFragment, false)
                }
                Log.d("MainActivity", "Điều hướng đến filesFragment")
                navController.navigate(R.id.action_mainFragment_to_filesFragment)
            }
        }

        navBell.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            Log.d("MainActivity", "Đích hiện tại: $currentDestination")
            if (currentDestination != R.id.notificationsFragment) {
                if (currentDestination != R.id.mainFragment) {
                    Log.d("MainActivity", "Quay lại mainFragment")
                    navController.popBackStack(R.id.mainFragment, false)
                }
                Log.d("MainActivity", "Điều hướng đến notificationsFragment")
                navController.navigate(R.id.action_mainFragment_to_notificationsFragment)
            }
        }

        navSearch.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            Log.d("MainActivity", "Đích hiện tại: $currentDestination")
            if (currentDestination != R.id.searchFragment) {
                if (currentDestination != R.id.mainFragment) {
                    Log.d("MainActivity", "Quay lại mainFragment")
                    navController.popBackStack(R.id.mainFragment, false)
                }
                Log.d("MainActivity", "Điều hướng đến searchFragment")
                navController.navigate(R.id.action_mainFragment_to_searchFragment)
            }
        }

        navUser.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            Log.d("MainActivity", "Đích hiện tại: $currentDestination")
            if (currentDestination != R.id.profileFragment) {
                if (currentDestination != R.id.mainFragment) {
                    Log.d("MainActivity", "Quay lại mainFragment")
                    navController.popBackStack(R.id.mainFragment, false)
                }
                Log.d("MainActivity", "Điều hướng đến profileFragment")
                navController.navigate(R.id.action_mainFragment_to_profileFragment)
            }
        }

        navChat.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            Log.d("MainActivity", "Đích hiện tại: $currentDestination")
            if (currentDestination != R.id.chatFragment) {
                if (currentDestination != R.id.mainFragment) {
                    Log.d("MainActivity", "Quay lại mainFragment")
                    navController.popBackStack(R.id.mainFragment, false)
                }
                Log.d("MainActivity", "Điều hướng đến chatFragment")
                navController.navigate(R.id.action_mainFragment_to_chatFragment)
            }
        }
    }

    // Cập nhật màu sắc của các tab khi được chọn
    private fun updateTabColors(selectedTabId: Int) {
        val tabs = listOf(R.id.tab_dashboard, R.id.tab_devices, R.id.tab_alerts, R.id.tab_history)
        tabs.forEach { tabId ->
            val tab = findViewById<TextView>(tabId)
            if (tabId == selectedTabId) {
                tab.setTextColor(resources.getColor(R.color.purple_500, theme))
                tab.setBackgroundColor(resources.getColor(R.color.white, theme))
            } else {
                tab.setTextColor(resources.getColor(R.color.grey_500, theme))
                tab.setBackgroundColor(resources.getColor(R.color.background_light_grey, theme))
            }
        }
    }

    // Thiết lập nút chuyển đổi giao diện sáng/tối với Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun setupThemeToggle() {
        val themeToggle = findViewById<Switch>(R.id.theme_toggle)
        // Đặt trạng thái ban đầu của Switch dựa trên chế độ hiện tại
        themeToggle.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        themeToggle.setOnCheckedChangeListener { _, isChecked ->
            val newMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }

    // Xử lý nút quay lại
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}