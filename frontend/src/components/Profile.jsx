import React from "react";


import '../styles/Profile.css'
import ProfileInfo from "./ProfileInfo";

const Profile = ({profile}) => {

    return (
        <div className="Account ">
          <div className="Frame-936">

         <ProfileInfo profile ={profile}/>
          </div>
    
             
            

        </div>
    )




}

export default Profile;