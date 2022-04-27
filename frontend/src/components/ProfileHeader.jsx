import React from "react";
import { Link } from 'react-router-dom'
import '@Styles/ProfileHeader.css'

const ProfileHeader = () => {



    return (
        <div className="ProfileHeader container">
            <div className="Breadcrumb-wrap">

                <Link  to= "/profile" className="btn">
                    <i className='arrow-left-icon'></i>
                    <label className="backtoprofile"> Back to profile</label>
                </Link>
                <div className="Breadcrumb">
                    <Link className='headerprofile-linkp' to='#' >Profile</Link>
                    <i className='arrow-simple-icon'></i>
                    <Link className='headerprofile-link' to='#' >Edit profile</Link>
                </div>

            </div>
            <div className="Divider" />
        </div>
    )




}

export default ProfileHeader;