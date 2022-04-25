import React from 'react'

import ProfilePhoto from './ProfilePhoto'
import ProfileForm from './ProfileForm'
import '../styles/ProfileSettings.css'

const ProfileSettings = ({profile, setprofile}) => {
	return (
		<div className='ProfileSettings'>
			<ProfilePhoto profile = {profile} />
			<ProfileForm profile = {profile}  setprofile ={setprofile}/>
		</div>
	)
}

export default ProfileSettings
