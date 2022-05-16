import React, { useContext, useEffect, useState } from 'react'
import { Navigate, Route, Routes, useParams } from 'react-router'

import ProfileCover from '@Components/ProfileCover'
import ProfileInfoCard from '@Components/ProfileInfoCard'
import ProfileNavbar from '@Components/ProfileNavbar'
import ProfileTokenList from '@Components/ProfileTokenList'
import { AuthContext } from '@Context/AuthContext'
import { getUserCreatedTokens, getUserData, getUserFavoriteTokens, getUserForSaleTokens, getUserOwnedTokens } from '@Utils/api'

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
					
					{profile && profile.id ? (
						<Routes>
							<Route index element={<Navigate to='./sale' />} />
							<Route path='sale' element={<ProfileTokenList getTokensFunction={getUserForSaleTokens} id={profile.id} />} />
							<Route path='collectibles' element={<ProfileTokenList getTokensFunction={getUserOwnedTokens} id={profile.id} />} />
							<Route path='created' element={<ProfileTokenList getTokensFunction={getUserCreatedTokens} id={profile.id} />} />
							<Route path='likes' element={<ProfileTokenList getTokensFunction={getUserFavoriteTokens} id={profile.id} />} />
						</Routes>
					) : null}
				</div>
			</div>
		</div>
	)
}

export default UserProfilePage
