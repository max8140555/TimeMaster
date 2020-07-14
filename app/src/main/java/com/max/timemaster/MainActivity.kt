package com.max.timemaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.appworks.school.stylish.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.max.timemaster.databinding.ActivityMainBinding
import com.max.timemaster.util.UserManager
import kotlinx.coroutines.awaitAll

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_calendar -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCalendarFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToFavoriteFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_cost -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCostFragment())


                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.myNavHostFragment)
        binding.lifecycleOwner = this

        viewModel.postUser(UserManager.user)
        viewModel.getLiveUserResult()
        viewModel.getLiveMyDateResult()
        viewModel.getAllEventResult()
        viewModel.liveUser.observe(this, Observer {
            it?.let {
                UserManager.user = viewModel.liveUser.value!!
            }
        })
        viewModel.liveMyDate.observe(this, Observer {
            it?.let {
                UserManager.addDate.value?.let { savedDate ->
                        UserManager.myDate.value = viewModel.liveMyDate.value
                        setupDrawer()
                }


            }
        })
        viewModel.liveAllEvent.observe(this, Observer {
            it?.let {
                UserManager.allEvent.value = viewModel.liveAllEvent.value
                Log.d("ccc", "$it")
                Log.d("ccc", "${UserManager.allEvent.value}")
            }
        })



        setupBottomNav()
    }

    /**
     * Set up [androidx.drawerlayout.widget.DrawerLayout] with [androidx.appcompat.widget.Toolbar]
     */
    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        val m = binding.drawerNavView.menu
        m.clear()
        val menu = m.addSubMenu("時間管理").setIcon(R.drawable.ic_home_black_24dp)


        menu.add("All").setIcon(R.drawable.ic_nav_profile).setOnMenuItemClickListener {
            Log.d("zxc", "All")
            viewModel.selectAttendee.value = ""
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setOnMenuItemClickListener true
        }
        val s = UserManager.myDate.value?.map {
            it.name
        }
        Log.d("zxc", "$s")
        if (s != null) {
            for (i in s) {
                menu.add(i).setIcon(R.drawable.baseline_favorite_border_black_36)
                    .setOnMenuItemClickListener {
                        Log.d("zxc", i)
                        viewModel.selectAttendee.value = i
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        return@setOnMenuItemClickListener true
                    }
            }
        } else {
            Log.d("zxcERR", "有問題")
        }


//        // Set up header of drawer ui using data binding
//        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
//            LayoutInflater.from(this), binding.drawerNavView, false)
//
//        bindingNavHeader.lifecycleOwner = this
//        bindingNavHeader.viewModel = viewModel
//        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

//        // Observe current drawer toggle to set the navigation icon and behavior
//        viewModel.currentDrawerToggleType.observe(this, Observer { type ->
//
//            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
//            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
//            binding.toolbar.setNavigationIcon(
//                when (type) {
//                    DrawerToggleType.BACK -> R.drawable.toolbar_back
//                    else -> R.drawable.toolbar_menu
//                }
//            )
//            actionBarDrawerToggle?.setToolbarNavigationClickListener {
//                when (type) {
//                    DrawerToggleType.BACK -> onBackPressed()
//                    else -> {}
//                }
//            }
//        })

    }


    /**
     * override back key for the drawer design
     */
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}


