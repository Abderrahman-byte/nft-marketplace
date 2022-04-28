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
		openModel(<LoadingCard />)

		const [profileSaved] = await updateProfile(data)

		if (image?.file && !image?.saved) {
			const [imageSaved] = await saveProfilePicture(image.file, 'avatar')
			setimage({...image, saved: imageSaved})
		}

		if (profileSaved) setProfile({...profile, ...data})

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
