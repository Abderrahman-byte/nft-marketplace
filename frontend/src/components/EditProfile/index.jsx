import React from 'react'

import ProfileSettings from './ProfileSettings'

import './styles.css'

const EditProfile = ({ profile, setProfile }) => {
	return (
		<div className='Edit-profile'>
			<div className='Headline'>
				<h2> Edit profile </h2>
				<span className='Headline-span'>
					You can set preferred display name, create
					<span className='text-style-1'> your profile URL </span>
					and manage other personal settings.
				</span>
			</div>
			<ProfileSettings profile={profile} setProfile={setProfile} />
		</div>
	)
}
export default EditProfile
