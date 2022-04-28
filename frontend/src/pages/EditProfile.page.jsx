import React, {useContext, useEffect, useState}from "react";

import ProfileHeader from "@Components/ProfileHeader";
import EditProfile from "@Components/EditProfile";
import { AuthContext } from "@/context/AuthContext";

import '@Styles/EditProfilePage.css'

const EditProfilepage = () => {
    const { account:profile, setProfileData:setProfile } = useContext(AuthContext)

    return (
        <div className="EditProfilePage">           
            <ProfileHeader/>
            <div className='horizontal-divider' />
            <EditProfile profile={profile} setProfile={setProfile}/>
        </div>
    )
}

export default EditProfilepage;



