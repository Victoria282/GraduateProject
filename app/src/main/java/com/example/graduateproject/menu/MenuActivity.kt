package com.example.graduateproject.menu

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.authentication.firebase.Firebase
import com.example.graduateproject.databinding.ActivityMenuBinding
import com.example.graduateproject.di.utils.FragmentFactory
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.rate.RateDialog
import com.example.graduateproject.shared_preferences.Storage
import com.example.graduateproject.utils.Constants
import com.example.graduateproject.utils.Constants.FIRST_WEEK
import com.example.graduateproject.utils.Constants.SECOND_WEEK
import com.example.graduateproject.utils.Constants.WIDGET_ACTION
import com.example.graduateproject.widget.ScheduleWidget
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MenuActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var scheduleWidget: ScheduleWidget

    @Inject
    lateinit var firebase: Firebase

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private val viewModel: MenuViewModel by viewModels { viewModelFactory }
    private lateinit var rateDialog: RateDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        initNavigationView()
        initNavigationViewHeader()
        registerReceiver()
        rateDialog = RateDialog(this, firebase)
    }

    override fun onStart() {
        super.onStart()
        checkReviewAppExisting()
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(WIDGET_ACTION)
        registerReceiver(scheduleWidget, filter)
    }

    private fun initNavigationView() = with(binding) {
        setSupportActionBar(appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = drawerLayout
        val navView: NavigationView = navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)

        navView.menu.findItem(R.id.log_out).setOnMenuItemClickListener {
            logOut()
            true
        }

        navView.menu.findItem(R.id.nav_about_developer).setOnMenuItemClickListener {
            writeEmailMessage()
            true
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_schedule,
                R.id.notesFragment,
                R.id.mapFragment,
                R.id.settingsFragment,
                R.id.expenseFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initNavigationViewHeader() {
        val header = binding.navView.getHeaderView(0)
        val email = header.findViewById<TextView>(R.id.userEmail)
        email.text = viewModel.getUserInfo()?.email
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        checkStudyWeek(menu)
        return true
    }

    private fun checkStudyWeek(menu: Menu) {
        if (!Storage.studyWeek)
            menu.findItem(R.id.switchWeek).title = FIRST_WEEK
        else
            menu.findItem(R.id.switchWeek).title = SECOND_WEEK
    }

    fun logOut() {
        viewModel.logOut()
        Intent(this, MainActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(it)
        }
        finish()
    }

    fun updateReceiver() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this, ScheduleWidget::class.java))
        for (id in ids)
            ScheduleWidget.updateAppWidget(this, appWidgetManager, id)
    }

    private fun checkReviewAppExisting() {
        if (Storage.visitingApp == 3 && !Storage.rateUs) {
            rateDialog.setCancelable(true)
            rateDialog.show()
        } else if (Storage.visitingApp < 3) {
            Storage.visitingApp++
        }
    }

    private fun writeEmailMessage() {
        val action = Intent.ACTION_SENDTO
        val uri = Uri.fromParts("mailto", Constants.DEVELOPER_EMAIL, null)
        val emailIntent = Intent(action, uri)
        val tittle = getString((R.string.email_sending))
        startActivity(Intent.createChooser(emailIntent, tittle))
    }
}