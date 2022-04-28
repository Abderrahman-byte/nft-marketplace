import React from 'react'
import { Link } from 'react-router-dom'
import './styles.css'

const ProfileHeader = () => {
	return (
		<div className='ProfileHeader container Breadcrumb-wrap'>
			<Link to='/profile' className='btn btn-white'>
				<i className='arrow-left-icon'></i>
				<label className='backtoprofile'> Back to profile</label>
			</Link>

			<div className='Breadcrumb'>
				<Link className='headerprofile-linkp' to='#'>
					Profile
				</Link>
				<i className='arrow-simple-icon'></i>
				<Link className='headerprofile-link' to='#'>
					Edit profile
				</Link>
			</div>
		</div>
	)
}

export default ProfileHeader
