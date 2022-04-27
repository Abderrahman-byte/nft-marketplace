import React from "react";
import CreateSingleImg from '@Assets/create-single-img.jpg'


import '@Styles/ProfileInfo.css'


const ProfileInfo = ({profile}) => {

    return (
        <div className="ProfileInfo ">
          <div className="Frame-928">
          <div className="Frame-934">     
          <div className="Avatar">
            
            <img className="Pic" src={profile.avatarUrl} />
           </div>
           <div className="Frame-930">
           <div className="Name">
              <span>{profile.displayName} </span> 
           </div>
           <div className="Bio">
                {profile.bio}
           </div>
           <div className="adress">
             <i className="global"> </i>
             <span>  {profile.customUrl} </span>
            </div>
            </div>
            </div>

            <div className="Frame-932">
               <i className="share-icon"></i>
               <i className="more-icon"></i>
            </div>

            <div className="social-icons">
               <i className="twitter"></i>
               <i className="instagram"></i>
               <i className="facebook"> </i>

            </div>

          </div>
    
             
            

        </div>
    )




}

export default ProfileInfo;