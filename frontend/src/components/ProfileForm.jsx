import React, { useState } from "react";

import AccountInfo from "./AccountInfo";
import { sendProfile } from "../utils/api";
import { Link } from "react-router-dom";

import'../styles/ProfileForm.css'
import Profilepage from "../pages/Profile.page";

const ProfileForm = ({profile, setprofile})=>{
   
   /*const submit = async (e) => {
      e.preventDefault()
      console.log(items)
      const [success, err] = await sendProfile(items);
      console.log(err)
      console.log(success)
    } */
    
    const submit = async (e) => {
      e.preventDefault()
     const data={
        displayName : profile.displayName,
        customUrl : profile.customUrl,
        bio: profile.bio
      }
      console.log(profile.displayName)
      console.log(data)
     const [success, err] = await sendProfile(data);
     console.log(err)
     console.log(success)
     
    }
    

  return (
     
      <form className="ProfileForm" onSubmit={submit}>
           {/*<AccountInfo displayname={displayname} setdisplayname={setdisplayname}  />*/}
           <AccountInfo /*items={items} setitems={setitems}*/ profile={profile} setprofile={setprofile} />
           <div className="Social">
              <span>
                  Social  
              </span>
              <div >
              <label>portfolio or website</label>
              <input name='search' placeholder='EnterUrl' className='Social-input' autoComplete='off' />
              </div>
              
              <button>
                 <label > Add more social account </label>
              </button>

           </div>
           <span > To update your settings you should sign message through your wallet. Click 'Update profile' then sign the message  </span>
           <div className="Divider"></div>

           <div className="buttons"> 
           <button className='btn btn-blue' type="submit">Update Profile</button>
           <div className="Frame-942">
            <i className="clearall"> </i> <label> Clear all </label>
            </div>
           </div>
      </form>
  )


}

export default ProfileForm;