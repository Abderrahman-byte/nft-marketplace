import React from 'react'

import AccountInfo from './AccountInfo'

// TODO : Must Check data validity
// TODO : Submit changes only

const ProfileForm = ({ profile, saveProfileCallback }) => {
	const submitCallback = (e) => {
		e.preventDefault()

		const elts = e.target.elements

		const data = {
			displayName: elts.displayName.value,
			customUrl: elts.customUrl.value,
			bio: elts.bio.value,
		}

		saveProfileCallback(data)
	}

	return (
		<form className='ProfileForm form' onSubmit={submitCallback}>
			<AccountInfo profile={profile} />

			<div className='form-div social-info-div'>
				<h3>Social</h3>

				<div className='form-subdiv'>
					<label className='form-label'>portfolio or website</label>
					<input placeholder='Enter Url' className='form-input' autoComplete='off' />
				</div>

				<button type='button' className='btn btn-white'>Add more social account</button>
			</div>

			<div className='form-div bottom-msg-div'>
				<p>To update your settings you should sign message through your wallet. Click 'Update profile' then sign the message</p>
			</div>

			<div className='buttons'>
				<button className='btn btn-blue' type='submit'>Update Profile</button>
				<button className='btn btn-white clear-all-btn'>
					<i className='timer-icon'></i>Clear all
				</button>
			</div>
		</form>
	)
}

export default ProfileForm
