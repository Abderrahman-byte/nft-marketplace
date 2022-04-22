import React from "react";

import AccountInfo from "./AccountInfo";
import { Link } from "react-router-dom";

import'../styles/ProfileForm.css'

const ProfileForm = ()=>{


  return (
      <div className="ProfileForm">
           <AccountInfo/>
           
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
           <Link className='btn btn-blue' to='#'>Update Profile</Link>
           </div>
      </div>
  )


}

export default ProfileForm;