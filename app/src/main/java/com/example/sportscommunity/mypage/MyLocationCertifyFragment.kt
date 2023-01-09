package com.example.sportscommunity.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.getLocationBtn.setOnClickListener {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(requireContext(), "위치 정보를 활성화 시켜주세요", Toast.LENGTH_SHORT).show()
            } else {
                checkPermission()
            }
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
    fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
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

    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)  // 전달받은 위치를 좌표로 마커를 생성
        /* 마커 추가 및 옵션 */
        val markerOptions = MarkerOptions()  // 마커 추가
            .position(LATLNG)  // 마커의 좌표
            .title("현재 위치")  // 마커의 제목
        // .snippet("정보창 추가")

        /* 카메라 위치를 현재 위치로 세팅하고 마커와 함께 지도에 반영 */
        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)  // 카메라의 목표 지점
            .zoom(15.0f)  // 카메라 줌
            .build()
        // bearing : 지도의 수직선이 북쪽을 기준으로 시계 방향 단위로 측정되는 방향
        // tilt : 카메라의 기울기

        mMap.clear()  // 마커를 지도에 반영하기 전에 clear를 사용해서 이전에 그려진 마커가 있으면 지운다.
        mMap.addMarker(markerOptions)?.showInfoWindow()  // 지도에 마커를 추가
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}