import React from "react";

const AccountInfo = ({ profile }) => {
    return (
        <div className="AccountInfo form-div">
            <h3>Account Info</h3>

            <div className="form-subdiv">
                <label className="form-label">Display Name </label>
                <input name='displayName' placeholder='Enter your display name'
                 defaultValue=  {profile?.displayName}
                 autoComplete='off'
                 className='form-input' />
            </div>

            <div className="form-subdiv">
                <label className="form-label">Custom URL</label>     
                 <input name='customUrl' placeholder='Enter your display name' defaultValue={profile?.customUrl} autoComplete='off' className='form-input'  />
            </div>

            <div className="form-subdiv">
                <label className="form-label">Bio</label>
                <textarea name='bio' placeholder='About yourselt in a few words' defaultValue={profile?.bio} className='form-input'></textarea>
            </div>
        </div>
    )
}

export default AccountInfo;