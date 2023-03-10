import React from 'react'
import { Link } from 'react-router-dom'

import { formatDate } from '@Utils/generic'

import './styles.css'

const ProfileInfoCard = ({ profile }) => {
	return (
      <div className='ProfileInfoCard'>
         <img src={profile?.avatarUrl} alt={profile?.displayName} />
         <div className='info'>
            <h2 className='name'>{profile?.displayName || profile?.username}</h2>
            <p className='bio'>{profile?.bio}</p>
            {profile?.website ? (
               <a href={profile?.website} target='_blank' className='website-link'>
                  <i className='globe-icon'></i>
                  {profile?.website}
               </a>
            ) : null}
         </div>

         <div className='controll-btns'>
            <Link to='#'><i className='share-icon'></i> </Link>
            <Link to='#'><i className='more-icon'></i> </Link>
         </div>

         <div className='social-media-icons'>
            <Link to='#'><i className='twitter-icon'></i></Link>
            <Link to='#'><i className='instagram-icon'></i></Link>
            <Link to='#'><i className='facebook-icon'></i></Link>
         </div>

         <div className='horizontal-divider' />

         <p className='card-footer'>Member since {formatDate(profile?.createdDate)}</p>
      </div>
   )
}

export default ProfileInfoCard
