import React from "react";


import '../styles/AccountInfo.css'



const AccountInfo = () => {

    return (

        <div className="accountInfo">
            <span className="Account-Info">
                Account Info
            </span>
            <div className="Display-name">
                <label > Display Name</label>
                <input name='search' placeholder=' Enter your display name' className='Display-name-input' autoComplete='off' />
            </div>
            <div className="CustomUrl">
                <label > Custom URL</label>     
                <input name='search' placeholder=' Enter your display name' className='CustomUrl-input' autoComplete='off' />

            </div>
            <div className="Bio">
                <label >Bio</label>
                <input name='search' placeholder='  About yourselt in a few words' className='Bio-input' autoComplete='off' />
            </div>

        </div>

    )



}

export default AccountInfo;