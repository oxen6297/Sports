package com.example.sportscommunity.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.FragmentMyLocationCertifyBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MyLocationCertifyFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var mBinding: FragmentMyLocationCertifyBinding? = null
    private val binding get() = mBinding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyLocationCertifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)
        val locationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        binding.run {
            binding.getLocationBtn.setOnClickListener {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(requireContext(), "위치 정보를 활성화 시켜주세요", Toast.LENGTH_SHORT).show()
                } else {
                    checkPermission()
                }
            }

            val areaAdapter =
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.areaList,
                    R.layout.spinner_dropdown_item
                )
            areaAdapter.setDropDownViewResource(R.layout.spinner_item_style)
            areaSpinner.adapter = areaAdapter

            areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> detailAreaSpinner(R.array.noSelect)
                        1 -> detailAreaSpinner(R.array.seoulItem)
                        2 -> detailAreaSpinner(R.array.gyungiList)
                        3 -> detailAreaSpinner(R.array.incheonList)
                        4 -> detailAreaSpinner(R.array.daeJeonList)
                        5 -> detailAreaSpinner(R.array.daeGuList)
                        6 -> detailAreaSpinner(R.array.ulsanList)
                        7 -> detailAreaSpinner(R.array.jeonnamList)
                        8 -> detailAreaSpinner(R.array.jeonbukList)
                        9 -> detailAreaSpinner(R.array.chungnamList)
                        10 -> detailAreaSpinner(R.array.chungbukList)
                        11 -> detailAreaSpinner(R.array.jejuList)
                        12 -> detailAreaSpinner(R.array.sejongList)
                        13 -> detailAreaSpinner(R.array.gwangjuList)
                        14 -> detailAreaSpinner(R.array.busanList)
                        15 -> detailAreaSpinner(R.array.gyungbukList)
                        16 -> detailAreaSpinner(R.array.gyungnamList)
                        17 -> detailAreaSpinner(R.array.gangwonList)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            certifyBtn.setOnClickListener {
                checkLocationInfo()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun detailAreaSpinner(item: Int) {
        binding.run {
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                item,
                R.layout.spinner_dropdown_item
            )
            adapter.setDropDownViewResource(R.layout.spinner_item_style)
            areaSpinnerTwo.adapter = adapter
        }
    }

    private fun checkLocationInfo() {
        //구글 지도 위치 인증 함수
        val area =
            binding.areaSpinner.selectedItem.toString() + " " + binding.areaSpinnerTwo.selectedItem.toString()
        Log.d("myArea", area)
        if (binding.myLocateText.text.toString() == "나의 현재 위치: $area") {
            Toast.makeText(requireContext(), "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(
                requireContext(), "인증 실패\n위치 정보를 확인해주세요.", Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun checkPermission() {

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startProcess()
            Toast.makeText(requireContext(), "위치를 불러오고 있습니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
        }

        // 권한이 없을 때 권한을 요구함
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    1
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.root, "위치 권한이 동의 되었습니다.", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "권한에 동의하지 않을 경우 이용할 수 없습니다.", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startProcess() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        updateLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.let {
                    for (location in it.locations) {
                        setLastLocation(location)
                        Log.d("startProcesses", "success")
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("SetTextI18n")
    fun setLastLocation(lastLocation: Location) {
        val geocoder = Geocoder(requireContext())
        val lat = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(lat)
            .title("현재 위치")

        val cameraPosition = CameraPosition.Builder().target(lat).zoom(14f).build()

        mMap.clear()
        mMap.addMarker(markerOptions)?.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        val address =
            geocoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1)
        val addressSubLocate = address[0].locality.toString()
        val addressLocate = address[0].adminArea
        val addressLastLocate = address[0].subLocality
        binding.myLocateText.text = "나의 현재 위치: $addressLocate $addressSubLocate $addressLastLocate"
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}