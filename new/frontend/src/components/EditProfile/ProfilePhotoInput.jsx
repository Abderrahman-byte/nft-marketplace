import React, { useEffect, useState } from 'react'

const ProfilePhotoInput = ({ image, setimage }) => {
	const [imageUrl, setImageUrl] = useState(image?.url)

	useEffect(() => {
		if (image != null && image != imageUrl) setImageUrl(image?.url)
	}, [image])

	const pictureChanged = (e) => {
		const files = e.target.files

		if (files.length <= 0) return

		const fileUrl = URL.createObjectURL(files[0])

		setimage({ url: fileUrl, file: files[0], saved: false})
	}

	return (
		<div className='ProfilePhoto'>
			<div className='Avatar'>
				<img className='Pic' src={imageUrl} />
			</div>

			<div className='Frame-944'>
				<div className='Frame-945'>
					<h3 className='Profile'>Photo de profile </h3>
					<span className='Recomendation'>We recommend an image of at least 400x400. Gifs work too 🙌</span>
				</div>
				<div>
					<label htmlFor='img' className='btn btn-white'> Upload </label>
					<input
						className='inputfile'
						type='file'
						id='img'
						name='img'
						accept='image/*'
						onChange={pictureChanged}
					/>
				</div>
			</div>
		</div>
	)
}

export default ProfilePhotoInput
