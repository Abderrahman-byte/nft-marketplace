import React from "react";


import '../styles/AccountInfo.css'



const AccountInfo = ({ profile, setprofile}) => {

    return (

        <div className="accountInfo">
            <span className="Account-Info">
                Account Info 
            </span>
            <div className="Display-name">
                <label > Display Name </label>
                <input onChange={e => setprofile({...profile, displayName : e.target.value})} name='search' placeholder=' Enter your display name'
                 defaultValue=  {profile.displayName}
                /* defaultValue={` ${profile === null ? 'h': profile.displayName}`}*/
                 className='Display-name-input' />
            </div>
            <div className="CustomUrl">
                <label > Custom URL</label>     
                 <input onChange={e=> setprofile({ ...profile, customUrl : e.target.value })}  name='search' placeholder=' Enter your display name'  defaultValue={profile.customUrl } className='CustomUrl-input'  />
            </div>
            <div className="Bio">
                <label >Bio</label>
               <input onChange={e=> setprofile({ ...profile, bio : e.target.value })} name='search' placeholder='  About yourselt in a few words'  defaultValue={profile.bio } className='Bio-input' autoComplete='off' />

            </div>
           
        </div>

    )



}

export default AccountInfo;