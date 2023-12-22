package com.bangkit.huggingpet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.adapter.ArticleListAdapter
import com.bangkit.huggingpet.database.ListPetDetail
import com.bangkit.huggingpet.databinding.ActivityPetArticleBinding
import com.bangkit.huggingpet.preference.Preference
import com.bangkit.huggingpet.viewmodel.DataStoreViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModelFactory
import com.bangkit.huggingpet.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PetArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Pet Articles"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
