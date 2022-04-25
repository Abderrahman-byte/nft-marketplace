import React from "react";
import CreateSingleImg from '../assets/create-single-img.jpg'


import '../styles/ProfileInfo.css'


const ProfileInfo = ({profile}) => {

    return (
        <div className="ProfileInfo ">
          <div className="Frame-928">
               
          <div className="Avatar">
            
            <img className="Pic" src={CreateSingleImg} />
           </div>
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
    )




}

export default ProfileInfo;