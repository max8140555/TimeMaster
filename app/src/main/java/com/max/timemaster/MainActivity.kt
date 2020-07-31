package com.max.timemaster

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.max.timemaster.util.UserManager


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





        viewModel.postUser(UserManager.user)

        viewModel.getLiveUserResult()
        viewModel.getLiveMyDateResult()
        viewModel.getAllEventResult()
        viewModel.liveUser.observe(this, Observer {
            it?.let {
                UserManager.user = viewModel.liveUser.value!!
                UserManager.exp.value = UserManager.user.exp
            }
        })
        UserManager.exp.observe(this, Observer {
            viewModel.updateExp(it)
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
        setupNavController()
    }

    /**
     * Set up [androidx.drawerlayout.widget.DrawerLayout] with [androidx.appcompat.widget.Toolbar]
     */
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
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        val m = binding.drawerNavView.menu
        m.clear()
        val menu = m.addSubMenu("時間管理")

        val states = arrayOf(intArrayOf(-android.R.attr.state_checked))

        val myColor = intArrayOf(Color.parseColor("#265D7C"))
        val myColorStateList = ColorStateList(states, myColor)
        menu.add("所有對象").setIcon(R.drawable.bg_publish).setIconTintList(myColorStateList).setOnMenuItemClickListener {
                Log.d("zxc", "All")
                viewModel.selectAttendee.value = ""
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                return@setOnMenuItemClickListener true
            }
        val s = UserManager.myDate.value?.filter {
            it.active == true
        }

        if (s != null) {
            for (i in s) {

                val colors = intArrayOf(Color.parseColor("#${i.color}"))
                val colorsStateList = ColorStateList(states, colors)
                Log.d("zxc", i.color)

                menu.add(i.name).setIcon(R.drawable.bg_publish).setIconTintList(colorsStateList).setOnMenuItemClickListener {
                    Log.d("zxc", i.color)
                        viewModel.selectAttendee.value = i.name
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        return@setOnMenuItemClickListener true
                    }
            }
        } else {
            Log.d("zxcERR", "有問題")
        }
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


