package com.max.timemaster

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.max.timemaster.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.max.timemaster.databinding.ActivityMainBinding
import com.max.timemaster.util.CurrentFragmentType
import com.max.timemaster.util.SetColorStateList
import com.max.timemaster.util.UserManager


class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null

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

    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.calendarFragment -> CurrentFragmentType.CALENDAR
                R.id.favoriteFragment -> CurrentFragmentType.FAVORITE
                R.id.costFragment -> CurrentFragmentType.COST
                R.id.profileFragment -> CurrentFragmentType.PROFILE

                else -> viewModel.currentFragmentType.value
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.myNavHostFragment)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.liveUser.observe(this, Observer {
            it?.let { user ->
                UserManager.user = user
                UserManager.exp.value = UserManager.user.exp
            }
        })

        UserManager.exp.observe(this, Observer {
            viewModel.upUserExp(it)
        })

        viewModel.liveMyDate.observe(this, Observer {
            it?.let { listMyDate ->
                UserManager.myDate.value = listMyDate
                setupDrawer()
            }
        })

        viewModel.liveAllEvent.observe(this, Observer {
            it?.let { allEvents ->
                UserManager.allEvent.value = allEvents
            }
        })

        setupBottomNav()
        setupNavController()
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false
        binding.drawerNavView.itemIconTintList = null
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {

        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        val navMenu = binding.drawerNavView.menu
        navMenu.clear()
        val menu = navMenu.addSubMenu(getString(R.string.drawer_title_text))

        menu.add(getString(R.string.drawer_item_text))
            .setIcon(R.drawable.bg_publish)
            .setIconTintList(SetColorStateList.setColorStateList(getString(R.string.main_color_text)))
            .setOnMenuItemClickListener {
            viewModel.selectAttendee.value = ""
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            return@setOnMenuItemClickListener true
        }

        val activeDate = UserManager.myDate.value?.filter {
            it.active == true
        }

        if (activeDate != null) {
            for (date in activeDate) {

                menu.add(date.name)
                    .setIcon(R.drawable.bg_publish)
                    .setIconTintList(date.color?.let { SetColorStateList.setColorStateList(it) })
                    .setOnMenuItemClickListener {
                        viewModel.selectAttendee.value = date.name
                        binding.drawerLayout.closeDrawer(GravityCompat.START)

                        return@setOnMenuItemClickListener true
                    }
            }
        }
    }


    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}


