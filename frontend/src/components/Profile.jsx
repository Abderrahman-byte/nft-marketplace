import React, {useState, useEffect} from "react";
import { Link } from "react-router-dom";

import '../styles/Profile.css'
import ProfileInfo from "./ProfileInfo";


const Profile = ({profile, pictureChanged}) => {
    //const [imageUrl, setImageUrl] = useState(profile.cover)
    const myStyle={
        backgroundImage: `url(${profile.cover})`,
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
    };

   
   
  
    return (
     
        <div className="Account ">
           <div className="Container">
          <div className="cover" style={myStyle}>
            
               <div className="Action-button ">
                  <button  className=" Editcover" >
                  <label for="img" className="coverbutton"> Edit cover photo</label>
                  <input className='inputfile' type="file" id="img" name="img" accept="image/*" onChange={pictureChanged}  />
                  

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