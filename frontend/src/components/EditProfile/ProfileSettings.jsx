import React, { useContext, useEffect, useState } from 'react'

import ProfilePhotoInput from './ProfilePhotoInput'
import ProfileForm from './ProfileForm'

import { saveProfilePicture, updateProfile } from '../../utils/api'
import { AuthContext } from '@/context/AuthContext'
import LoadingCard from '../LoadingCard'

const ProfileSettings = ({ profile, setProfile }) => {
	const [image, setimage] = useState({ url: profile?.avatarUrl, file: null, saved: true })

	const { openModel, closeModel } = useContext(AuthContext)

	useEffect(() => {
		if (profile && profile.avatarUrl && (!image || !image.url))  {
			setimage({url: profile?.avatarUrl, file: null })
		}
	}, [profile])

	const saveProfileCallback = async (data) => {
		let savedData = {}
		openModel(<LoadingCard />)

		const [profileSaved] = await updateProfile(data)

		if (profileSaved) savedData = {...profile, ...data}

		if (image?.file && !image?.saved) {
			const [imageSaved] = await saveProfilePicture(image.file, 'avatar')
			setimage({...image, saved: imageSaved})

			if (imageSaved) savedData = {...savedData, avatarUrl: image.url}
		}

		if (Object.entries(savedData).length > 0)
			setProfile({...profile, ...savedData})

		closeModel()
	}

	return (
		<div className='ProfileSettings'>
			<ProfilePhotoInput image={image} setimage={setimage} />
			<ProfileForm
				profile={profile}
				saveProfileCallback={saveProfileCallback}
			/>
		</div>
	)
}

export default ProfileSettings
