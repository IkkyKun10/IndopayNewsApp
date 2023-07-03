package com.riezki.indopaynewsapp.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import com.riezki.indopaynewsapp.R
import com.riezki.indopaynewsapp.adapter.NewsAdapter
import com.riezki.indopaynewsapp.databinding.ActivityHomeBinding
import com.riezki.indopaynewsapp.ui.detail.DetailActivity
import com.riezki.indopaynewsapp.utils.Resource
import com.riezki.indopaynewsapp.utils.ViewModelFactory
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        newsAdapter = NewsAdapter { news ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.INTENT_TO_DETAIL, news)
            startActivity(intent)
        }
        setRecyclerView()

        showNewsHeadline()

        getMyLastLocation()

        binding.shareLocBtn.setOnClickListener {
            binding.progressBarLoc.visibility = View.VISIBLE

            try {
                getMyLastLocation()

                binding.latText.text = getString(R.string.latitude_info, currentLocation?.latitude.toString())
                binding.lonText.text = getString(R.string.longitude_info, currentLocation?.longitude.toString())
                binding.cityName.text = getString(
                    R.string.name_of_city,
                    getCityName(currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0)
                )
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Acces Location Not Granted", Snackbar.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }

            binding.progressBarLoc.visibility = View.GONE
        }

        //Handle Category News
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.category_health -> {
                    viewModel.getHeadlineNews(category = CATEGORY_HEALTH).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Resource.Loading -> {
                                    binding.progressBarRv.visibility = View.VISIBLE
                                }

                                is Resource.Success -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    newsAdapter.submitList(result.data)
                                }

                                is Resource.Error -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    Toast.makeText(this, "Error get data $CATEGORY_HEALTH", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    true
                }

                R.id.category_science -> {
                    viewModel.getHeadlineNews(category = CATEGORY_SCIENCE).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Resource.Loading -> {
                                    binding.progressBarRv.visibility = View.VISIBLE
                                }

                                is Resource.Success -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    newsAdapter.submitList(result.data)
                                }

                                is Resource.Error -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    Toast.makeText(this, "Error get data $CATEGORY_SCIENCE", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    true
                }

                R.id.category_sports -> {
                    viewModel.getHeadlineNews(category = CATEGORY_SPORTS).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Resource.Loading -> {
                                    binding.progressBarRv.visibility = View.VISIBLE
                                }

                                is Resource.Success -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    newsAdapter.submitList(result.data)
                                }

                                is Resource.Error -> {
                                    binding.progressBarRv.visibility = View.GONE
                                    Toast.makeText(this, "Error get data $CATEGORY_SPORTS", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun showNewsHeadline() {
        viewModel.getHeadlineNews().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBarRv.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarRv.visibility = View.GONE
                        newsAdapter.submitList(result.data)
                    }

                    is Resource.Error -> {
                        binding.progressBarRv.visibility = View.GONE
                        Toast.makeText(this, "Error get data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = newsAdapter
            clipToPadding = false
        }

    }

    //permission location
    private fun checkPermission(permission: String): Boolean {
        return this.let {
            ContextCompat.checkSelfPermission(
                it, permission
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.getCurrentLocation(
                PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    currentLocation = loc

                }
            }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }

            permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }

            else -> {

            }
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        var cityName: String? = ""
        var countryName: String? = ""
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 3)

        cityName = address?.get(0)?.subAdminArea
        countryName = address?.get(0)?.countryName
        val area = address?.get(0)?.adminArea
        val subArea = address?.get(0)?.locality
        val subLocal = address?.get(0)?.subLocality
        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")

        return String(StringBuilder("$countryName, $area \n$cityName, $subArea, $subLocal"))
    }

    companion object {

        const val CATEGORY_HEALTH = "health"
        const val CATEGORY_SPORTS = "sports"
        const val CATEGORY_SCIENCE = "science"

    }
}