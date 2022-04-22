import React from "react";
import { Link } from 'react-router-dom'
import '../styles/ProfileHeader.css'

const ProfileHeader = () => {



    return (
        <div className="container">
            <div className="Breadcrumb-wrap">

                <button>
                    <i className='arrow-left-icon'></i>
                    <label className="backtoprofile"> Back to profile</label>
                </button>
                <div className="Breadcrumb">
                    <Link className='headerprofile-link' to='#' >Profile</Link>
                    <i className='arrow-right-icon'></i>
                    <Link className='headerprofile-link' to='#' >Edit profile</Link>
                </div>

            </div>
            <div className="Divider" />
        </div>
    )




}

export default ProfileHeader;