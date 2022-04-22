import React from 'react'

import ProfilePhoto from './ProfilePhoto'
import ProfileForm from './ProfileForm'
import '../styles/ProfileSettings.css'

const ProfileSettings = () => {
	return (
		<div className='ProfileSettings'>
			<ProfilePhoto />
			<ProfileForm />
		</div>
	)
}

export default ProfileSettings
