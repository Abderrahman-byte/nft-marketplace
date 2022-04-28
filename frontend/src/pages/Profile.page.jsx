import React, { useContext, useEffect, useState } from 'react'

import { getProfile } from '@Utils/api'
import ProfileCover from '@/components/ProfileCover'
import ProfileInfoCard from '@/components/ProfileInfoCard'

import ProfileNavbar from '@/components/ProfileNavbar'
import { AuthContext } from '@/context/AuthContext'

import '@Styles/ProfilePage.css'

const ProfilePage = () => {
	const { account:profile } = useContext(AuthContext)

	return (
		<div className='ProfilePage'>
			<ProfileCover allowUpdate profile={profile} />
			<div className='container'>
				<ProfileInfoCard profile={profile} />
				<div className='profile-pages'>
					<ProfileNavbar />
					<div className='profile-content'></div>
				</div>
			</div>
		</div>
	)
}

export default ProfilePage
