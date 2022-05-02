import React, { useContext, useEffect, useState } from 'react'

import ProfileCover from '@/components/ProfileCover'
import ProfileInfoCard from '@/components/ProfileInfoCard'
import ProfileNavbar from '@/components/ProfileNavbar'
import ProfileTokenList from '@/components/ProfileTokenList'
import { AuthContext } from '@/context/AuthContext'
import { Navigate, Route, Routes } from 'react-router'
import { getUserForSaleTokens, getUserCreatedTokens, getUserOwnedTokens, getUserFavoriteTokens } from '@Utils/api'

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

export default ProfilePage
