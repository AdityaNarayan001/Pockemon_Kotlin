package com.aditya.pockemon

import android.location.Location

class Pockemon {

    var name:String?=null
    var des:String?=null
    var image:Int?=null    //image is Int because we will import from Resource
    var power:Double?=null
    //var lat:Double?=null
    //var log:Double?=null
    var isCatch:Boolean?=false
    var location:Location?=null
    constructor(image:Int,name:String,des:String,power:Double,lat:Double,log:Double){
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.location= Location(name)
        this.location!!.latitude=lat
        this.location!!.longitude=log
        //this.lat=lat
        //this.log=log
        this.isCatch=isCatch
    }
}
