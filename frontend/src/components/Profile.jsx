import React from "react";
import { Link } from "react-router-dom";


import '../styles/Profile.css'
import ProfileInfo from "./ProfileInfo";

const Profile = ({profile}) => {
 
    return (
     
        <div className="Account ">
           <div className="Container">
          <div className="cover">
               <div className="Action-button ">
                  <button  className=" Editcover" >
                  <label className="coverbutton"> Edit cover photo</label>
                  </button>
                  <Link className="Editprofile" to='/updateprofile'> 
                  <label className="profilebutton"> Edit Profile</label>
                  </Link>   
               </div>
          </div>         
          <div className="Frame-936">

               <ProfileInfo profile ={profile}/>
              <div className="Frame-935">
                     <div className="sub-navigation">

                     </div>
              </div>

          </div>
        </div>
        </div>
    )




}

export default Profile;