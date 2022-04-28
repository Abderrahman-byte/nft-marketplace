import React, { useContext, useEffect, useState } from 'react'

import ProfileCover from '@/components/ProfileCover'
import ProfileInfoCard from '@/components/ProfileInfoCard'
import ProfileNavbar from '@/components/ProfileNavbar'
import { AuthContext } from '@/context/AuthContext'

import '@Styles/ProfilePage.css'

const ProfilePage = () => {
	const { account } = useContext(AuthContext)
	const [profile, setProfile] = useState(account)

	useEffect(() => {
		setProfile(account)
	}, [account])

	return (
		<div className='ProfilePage'>
			<ProfileCover allowUpdate={account && profile && profile?.id === account?.id} profile={profile} />
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
