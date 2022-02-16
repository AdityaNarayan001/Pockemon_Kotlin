package com.aditya.pockemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.aditya.pockemon.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPockemon()
    }

    var ACCESSLOCATION = 123
    fun checkPermission()
    {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat
                    .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return
            }
        }
        GetUserLocation()
    }

    fun GetUserLocation(){
        Toast.makeText(this, "User Location Access ON", Toast.LENGTH_LONG).show()
        //TODO : Will implement later

        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var mythread = myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            ACCESSLOCATION ->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access to your Location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
           //we can zoom from 1f to 24f
    }

    var location:Location?=null

    // Get User Location

    inner class MyLocationListener:LocationListener {

        constructor(){
            location= Location("Start")
            location!!.longitude =0.0
            location!!.latitude =0.0
        }
        override fun onLocationChanged(p0: Location) {
            location=p0
        }
    }
    var oldLocation:Location?=null
    inner class myThread:Thread{
        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0

        }
        override fun run(){
            while (true){
                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }
                    oldLocation = location
                    runOnUiThread {
                        mMap!!.clear()
                        //show me
                        val sydney =
                            LatLng(location!!.latitude, location!!.longitude)   // hard coded location
                        mMap.addMarker(
                            MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("My location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        //show Pockemons
                        for(i in 0 until listPockemon.size){
                            var newPockemon = listPockemon[i]

                            if(newPockemon.isCatch==false){
                                val pockemonLoc =
                                    LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)   // hard coded location
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(pockemonLoc)
                                        .title(newPockemon.name)
                                        .snippet("Description: " + newPockemon.des+ " , " + "Power: " + newPockemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                                if (location!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.isCatch=true
                                    listPockemon[i]=newPockemon
                                    playerPower+=newPockemon.power!!
                                    Toast.makeText(applicationContext,"You got new Pockemon, Your new Power " + playerPower, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){}
            }
        }
    }
    var playerPower = 0.0
    var listPockemon = ArrayList<Pockemon>()

    fun LoadPockemon(){

        listPockemon.add(
            Pockemon(R.drawable.charmander,
            "Charmander",
            "He is from Japan",
            55.0,
            37.33,
            -122.0)
        )

        listPockemon.add(Pockemon(R.drawable.bulbasaur,
            "Bulbasaur",
            "He is from USA",
            90.5,
            37.794,
            -122.410))

        listPockemon.add(Pockemon(R.drawable.squirtle,
            "Squirtle",
            "He is from Iraq",
            33.5,
            37.781,
            -122.412))
    }
}
