import React, { useContext, useEffect, useState } from 'react'

import ProfileCover from '@/components/ProfileCover'
import ProfileInfoCard from '@/components/ProfileInfoCard'
import ProfileNavbar from '@/components/ProfileNavbar'
import { useParams } from 'react-router'
import { AuthContext } from '@/context/AuthContext'
import { getUserData } from '@/utils/api'

import '@Styles/ProfilePage.css'

const UserProfilePage = () => {
	const { account } = useContext(AuthContext)
	const [profile, setProfile] = useState(undefined)

	const { id } = useParams()

	const getProfileData = async () => {
		if (!id) setProfile(null)

		const userData = await getUserData(id)
		setProfile(userData)
	}

	useEffect(() => {
		getProfileData()
	}, [])

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

export default UserProfilePage
