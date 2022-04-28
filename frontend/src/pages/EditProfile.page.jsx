import React, {useEffect, useState}from "react";

import ProfileHeader from "@Components/ProfileHeader";
import EditProfile from "@Components/EditProfile";
import { getProfile } from "@Utils/api";

import '@Styles/EditProfilePage.css'

const EditProfilepage = () => {
    const [profile, setProfile]= useState({});

    const getProfileData = async () => {
        const profile = await getProfile();   
        
        setProfile(profile)
    }

     useEffect(()=>{
        getProfileData();
     },[])

    return (
        <div className="EditProfilePage">           
            <ProfileHeader/>
            <div className='horizontal-divider' />
            <EditProfile profile={profile} setProfile={setProfile}/>
        </div>
    )
}

export default EditProfilepage;



