import React, {useEffect, useState}from "react";
import { Link } from "react-router-dom";
import ProfileHeader from "../components/ProfileHeader";
import EditProfile from "../components/EditProfile";
import { getProfile } from "../utils/api";
import'../styles/UpdateProfilepage.css'

const UpdateProfilepage = () => {
    const [profile, setprofile]= useState({});
    const submitCallback = async () => {
       
       
        const [success, Profile] = await getProfile();
     
        console.log("1111---->"+success)
        console.log(Profile)
        setprofile(Profile)
        console.log("here object")
        console.log(Profile)
     }
     useEffect(()=>{

         console.log("this is use effect")
         submitCallback();
     },[])

    return (
        <div className="UpdateProfile">           
                  <ProfileHeader/>
                  <EditProfile profile={profile} setprofile={setprofile}/>

        </div>
    )




}

export default UpdateProfilepage;



