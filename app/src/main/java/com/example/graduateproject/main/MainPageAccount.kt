package com.example.graduateproject.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.graduateproject.R
import org.chromium.base.Log

class MainPageAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        // Показать сообщение (Вы уверены, что хотите выйти? 'да' / 'отмена')
        // logOut из Firebase
        Log.e("tag", "logOut")
    }
}