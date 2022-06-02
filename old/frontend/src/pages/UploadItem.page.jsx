import React from 'react'
import { Link } from 'react-router-dom'

import CreateSingleImg from '../assets/create-single-img.jpg'
import CreateMultiImg from '../assets/create-multi-img.jpg'

import '@Styles/UploadItemPage.css'

const UploadItemPage = () => {
    const renderOption = (image, text) => {
		return (
			<div className='create-option'>
				<img src={image} />
				<Link className='create-option-link btn btn-white' to='single'>{text}</Link>
			</div>
		)
	}

	return (
		<div className='UploadItemPage'>
			<div className='container'>
				<div className='headline'>
					<h2>Upload Item</h2>
					<p>
						Choose “Single” if you want your collectible to be one
						of a kind or “Multiple” if you want to sell one
						collectible multiple times
					</p>
				</div>

                <div className='options-container'>
                    {renderOption(CreateSingleImg, 'Create Single')}
                    {renderOption(CreateMultiImg, 'Create Multiple')}
                </div>

                <div className='footline'>
                    <p>We do not own your private keys and cannot access your funds without your confirmation.</p>
                </div>
			</div>
		</div>
	)
}

export default UploadItemPage
