import React from "react";
import { Link } from "react-router-dom";
import ProfileHeader from "./ProfileHeader";

import '../styles/Profile.css'
import EditProfile from "./EditProfile";

const Profile = () => {

    return (
        //<div>
        //<ProfileHeader/>
        /* <div className="Profile EditProfile">
             <div className="Headline">
              

             </div>

         </div>*/

        //</div>
        <div className="Edit-ProfileDesktopLight">


            
           
                  <ProfileHeader/>
                  <EditProfile/>

         
           { /*
            <div className="Headline">
            <span className="Edit-profile"> Edit profile  </span>
            <span className="Headline-span">You can set preferred display name, create
                <span className="text-style-1"> your profile URL </span>
                 and manage other personal settings.
            </span>
            </div>
*/}  
        </div>
    )




}

export default Profile;