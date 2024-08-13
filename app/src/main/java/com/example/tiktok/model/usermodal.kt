package com.example.tiktok.model

data class usermodal

    (  var id:String?=null,
       var email:String?=null,
       var name:String?=null,
       var profilepic:String?=null,
       var followerList:MutableList<String> = mutableListOf(),
       var followingList:MutableList<String> = mutableListOf()
){
}