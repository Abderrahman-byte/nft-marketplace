import React, {useEffect, useState}from "react";

import ProfileHeader from "@Components/ProfileHeader";
import EditProfile from "@Components/EditProfile";
import { getProfile } from "@Utils/api";

import '@Styles/UpdateProfilepage.css'

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



