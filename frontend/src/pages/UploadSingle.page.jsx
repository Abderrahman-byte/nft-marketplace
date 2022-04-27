import React from 'react'
import { Link } from 'react-router-dom'

import CreateSingleItemForm from '@Components/CreateTokenForm'

import '@Styles/UploadSinglePage.css'
import '@Styles/UploadItem.css'

const UploadSinglePage = () => {
	return (
		<div className='UploadSinglePage UploadItem'>
			<div className='container'>
				<div className='left-side'>
                    <div className='form-header'>
                        <h2>Create Single collectible</h2>
                        <Link className='btn btn-white' to='#'>Switch to multiple</Link>
                    </div>
                    <CreateSingleItemForm />
				</div>
				<div className='preview'></div>
			</div>
		</div>
	)
}

export default UploadSinglePage
