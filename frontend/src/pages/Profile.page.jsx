import React, {useEffect, useState}from "react";

import { getProfile } from "@Utils/api";
import ProfileCover from "@/components/ProfileCover";
import ProfileInfoCard from "@/components/ProfileInfoCard";

import '@Styles/ProfilePage.css'

const ProfilePage = () => {
    const [profile, setprofile]= useState({});

    const getProfileData = async () => {       
        const profile = await getProfile();
     
        setprofile(profile)
    }

    useEffect(()=>{
        getProfileData();
    },[])

    return (
        <div className="ProfilePage">
            <ProfileCover updateCover profile={profile} />
            <div className="container">
                <ProfileInfoCard profile={profile} />
            </div>
        </div>
    )




}

export default ProfilePage;



