import React, {useEffect, useState}from "react";

import { getProfile } from "@Utils/api";
import ProfileCover from "@/components/ProfileCover";
import ProfileInfoCard from "@/components/ProfileInfoCard";

import '@Styles/ProfilePage.css'
import ProfileNavbar from "@/components/ProfileNavbar";

const ProfilePage = () => {
    const [profile, setprofile]= useState({});

    const getProfileData = async () => {       
        const profile = await getProfile();
     
        setprofile(profile)
         console.log("profile page")
    }

    useEffect(()=>{
        getProfileData();
    },[])

    return (
        <div className="ProfilePage">
            <ProfileCover allowUpdate profile={profile} />
            <div className="container">
                <ProfileInfoCard profile={profile} />
                <div className="profile-pages">
                    <ProfileNavbar />
                    <div className="profile-content">
                        
                    </div>
                </div>
            </div>
        </div>
    )




}

export default ProfilePage;



